package config;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ui.groupList;
import ui.friendList;

import java.net.DatagramSocket;

/**
 * Created by hello on 2018/4/22.
 */
public class Config {
    /*
        public static final String IP="192.168.76.1";
    */

    //发送duan端，接受端，心跳端
    public static DatagramSocket datagramSocket_client = null;
    /*   public static final String IP = "192.168.43.41";
   */
    public static final String IP = "127.0.0.1";

    public static final int LOGIN_PORT = 4001;
    public static final int REGISTER_PORT = 4002;
    //用户名密码
    public static String username;
    public static String password;
    public static friendList friendList;
    public static String hyList = "";
    public static groupList groupList;
    public static String hy_json_data = "";
    public static String geren_json_data = "";
    public static String groupInfo="";


    public static void jiexi_haoyou_json(String hy_json_data) {
        //Config.hy_json_data=hy_json_data;
        JSONArray json = JSONArray.fromObject(hy_json_data);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < json.size(); i++) {
            JSONObject jsonObject = (JSONObject) json.get(i);
            stringBuffer.append(jsonObject.getString("uid"));
            stringBuffer.append(",");
        }
        hyList = stringBuffer.toString();
    }
    public static String hy_online = "";
   // public static String filename = "";
   // public static StringBuffer FileContent = new StringBuffer();
}
