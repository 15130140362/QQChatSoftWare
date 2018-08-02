package ui;

import config.Config;
import model.msg;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by hello on 2018/4/26.
 */
public class friendFace extends JPanel implements Comparable<friendFace>, MouseListener ,Runnable{
    private String image;
    private String netname;
    private String info;
    private String uid;
    private JLabel lbface, lbname, lbinfo;
    private boolean online = false;

    public String getNetname() {
        return netname;
    }

    public String getInfo() {
        return info;
    }

    public String getUid() {
        return uid;
    }

    public boolean isOnline() {
        return online;
    }

    int x = 0, y = 0;

    public void change() {
        ClassLoader classLoader = friendList.class.getClassLoader();
        this.remove(lbface);
        URL url;
        if (online) {
            url = classLoader.getResource("headImage/" + image + ".jpg");
        } else {
            url = classLoader.getResource("headImage1/" + image + ".jpg");
        }
        lbface = new JLabel(new ImageIcon(url));
        lbface.setBounds(5, 5, 60, 60);
        this.add(lbface);
        this.updateUI();
    }

    public friendFace(String image, String netname, String info, String uid) {
        this.image = image;
        this.netname = netname;
        this.info = info;
        this.uid = uid;
        this.setLayout(null);

        setImage(image);
        lbname = new JLabel();
        lbinfo = new JLabel();

        lbname.setText(netname);
        lbinfo.setText(info);
        lbface.setBounds(5, 5, 60, 60);
        lbname.setBounds(75, 0, 200, 40);
        lbinfo.setBounds(75, 35, 200, 20);


        this.add(lbface);
        this.add(lbname);
        this.add(lbinfo);
        this.setBackground(new Color(196, 221, 242));
        this.addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        x = this.getX();
        y = this.getY();
    }

    public String getImage() {
        return image;
    }

    //重新改头像
    public void setImage(String image) {
        ClassLoader classLoader = friendList.class.getClassLoader();
        URL url;
        if (image.equals("def")) {
            image = "0";
        }


        String uidonline = Config.hy_online;
        String[] uids = uidonline.split(",");
        if (!uidonline.equals("notfound")) {
            for (String uid : uids) {
                this.setOnline(true);
            }
        }
        if (online) {
            url = classLoader.getResource("headImage/" + image + ".jpg");
        } else {
            url = classLoader.getResource("headImage1/" + image + ".jpg");
        }
        lbface = new JLabel(new ImageIcon(url));
        this.image = image;
    }


    public void setName(String netname) {
        lbname.setText(netname);
        this.netname = netname;
    }

    public void setInfo(String info) {
        lbinfo.setText(info);
        this.info = info;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }


    //存储所有的消息
    private Vector<msg> vector=new Vector<msg>();
   //寄存消息
    public void addMessage(msg msg){
        vector.add(msg);
        if(thread==null)
        {
            thread=new Thread(this);
            thread.start();
        }else if(thread.getState()==Thread.State.TERMINATED){
            thread=new Thread(this);
            thread.start();
        }else if(!run)
        {
            thread=new Thread(this);
            thread.start();
        }

    }


    boolean run=true;
    private Thread thread=null;
    public void run() {
        run=true;
        int x=this.getX();
        int y=this.getY();
        while(run) {
            this.setLocation(x-2,y-2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.setLocation(x+2,y+2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        this.setLocation(x,y);
    }

    public int compareTo(friendFace o) {
        if (o.online) {
            return 1;//你比他小
        } else if (this.online) {
            return -1;//比他大
        } else {
            return 0;
        }
    }


    public void mouseClicked(MouseEvent e) {
        run=false;
        try {
            chatInterface.addChatTo(this.uid,this.netname);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        for(msg m:vector)
        {
            chatInterface.addOtherMsg(m);
        }
        vector.clear();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        this.setLocation(x - 5, y - 5);
    }

    public void mouseExited(MouseEvent e) {
        this.setLocation(x + 5, y + 5);
    }


}
