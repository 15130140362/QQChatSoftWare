package model;

import java.net.Socket;
/**
 * Created by hello on 2018/4/21.
 */
public class userInfo {
    private String uid;
    private String phonne;
    private String email;
    private Socket socket;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhonne() {
        return phonne;
    }

    public void setPhonne(String phonne) {
        this.phonne = phonne;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUdpip() {
        return udpip;
    }

    public void setUdpip(String udpip) {
        this.udpip = udpip;
    }

    public int getUdpport() {
        return udpport;
    }

    public void setUdpport(int udpport) {
        this.udpport = udpport;
    }

    private String udpip;
    private int udpport;
}
