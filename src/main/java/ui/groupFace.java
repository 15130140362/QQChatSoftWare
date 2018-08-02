package ui;

import model.UserService;
import model.friendListInfo;
import model.msg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by hello on 2018/5/9.
 */
public class groupFace extends JPanel implements Runnable {
    private String img;
    private String groupName;
    private String uid;
    private JLabel lbface, lbname;

    public String getImg() {
        return img;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    int x, y;

    public groupFace(String image, final String netname, final String uid) throws SQLException {
        lbface = new JLabel();
        lbname = new JLabel();
        this.setLayout(null);
        this.setImg(image);
        this.setGroupName(netname);
        this.setUid(uid);
        ClassLoader classLoader = groupFace.class.getClassLoader();
        URL url;

        if (image.equals("def")) {
            image = "0";
        }

        url = classLoader.getResource("headImage/" + image + ".jpg");
        assert url != null;
        lbface = new JLabel(new ImageIcon(url));
        lbface.setBounds(5, 5, 60, 60);
        lbname = new JLabel(netname);
        lbname.setBounds(75, 0, 200, 40);


        this.add(lbface);
        this.add(lbname);
        this.setBackground(new Color(196, 221, 242));

        final Vector<friendListInfo> vector2=new UserService().getMemberInfo(uid);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                run=false;
                chatInterface.addGroupChatTo(uid,netname,vector2);
                for(msg m:vector)
                {
                    chatInterface.addGroupMsg(m);
                }
                vector.clear();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                x = getX();
                y = getY();
                super.mouseEntered(e);
                setLocation(x - 5, y - 5);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                x = getX();
                y = getY();
                super.mouseExited(e);
                setLocation(x + 5, y + 5);
            }
        });
    }

    private Vector<msg> vector = new Vector<msg>();

    private Thread thread = null;

    boolean run = true;

    public void addMessage(msg msg) {
        vector.add(msg);
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        } else if (thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(this);
            thread.start();
        } else if (!run) {
            thread = new Thread(this);
            thread.start();
        }
    }


    public void run() {
        run = true;
        int x = this.getX();
        int y = this.getY();
        while (run) {
            this.setLocation(x - 2, y - 2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.setLocation(x + 2, y + 2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.setLocation(x, y);
        }
    }


}
