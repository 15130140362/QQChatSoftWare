package server;

import exception.UsernameException;
import model.UserService;
import net.sf.json.JSONObject;
import sendEmail.sendCode;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created by hello on 2018/4/24.
 */

public class registerServer implements Runnable {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public registerServer(Socket socket) {
        this.socket = socket;
    }

    //所有的验证码都在这里
    public static HashMap<String, String> hashMap = new HashMap<String, String>();


    public void run() {
        String string = "";
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            string = getString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = JSONObject.fromObject(string);
        String type = json.getString("type");
        String username = json.getString("username");
        if (type.equals("code")) {
            try {
                identifyCode(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("reg")) {
            String password = json.getString("password1");
            String code = json.getString("code");
            String code1 = hashMap.get(username);
            if (code1 != null) {
                hashMap.remove(username);
            }
            if (code.equals(code1)) {
                try {
                    new UserService().registerUser(username, password);
                } catch (UsernameException e) {
                    try {
                        outputStream.write(("{\"state\":1,\"msg\":\"用户存在\"}").getBytes());
                        outputStream.flush();
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (SQLException e) {
                    try {
                        outputStream.write(("{\"state\":2,\"msg\":\"未知错误\"}").getBytes());
                        outputStream.flush();
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    e.printStackTrace();
                }
                try {
                    outputStream.write(("{\"state\":0,\"msg\":\"注册成功\"}").getBytes());
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    outputStream.write(("{\"state\":2,\"msg\":\"验证码错误\"}").getBytes());
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public StringBuffer proCode() {
        StringBuffer code = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code;
    }

    private void identifyCode(String username) throws IOException {
        if (username.contains("@")) {
            StringBuffer stringBuffer = proCode();
            hashMap.put(username, stringBuffer.toString());
            if (sendCode.sendEmail(username, stringBuffer)) {
                outputStream.write("{\"state\":0,\"msg\":\"验证码发送成功\"}".getBytes());
                outputStream.flush();
            } else {
                outputStream.write("{\"state\":1,\"msg\":\"验证码发送失败\"}".getBytes());
                outputStream.flush();
            }
        } else {
            outputStream.write("{\"state\":1,\"msg\":\"用户名不正确\"}".getBytes());
            outputStream.flush();
        }
    }


    public String getString(InputStream in) throws IOException {
        byte[] bytes = new byte[1024 * 10];
        int len = in.read(bytes);
        return new String(bytes, 0, len);
    }

    public static void OpenServer() throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(1000);
        ServerSocket serverSocket = new ServerSocket(4002);
        while (true) {
            Socket socket = serverSocket.accept();
            service.execute(new registerServer(socket));
        }
    }
}
