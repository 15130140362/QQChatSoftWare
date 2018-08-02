package ui;

import config.Config;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
/**
 * Created by hello on 2018/4/25.
 */

public class friendList extends JPanel {

    JLabel lbface, lbname, lbinfo;

    public static Hashtable<String, friendFace> list = new Hashtable<String, friendFace>();

    public friendList() {
        this.setLayout(null);
        genxin();
        Config.friendList = this;
        this.setPreferredSize(new Dimension(0, 985));
        friendOnlineUpdate();
    }


    //更新列表
    public void genxin() {
        //好友列表
        String friendList = Config.hy_json_data;
        //在线列表
        if (friendList.equals("")) {
            return;
        }

        JSONArray arrayJson = JSONArray.fromObject(friendList);

        if (list.size() == 0) {
            //第一次加载列表
            for (int i = 0; i < arrayJson.size(); i++) {
                JSONObject jsonObject = (JSONObject) arrayJson.get(i);
                friendFace friendFace = new friendFace(jsonObject.getString("img"),
                        jsonObject.getString("netname"),
                        jsonObject.getString("info"),
                        jsonObject.getString("uid"));
                list.put(jsonObject.getString("uid"), friendFace);
            }
        } else {// 已有列表数据
            for (int i = 0; i < arrayJson.size(); i++) {
                JSONObject jsonObject = (JSONObject) arrayJson.get(i);
                String uid = jsonObject.getString("uid");
                friendFace friendFace = list.get(uid);
                if (friendFace != null)//已有
                {
                    friendFace.setName(jsonObject.getString("netname"));
                    friendFace.setImage(jsonObject.getString("img"));
                    friendFace.setInfo(jsonObject.getString("info"));
                } else {//新建
                    friendFace friendFace1 = new friendFace(jsonObject.getString("img"),
                            jsonObject.getString("netname"),
                            jsonObject.getString("info"),
                            jsonObject.getString("uid"));

                    list.put(jsonObject.getString("uid"), friendFace1);
                }
            }
        }
    }

    public void friendOnlineUpdate() {
        String online = Config.hy_online;
        String[] uids = online.split(",");
        Set<String> keys = list.keySet();
        //全部下线
        for (String string : keys) {
            list.get(string).setOnline(false);
        }

        if (!online.equals("notfound")) {
            for (String uid : uids) {
                friendFace friendFace = list.get(uid);
                friendFace.setOnline(true);
            }
        }

        Collection<friendFace> collection = list.values();
        List<friendFace> list1 = new ArrayList(collection);
        Collections.sort(list1);
        this.removeAll();
        this.updateUI();
        int i = 0;
        for (friendFace friendFace : list1) {
            //  System.out.println(friendFace.getUid() + " " + friendFace.isOnline());
            friendFace.setBounds(0, i * 60+i++*5, 279, 70);
            friendFace.change();
            this.add(friendFace);
        }
        this.updateUI();
        this.repaint();
    }
}
