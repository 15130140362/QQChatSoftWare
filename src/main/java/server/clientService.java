package server;

import config.Config;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created by hello on 2018/4/22.
 * 通讯服务
 * 1更新在线状态
 * 2登陆验证
 * 3退出账户
 */
public class clientService implements Runnable {
    private clientService() {
    }

    private static clientService clientService = new clientService();

    public static clientService getClientService() {
        return clientService;
    }
    //单例模式

    private Socket socket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private Thread thread = null;
    private boolean run = false;

    //长时间通讯
    public void run() {
        try {
            output.write("U0001".getBytes());
            output.flush();
            //得到好友信息
            /////////////////////////////////////////
            Config.hy_json_data = getString(input);

            Config.jiexi_haoyou_json(Config.hy_json_data);


            output.write("U0003".getBytes());
            output.flush();
            //个人资料的获得
            Config.geren_json_data = getString(input);

            output.write("G0001".getBytes());
            output.flush();
            Config.groupInfo=getString(input);

            //启动udp服务器
            Config.datagramSocket_client = new DatagramSocket();

            new heartBeat(Config.datagramSocket_client);
            new messageServer(Config.datagramSocket_client);

            //解析成为好友的列表

            while (run) {
                output.write("U0002".getBytes());
                output.flush();
                input.read();
                output.write(Config.hyList.getBytes());
                output.flush();
                Config.hy_online = getString(input);

                String string = "";
                if (!string.equals(Config.hy_online)) {
                    try {
                        string = Config.hy_online;
                        Config.friendList.friendOnlineUpdate();
                    } catch (Exception e) {

                    }
                }
                Thread.sleep(5000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            run = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getString(InputStream in) throws IOException {
        byte[] bytes = new byte[1024 * 10];
        int len = in.read(bytes);
        return new String(bytes, 0, len);
    }

    public JSONObject login() throws IOException {
        socket = new Socket(Config.IP, Config.LOGIN_PORT);
        input = socket.getInputStream();
        output = socket.getOutputStream();
        String jsonStr = "{\"username\":\"" + Config.username + "\"," +
                "\"password\":\"" + Config.password + "\"}";
        //传递消息，发送用户名和密码
        output.write(jsonStr.getBytes());
        output.flush();

        //等待服务器回消息
        JSONObject json = JSONObject.fromObject(getString(input));

        if (json.getInt("state") == 0) {

            //重新开始通讯
            run = true;
            thread = new Thread(this);
            thread.start();
        }
        return json;
    }

}
