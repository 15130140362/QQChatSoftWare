package ui;
import config.Config;
import net.sf.json.JSONObject;
import server.clientService;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;


/**
 * Created by hello on 2018/4/18.
 */
public class login implements ActionListener {
    JComboBox qqCodeBox;//下拉框
    JPasswordField pwd;
    JLabel lbFace, lbHead, findpwd, register;
    JButton reg, login, close,minSize;
    JCheckBox memory, autoLogin;
    static   JFrame loginFrame=new JFrame();
    public static Point origin = new Point();

    public login() throws IOException {
        //加载图片
        ClassLoader classLoader = login.class.getClassLoader();
        URL resource = classLoader.getResource("images/qe.jpg");
        URL resoure2 = classLoader.getResource("images/newLogin.jpg");
        URL resource3 = classLoader.getResource("images/exit.png");
        URL resource4=classLoader.getResource("images/min.png");

        ImageIcon imageIcon = new ImageIcon(resource);
        ImageIcon imageIcon1 = new ImageIcon(resoure2);
        ImageIcon imageicon3 = new ImageIcon(resource3);
        lbHead = new JLabel(imageIcon1);
        //下拉菜单
        String qqCodes[] = readQQCode();
        if (qqCodes != null) {
            qqCodeBox = new JComboBox(qqCodes);
            qqCodeBox.setSelectedIndex(0);
        } else {
            qqCodeBox = new JComboBox();
        }
        qqCodeBox.setBackground(Color.WHITE);
        pwd = new JPasswordField();
        pwd.setBounds(120, 230, 182, 30);

        login = new JButton("登陆");
        login.setBounds(120, 289, 182, 35);
        Color color = new Color(0, 222, 255);
        login.setBackground(color);


        Font font = new Font("微软雅黑", Font.PLAIN, 12);
        findpwd = new JLabel("找回密码");
        register = new JLabel("注册用户");
        memory = new JCheckBox("记住密码");
        autoLogin = new JCheckBox("自动登录");


        // memory.setForeground(color);
        memory.setFont(font);
        autoLogin.setFont(font);
        autoLogin.setBackground(Color.WHITE);
        memory.setBackground(Color.WHITE);
        // autoLogin.setForeground(color);
        memory.setBounds(130, 266, 80, 17);
        autoLogin.setBounds(210, 266, 80, 17);
        findpwd.setBounds(320, 230, 182, 30);
        findpwd.setForeground(color);
        findpwd.setFont(font);

        register.setBounds(320, 190, 182, 30);
        register.setForeground(color);
        register.setFont(font);

        lbFace = new JLabel(new ImageIcon(resource));
        lbFace.setBounds(30, 190, 60, 60);
        lbHead.add(lbFace);

        qqCodeBox.setBounds(120, 190, 182, 35);
        qqCodeBox.setEditable(true);

        //右上角的按钮功能键
        close = new JButton(imageicon3);
        close.setBounds(405, 2, 20, 20);
        //  close.setBackground(color);
        close.setBorderPainted(false);
        close.setMargin(null);
        minSize=new JButton(new ImageIcon(resource4));
        minSize.setBounds(375,1,20,20);
        minSize.setMargin(null);
        minSize.setBorderPainted(false);
        lbHead.add(minSize);
        lbHead.add(close);
        lbHead.add(memory);
        lbHead.add(autoLogin);
        lbHead.add(qqCodeBox);
        lbHead.add(pwd);
        lbHead.add(login);
        lbHead.add(register);
        lbHead.add(findpwd);
        loginFrame.setUndecorated(true);
        loginFrame.add(lbHead);
        loginFrame.setIconImage(imageIcon.getImage());
        loginFrame.setVisible(true);
        loginFrame.setSize(428, 327);
        Dragable(loginFrame);
        loginFrame.setResizable(false);
        Dimension dimension=getcenter();
        loginFrame.setLocation((dimension.width-428)/2, (dimension.height-327)/2);
        login.addActionListener(this);
        register.addMouseListener(new MouseClicked());
        register.addMouseMotionListener(new MouseMoveOn());
        findpwd.addMouseListener(new MouseClicked());
        findpwd.addMouseMotionListener(new MouseMoveOn());

        //推出
        close.addActionListener(this);
        minSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.setExtendedState(1);
            }
        });
    }

    private Dimension getcenter(){
        return   Toolkit.getDefaultToolkit().getScreenSize();
    }

    private void Dragable(final JFrame frame){
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

    public String[] readQQCode() throws IOException {
        String[] qqcodes = null;
        ClassLoader classLoader = login.class.getClassLoader();
        //读取maven项目的文件
        URL url = login.class.getClassLoader().getResource("file/account.dat");
        File file = new File(url.getFile());

        if (!file.exists()) {
            System.out.println("文件不存在");
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str = "";
        String str2 = br.readLine();
        while (str2 != null) {
            str += str2;
            str2 = br.readLine();
        }
        br.close();
        if (str == null) {
            qqcodes = null;
        } else {
            qqcodes = str.split(",");
        }

        return qqcodes;
    }
    /*
    * 1密码错误
    * 2找不到账户
    * 3账户锁定
    * 0登陆成功*/
    public void actionPerformed(ActionEvent e) {
        String input = e.getActionCommand();
        if (input.equals("登陆")) {
            String username = qqCodeBox.getSelectedItem().toString().trim();
            //System.out.println(qqCode);
            String password = pwd.getText().trim();
            if(username.equals("")||password.equals(""))
            {
                javax.swing.JOptionPane.showMessageDialog(null,"用户名和密码必须填写");
                return;
            }

            //存储参数
            Config.username=username;
            Config.password=password;

            try {
                JSONObject jsonObject= clientService.getClientService().login();
                if(jsonObject.getInt("state")==0)
                {
                    javax.swing.JOptionPane.showMessageDialog(null,"登陆成功");
                }else{
                    javax.swing.JOptionPane.showMessageDialog(null,jsonObject.getString("msg"));
                }
            } catch (IOException e1) {
                javax.swing.JOptionPane.showMessageDialog(null,"连接失败");
                e1.printStackTrace();
                return;
            }
            loginFrame.dispose();
            try {
                new chatInterface();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else if (input.equals("")) {
            System.exit(0);
        }
    }
}

class MouseClicked extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        login.loginFrame.dispose();
        new registerInterface();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel source = (JLabel) e.getSource();
        Color color = new Color(0, 222, 255);
        source.setForeground(color);
    }
}

class MouseMoveOn extends MouseMotionAdapter {
    @Override
    public void mouseMoved(MouseEvent e) {
        //super.mouseMoved(e);
        JLabel source = (JLabel) e.getSource();
        source.setForeground(Color.black);
    }

}