package server;

import exception.NummberSyntaxError;
import exception.PasswordNotFoundException;
import exception.StateException;
import exception.UsernameNotFoundException;
import model.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import model.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 负责登陆服务器
 * Created by hello on 2018/4/21.
 */
public class loginServer implements Runnable {
    private Socket socket;

    public loginServer(Socket socket) {
        this.socket = socket;
    }

    /*将接收到的数据转换成为String
    * */
    public String getString(InputStream in) throws IOException {
        byte[] bytes = new byte[1024 * 10];
        int len = in.read(bytes);
        return new String(bytes, 0, len);
    }


    public void run() {
        InputStream in = null;
        OutputStream out = null;
        String uid = "";

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            //等待信息
            JSONObject jsonObject = JSONObject.fromObject(getString(in));
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            try {

                uid = new UserService().loginForEmail(username, password);
                userOnLineList.getUserOnLineList().regOnline(uid, socket, username, null);

                out.write("{\"state\":0,\"msg\":\"登陆成功\"}".getBytes());
                out.flush();
            } catch (UsernameNotFoundException e) {
                out.write("{\"state\":2,\"msg\":\"没有用户\"}".getBytes());
                out.flush();
                e.printStackTrace();
            } catch (PasswordNotFoundException e) {
                out.write("{\"state\":1,\"msg\":\"密码错误\"}".getBytes());
                out.flush();
                e.printStackTrace();
            } catch (StateException e) {
                out.write("{\"state\":4,\"msg\":\"状态错误\"}".getBytes());
                out.flush();
                e.printStackTrace();
            } catch (SQLException e) {
                out.write("{\"state\":4,\"msg\":\"未知错误\"}".getBytes());
                out.flush();
                e.printStackTrace();
            }
            //数据库上的验证
            //陆陆续续的接收客户端发送的指令
            while (true) {
                String command = "";
                    command = getString(in);
                //更新好友列表(昵称,uid,)
                if (command.equals("U0001")) {
                    try {
                        Vector<friendListInfo> vector =
                                new UserService().getmyFriendList(uid);
                        out.write(JSONArray.fromObject(vector).toString().getBytes());
                        out.flush();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else if(command.equals("G0001")){
                    Vector<groupInfo> vector=new UserService().getMyGroupList(uid);
                    out.write(JSONArray.fromObject(vector).toString().getBytes());
                    out.flush();
                } else
                    //更新好友的在线的状态 5秒钟更新一次
                    if (command.equals("U0002")) {
                        //获得好友的列表编号
                        out.write(1);
                        out.flush();
                        //发送过来原来在线的好友
                        String ids = getString(in);
                        String[] ids1 = ids.split(",");
                        StringBuffer stringBuffer = new StringBuffer();

                        for (String string : ids1) {
                            if (userOnLineList.getUserOnLineList().isUserOnline(string)) {
                                stringBuffer.append(string);
                                stringBuffer.append(",");
                            }
                        }

                        if (stringBuffer.length() == 0) {
                            out.write("notfound".getBytes());
                            out.flush();
                        } else {
                            //回执好友在线的列表
                            out.write(stringBuffer.toString().getBytes());
                            out.flush();
                        }

                    } else
                        //个人资料更新
                        if (command.equals("U0003")) {
                            try {
                                personInfo personInfo = new UserService().getUserInfo(uid);
                                out.write(JSONObject.fromObject(personInfo).toString().getBytes());
                                out.flush();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else
                            //个人资料保存
                            if (command.equals("E0001")) {

                            } else
                                //退出
                                if (command.equals("EXIT")) {
                                    userOnLineList.getUserOnLineList().logout(uid);
                                    return;
                                }
            }

        } catch (NummberSyntaxError e) {
            try {
                out.write("{\"state\":4,\"msg\":\"格式错误!\"}".getBytes());
                out.flush();
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                //突然关闭或者关闭要在列表里面关闭此账户
                userOnLineList.getUserOnLineList().logout(uid);
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void openServer() throws IOException {
        //线程池1000个
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        //开启4001
        ServerSocket serverSocket = new ServerSocket(4001);
        //可以无限的服务
        while (true) {
            //接受socket
            Socket socket = serverSocket.accept();
            socket.setSoTimeout(10000);//毫秒
            executorService.execute(new loginServer(socket));
        }
    }
}
