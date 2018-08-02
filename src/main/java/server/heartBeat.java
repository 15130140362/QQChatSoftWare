package server;

import config.Config;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by hello on 2018/4/29.
 */
public class heartBeat extends Thread {

    private DatagramSocket client = null;

    public heartBeat(DatagramSocket client) {
        this.client = client;
        this.start();
    }


    //每十秒心跳一下
    @Override
    public void run() {
        String uid = JSONObject.fromObject(Config.geren_json_data).
                getString("uid");
        String string = "{\"type\":\"reg\",\"myuid\":\"" + uid + "\"}";
        byte[] bytes = string.getBytes();
        while (true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(bytes,
                        bytes.length, InetAddress.getByName(Config.IP),4003);
                //更新消息发送给服务器
                client.send(datagramPacket);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
