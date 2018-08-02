package ui;

import config.Config;
import model.FileContent;
import model.friendListInfo;
import model.groupChatInfo;
import model.msg;
import net.sf.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

/**
 * Created by hello on 2018/4/20.
 */
public class chatInterface {
    private static JLabel jLabel1 = new JLabel("群成员列表");
    private static JTextArea Area = new JTextArea();
    private static JScrollPane jScrollPane = new JScrollPane(Area);
    private static JFrame chatJFrame = new JFrame();
    private JLabel lbLeft, lbRight, myface, myname, myinfo;
    private Point origin = new Point();
    private JTabbedPane jTabbedPane;
    private JScrollPane jsp1, jsp2;
    private JButton close, minSize;
    private JPanel mySelf;
    private static JTextArea yourWord;
    private static JButton send;
    private static JLabel msgArea, lbHead;
    public static JLabel nameto;
    public static String uid = "";
    private static JButton sendFile;
    public static JPanel groupMember;
    //存储列表的信息，方便查找netname
    private static Vector<friendListInfo> findUserNetname;

    //俩天哪窗口的人或者群的netname
    private static String netname = "";

    chatInterface() throws SQLException {
        ClassLoader classLoader = chatInterface.class.getClassLoader();
        URL url = classLoader.getResource("images/chat_head.png");
        URL Icon = classLoader.getResource("images/qe.jpg");
        URL close_fore = classLoader.getResource("images/close_for.png");
        URL close_background = classLoader.getResource("images/close_back.png");
        URL min = classLoader.getResource("images/min_size.png");
        lbHead = new JLabel(new ImageIcon(url));
        close = new JButton(new ImageIcon(close_fore));
        minSize = new JButton(new ImageIcon(min));
        minSize.setBounds(1068, 10, 22, 22);
        minSize.setBorderPainted(false);
        Iconified(minSize);
        close.setForeground(new Color(172, 208, 252));
        close.setBounds(1100, 10, 22, 22);
        close.setBorderPainted(false);
        close(close);
        lbLeft = new JLabel(new ImageIcon(Icon));
        lbLeft.setForeground(Color.blue);
        lbLeft.setBounds(0, 70, 284, 678);
        //选项卡组件
        jTabbedPane = new JTabbedPane();
        jsp1 = new JScrollPane();
        jsp2 = new JScrollPane();
        jsp1.setBackground(new Color(172, 208, 252));
        jsp2.setBackground(new Color(172, 208, 252));
        jTabbedPane.addTab("好友", jsp1);
        jsp1.getViewport().add(new friendList());
        jTabbedPane.addTab("群", jsp2);
        jsp2.getViewport().add(new groupList());

        jTabbedPane.setBounds(0, 0, 284, 678);
        jTabbedPane.setBorder(null);
        jTabbedPane.setBackground(new Color(172, 208, 252));

        //添加头像和用户名信息
        JSONObject object = JSONObject.fromObject(Config.geren_json_data);
        mySelf = new JPanel();
        URL myheadImage = classLoader.getResource("headImage/" + object.getString("img") + ".jpg");
        myface = new JLabel(new ImageIcon(myheadImage));
        myname = new JLabel(object.getString("netName"));
        myinfo = new JLabel(object.getString("info"));

        mySelf.setLayout(null);
        mySelf.setBounds(5, 5, 255, 55);
        myface.setBounds(0, 0, 50, 50);
        myname.setBounds(55, 0, 200, 30);
        myinfo.setBounds(55, 30, 200, 20);
        mySelf.setOpaque(false);

        mySelf.add(myface);
        mySelf.add(myname);
        mySelf.add(myinfo);
        //点击发送文件按钮的时候的动作
        lbHead.add(mySelf);
        lbLeft.add(jTabbedPane);
        lbHead.add(minSize);
        lbHead.add(lbLeft);
        lbHead.add(close);
        Dimension dimension = getcenter();
        chatJFrame.setIconImage(new ImageIcon(Icon).getImage());
        chatJFrame.setLocation((dimension.width - 1031) / 2, (dimension.height - 754) / 2);
        Dragable(chatJFrame);
        chatJFrame.add(lbHead);
        chatJFrame.setUndecorated(true);
        chatJFrame.setSize(1133, 752);
        chatJFrame.setVisible(true);
    }

    private static void close(JButton jbutton) {
        jbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static String findNetName(String string) {
        for (friendListInfo f : findUserNetname) {
            if (f.getUid().equals(string)) {
                return f.getNetname();
            }
        }
        return "not found";
    }

    public static void addGroupChatTo(final String uid, String netname,
                                      final Vector<friendListInfo> vector) {
        findUserNetname = vector;
        if (!chatInterface.uid.equals("")) {
            try {
                lbHead.remove(nameto);
                lbHead.remove(yourWord);
                lbHead.remove(Area);
                lbHead.remove(send);
                lbHead.remove(groupMember);
                lbHead.remove(jLabel1);
                lbHead.remove(jScrollPane);
            } catch (NullPointerException e) {
            }
        }

        Font font = new Font("微软雅黑", Font.PLAIN, 20);
        Area.setBounds(285, 97, 685, 469);
        Area.setFont(font);
        Area.setText(null);
        jScrollPane.setBounds(285, 97, 685, 469);
        lbHead.add(jScrollPane);

        chatInterface.uid = uid;
        nameto = new JLabel(netname);
        chatInterface.netname = netname;
        nameto.setFont(font);
        nameto.setBounds(284, 60, 240, 40);


        yourWord = new JTextArea();
        yourWord.setFont(font);
        yourWord.setBounds(284, 570, 847, 145);

        URL chatb = chatInterface.class.getClassLoader().getResource("images/chatb.png");

        send = new JButton("发送");
        send.setBounds(1068, 712, 60, 40);

        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JSONObject object = JSONObject.fromObject(Config.geren_json_data);
                String myuid = object.getString("uid");
                String text = yourWord.getText();
                msg m = new msg();
                m.setMsg(text);
                groupChatInfo groupChatInfo = new groupChatInfo();
                groupChatInfo.setMyuid(myuid);
                groupChatInfo.setTouid(chatInterface.uid);
                groupChatInfo.setMsg(text);
                groupChatInfo.setType("groupInfo");
                groupChatInfo.setVector(vector);
                addMyMsg(m);
                byte[] bytes = new byte[1024];
                String json = JSONObject.fromObject(groupChatInfo).toString();
                bytes = json.getBytes();
                System.out.println(json);
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
                            InetAddress.getByName(Config.IP), 4003);
                    Config.datagramSocket_client.send(datagramPacket);
                    yourWord.setText("");
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
        groupMember = new JPanel();
        groupMember.setBounds(944, 100, 186, 461);
        groupMember.setBackground(Color.LIGHT_GRAY);
        Color color = new Color(174, 211, 252);
        groupMember.setBorder(BorderFactory.createLineBorder(color));
        groupMember.setLayout(null);
        int i = 0;

        JSONObject object = JSONObject.fromObject(Config.geren_json_data);
        String myuid = object.getString("uid");

        for (final friendListInfo f : vector) {
            ui.groupMember small = new groupMember(f.getImg(), f.getNetname(), f.getUid());
            if (!f.getUid().equals(myuid))
                small.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            chatInterface.addChatTo(f.getUid(), f.getNetname());
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            small.setBounds(0, i * 40 + i++ * 3, 184, 40);
            groupMember.add(small);
        }

        jLabel1.setBounds(964, 60, 186, 40);
        jLabel1.setFont(font);
        lbHead.add(jLabel1);
        lbHead.add(send);
        lbHead.add(yourWord);
        lbHead.add(groupMember);
        lbHead.add(nameto);
        lbHead.updateUI();
    }

    //个人聊天界面
    public static void addChatTo(final String uid, String netname) throws SQLException, NullPointerException {
        Font font = new Font("微软雅黑", Font.PLAIN, 20);
        //JScrollPane jScrollPane = new JScrollPane(Area);
        if (!chatInterface.uid.equals("")) {
            try {
                lbHead.remove(nameto);
                lbHead.remove(yourWord);
                lbHead.remove(Area);
                lbHead.remove(send);
                lbHead.remove(groupMember);
                lbHead.remove(jScrollPane);
                lbHead.remove(jLabel1);
            } catch (NullPointerException e) {

            }
        }
        Area.setText(null);
        Area.setBounds(285, 97, 847, 469);
        Area.setFont(font);
        jScrollPane.setBounds(285, 97, 847, 469);
        lbHead.add(jScrollPane);
        //获得当前窗口的聊天者的uid
        chatInterface.uid = uid;
        nameto = new JLabel(netname);
        chatInterface.netname = netname;
        nameto.setFont(font);
        nameto.setBounds(284, 60, 240, 40);

        yourWord = new JTextArea();
        yourWord.setFont(font);
        yourWord.setBounds(284, 570, 847, 145);

        URL chatb = chatInterface.class.getClassLoader().getResource("images/chatb.png");

        send = new JButton("发送");
        send.setBounds(1068, 712, 60, 40);

        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JSONObject object = JSONObject.fromObject(Config.geren_json_data);
                String myuid = object.getString("uid");
                String text = yourWord.getText();
                msg m = new msg();
                m.setMyuid(myuid);
                m.setTouid(chatInterface.uid);
                m.setMsg(text);
                m.setType("msg");
                addMyMsg(m);
                byte[] bytes = new byte[1024];
                String json = JSONObject.fromObject(m).toString();
                bytes = json.getBytes();
                System.out.println(json);
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
                            InetAddress.getByName(Config.IP), 4003);
                    Config.datagramSocket_client.send(datagramPacket);
                    yourWord.setText("");
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
        sendFile();
        lbHead.add(send);
        lbHead.add(yourWord);
        lbHead.add(nameto);
        lbHead.updateUI();
    }


    private static void sendFile() {
        sendFile = new JButton("发送文件");
        sendFile.setBounds(281, 714, 80, 40);
        sendFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //定义一个发送文件的对象，
                FileContent fileContent = new FileContent();
                ClassLoader classLoader = chatInterface.class.getClassLoader();
                URL Icon = classLoader.getResource("images/qe.jpg");
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("选择文件");
                jFileChooser.showOpenDialog(chatJFrame);
                File file = jFileChooser.getSelectedFile();
                String filename = file.getName().split("\\.")[0];
                msg m = new msg();
                m.setMsg("您发送了文件:" + filename);
                fileContent.setFilename(filename);
                fileContent.setType("sendfile");
                try {
                    String stringBuffer = "";
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file.getAbsolutePath()), "utf-8"));
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        stringBuffer += (line);
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    JSONObject jsonObject = JSONObject.fromObject(Config.geren_json_data);
                    String uid = jsonObject.getString("uid");
                    fileContent.setMyuid(uid);
                    fileContent.setTouid(chatInterface.uid);
                    fileContent.setStringBuffer(stringBuffer);
                    byte[] bytes = new byte[1024];
                    bytes = JSONObject.fromObject(fileContent).toString().getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
                            InetAddress.getByName(Config.IP), 4003);
                    Config.datagramSocket_client.send(datagramPacket);
                    addMyMsg(m);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        lbHead.add(sendFile);
    }

    private static void addMyMsg(msg m) {
        String str = JSONObject.fromObject(Config.geren_json_data).getString("netName") + ": " + new Date().toLocaleString()
                + "\n" + m.getMsg() + "\n";
        Area.setText(Area.getText() + str);
        yourWord.requestFocus();
        Area.updateUI();
    }

    public static void addGroupMsg(msg m) {
        String str = findNetName(m.getMyuid()) + ":\t"
                + new Date().toLocaleString() + "\n" + m.getMsg() + "\n";
        Area.setText(Area.getText() + str);
        Area.updateUI();
        yourWord.requestFocus();
    }

    public static void addOtherMsg(msg m) {
        String str = chatInterface.netname + ":\t"
                + new Date().toLocaleString() + "\n" + m.getMsg() + "\n";
        Area.setText(Area.getText() + str);
        Area.updateUI();
        Area.requestFocus();
        yourWord.requestFocus();
    }

    private void Iconified(JButton jButton) {
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatJFrame.setExtendedState(1);
            }
        });
    }

    private void Dragable(final JFrame frame) {
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                origin.x = e.getX();
                origin.y = e.getY();
            }
        });

        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = frame.getLocation();
                frame.setLocation(point.x + e.getX() - origin.x, point.y + e.getY() - origin.y);
            }
        });
    }

    private Dimension getcenter() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }


}
