package server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
/**
 * 接受服务区的中转消息
 * Created by hello on 2018/4/29.
 */
//接收消息
public class messageServer extends  Thread{
    private DatagramSocket client = null;
    public messageServer(DatagramSocket client) {
        this.client = client;
        this.start();
    }
    @Override
    public void run() {
        while (true) {
            try {
                byte[] bytes=new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(bytes,
                        bytes.length);
                client.receive(datagramPacket);

                messagePool.getMessagePool().addMessage(
                        new java.lang.String(datagramPacket.getData(),0,datagramPacket.getData().length)
                );


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
