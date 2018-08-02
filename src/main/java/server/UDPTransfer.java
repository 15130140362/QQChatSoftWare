package server;

import model.userInfo;
import model.userOnLineList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hello on 2018/4/29.
 */
public class UDPTransfer implements Runnable {
    private DatagramPacket packet = null;

    public UDPTransfer(DatagramPacket datagramPacket) {
        this.packet = datagramPacket;
    }

    public void run() {
        String string = new String(packet.getData(), 0, packet.getLength());
        JSONObject json = JSONObject.fromObject(string);

        if (json.getString("type").equals("reg")) {
            String myUid = json.getString("myuid");
            userOnLineList.getUserOnLineList().updateOnlineUdp(myUid,
                    packet.getAddress().getHostAddress(),
                    packet.getPort());

        } else if (json.getString("type").equals("msg") || json.getString("type").equals("qr")) {
            String myuid = json.getString("myuid");
            String touid = json.getString("touid");
            userOnLineList.getUserOnLineList().updateOnlineUdp(myuid,
                    packet.getAddress().getHostAddress()
                    , packet.getPort());
            sendMessage(touid, packet);
        } else if (json.getString("type").equals("sendfile")) {
            String myuid = json.getString("myuid");
            String touid = json.getString("touid");
            userOnLineList.getUserOnLineList().updateOnlineUdp(myuid, packet.getAddress()
                    .getHostAddress(), packet.getPort());
            sendMessage(touid, packet);
        } else if (json.getString("type").equals("groupInfo")) {
            String myuid = json.getString("myuid");
            // String touid = json.getString("touid");
            userOnLineList.getUserOnLineList().updateOnlineUdp(myuid, packet.getAddress()
                    .getHostAddress(), packet.getPort());
            JSONArray jsonArray = json.getJSONArray("vector");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                //接收者的uid
                String uid = jsonObject.getString("uid");
                if (!uid.equals(myuid))//不向自己发送了
                    sendMessage(uid, packet);
            }
        }
    }

    public void sendMessage(String uid, DatagramPacket packet) {
        if (userOnLineList.getUserOnLineList().isUserOnline(uid)) {
            userInfo touserInfo = userOnLineList.getUserOnLineList().getOnlineuserInfo(uid);
            try {
                DatagramPacket datagramPacket = new DatagramPacket(packet.getData(), packet.getLength(),
                        InetAddress.getByName(touserInfo.getUdpip()), touserInfo.getUdpport());
                datagramSocket.send(datagramPacket);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static DatagramSocket datagramSocket = null;

    public static void openServer() throws IOException {
        datagramSocket = new DatagramSocket(4003);
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        while (true) {
            byte[] b = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(b, b.length);
            datagramSocket.receive(datagramPacket);
            executorService.execute(new UDPTransfer(datagramPacket));
        }
    }
}
