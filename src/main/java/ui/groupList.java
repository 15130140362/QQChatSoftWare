package ui;

import config.Config;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Created by hello on 2018/5/9.
 */
public class groupList extends JPanel {
    public static Hashtable<String, groupFace> list = new Hashtable<String, groupFace>();

    public groupList() throws SQLException {
        this.setLayout(null);
        String groupList = Config.groupInfo;

        if (groupList.equals("")) {
            return;
        }

        JSONArray arrayList = JSONArray.fromObject(groupList);
        for (int i = 0; i < arrayList.size(); i++) {
            JSONObject jsonObject = (JSONObject) arrayList.get(i);
            groupFace g = new groupFace(jsonObject.getString("groupimage"),
                    jsonObject.getString("groupname"),
                    jsonObject.getString("groupuid"));
            list.put(jsonObject.getString("groupuid"), g);
        }
        int i = 0;
        for (groupFace g : list.values()) {
            g.setBounds(0, i * 60 + i++ * 5, 279, 70);
            this.add(g);
        }
        this.updateUI();
        this.repaint();
        Config.groupList = this;
        this.setPreferredSize(new Dimension(0, 985));
    }
}
