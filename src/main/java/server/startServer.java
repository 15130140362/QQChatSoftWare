package server;

import java.io.IOException;

/**
 * Created by hello on 2018/4/24.
 */

public class startServer {
    public static void main(String[] args) {
        new Thread() {
            public void run(){
                try {
                    System.out.println("登陆服务器启动。");
                    loginServer.openServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    System.out.println("注册服务器启动。");
                    registerServer.OpenServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            public void run() {
                try {
                    System.out.println("消息服务器启动。");
                    UDPTransfer.openServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
