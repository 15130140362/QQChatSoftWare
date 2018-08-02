package model;

import exception.NummberSyntaxError;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by hello on 2018/4/21.
 */
//用户列表
public class userOnLineList {
    private userOnLineList() {}
    private static userOnLineList userOnLineList = new userOnLineList();

    public static userOnLineList getUserOnLineList() {
        return userOnLineList;
    }

    /*
    * string 是用户的编号*/
    public HashMap<String, userInfo> hashMap = new HashMap<String, userInfo>();

    //注册在线的用户
    //抢线（要先看有没有用户）
    public void regOnline(String uid, Socket socket, String email, String phoneNumber) throws IOException, NummberSyntaxError {
        userInfo userInfo = hashMap.get(uid);
        if (userInfo != null) {
            //4关闭
            //userInfo.getSocket().getOutputStream().write(4);
            userInfo.getSocket().close();
        }
        //重新登陆
        userInfo = new userInfo();
        userInfo.setUid(uid);
        userInfo.setEmail(email);
        userInfo.setPhonne(phoneNumber);
        userInfo.setSocket(socket);
        hashMap.put(uid, userInfo);
    }

    //更新udp
    public void updateOnlineUdp(String uid, String ip, int port) {
        userInfo userInfo = hashMap.get(uid);
        userInfo.setUdpip(ip);
        userInfo.setUdpport(port);
    }

    //判断是否在线
    public boolean isUserOnline(String uid) {
        return hashMap.containsKey(uid);
    }

    //查询在线的用户的信息
    public userInfo getOnlineuserInfo(String uid) {
        return hashMap.get(uid);
    }

    /*
    * 下线*/

    public void logout(String uid) {
        hashMap.remove(uid);
    }

    //返回用户的编号
    public Set<String> getUserInfo() {
        return hashMap.keySet();
    }
}
