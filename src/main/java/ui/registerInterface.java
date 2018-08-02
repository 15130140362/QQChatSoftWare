package ui;


import config.Config;
import net.sf.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

/**
 * Created by hello on 2018/4/21.
 */
public class registerInterface {
    private JLabel register1;
    private static JFrame registerFrame = new JFrame();
    private Point origin = new Point();
    private JButton close, minSize, identify, finish;
    private JLabel text, emailText, phoneOrEmailNumber, passwordText, comfirmPasswordText, identifyText;
    private JPasswordField passwordField, confirmPassword;
    private JTextField emailOrPhoneNumberText, confirmIdentify;

    registerInterface() {
        ClassLoader classLoader = registerInterface.class.getClassLoader();
        URL register4 = classLoader.getResource("images/register_frame.png");
        URL Icon = classLoader.getResource("images/qe.jpg");
        URL register_close = classLoader.getResource("images/registerClose.png");
        URL register_min = classLoader.getResource("images/registermin.png");
        register1 = new JLabel(new ImageIcon(register4));
        close = new JButton(new ImageIcon(register_close));
        minSize = new JButton(new ImageIcon(register_min));
        Font registerPerson = new Font("微软雅黑", Font.PLAIN, 17);
        text = new JLabel("注册用户");
        text.setBounds(20, 70, 80, 35);
        text.setFont(registerPerson);

        phoneOrEmailNumber = new JLabel("email:");

        phoneOrEmailNumber.setBounds(125, 110, 80, 35);
        phoneOrEmailNumber.setFont(registerPerson);
        emailOrPhoneNumberText = new JTextField();
        emailOrPhoneNumberText.setBounds(205, 110, 220, 35);
        emailOrPhoneNumberText.setFont(registerPerson);
        emailText = new JLabel("email:");
        emailText.setFont(registerPerson);
        emailText.setBounds(130, 140, 80, 20);


        identify = new JButton("获取验证码");
        identify.setBounds(275, 145, 150, 20);

        identifyText = new JLabel("验证码:");
        identifyText.setBounds(125, 175, 80, 35);
        identifyText.setFont(registerPerson);

        confirmIdentify = new JTextField();
        confirmIdentify.setBounds(205, 175, 220, 35);
        confirmIdentify.setFont(registerPerson);


        passwordText = new JLabel("密码:");
        passwordText.setBounds(125, 215, 80, 35);
        passwordText.setFont(registerPerson);


        passwordField = new JPasswordField();
        passwordField.setFont(registerPerson);
        passwordField.setBounds(205, 215, 220, 35);

        comfirmPasswordText = new JLabel("确认密码:");
        comfirmPasswordText.setBounds(125, 255, 80, 35);
        comfirmPasswordText.setFont(registerPerson);
        confirmPassword = new JPasswordField();
        confirmPassword.setBounds(205, 255, 220, 35);


        finish = new JButton("完成");
        finish.setBounds(345, 295, 80, 30);


        close.setBounds(505, 4, 22, 21);
        close.setBorderPainted(false);
        close(close);
        minSize.setBorderPainted(false);
        minSize.setBounds(470, 1, 27, 27);
        Iconified(minSize);

        register1.add(finish);
        register1.add(confirmPassword);
        register1.add(comfirmPasswordText);
        // register1.add(emailText);
        register1.add(passwordText);
        register1.add(passwordField);
        register1.add(identifyText);
        register1.add(confirmIdentify);
        register1.add(identify);
        register1.add(phoneOrEmailNumber);
        register1.add(emailOrPhoneNumberText);
        register1.add(text);
        register1.add(minSize);
        register1.add(close);
        Dimension dimension = getcenter();
        Dragable(registerFrame);
        registerFrame.setLocation((dimension.width - 531) / 2, (dimension.height - 347) / 2);
        registerFrame.add(register1);
        registerFrame.setIconImage(new ImageIcon(Icon).getImage());
        registerFrame.setUndecorated(true);
        registerFrame.setVisible(true);
        registerFrame.setSize(531, 347);

        identify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (emailOrPhoneNumberText.getText().trim().length() == 0) {
                        javax.swing.JOptionPane.showMessageDialog(null, "用户名不能为空");
                    } else {
                        Socket socket = new Socket(Config.IP, Config.REGISTER_PORT);
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();

                        outputStream.write(("{\"type\":\"code\",\"username\":\"" + emailOrPhoneNumberText.getText() + "\"}").getBytes());
                        outputStream.flush();

                        //接受
                        String str = getString(inputStream);
                        //System.out.println(str);
                        JSONObject json = JSONObject.fromObject(str);

                        int key = json.getInt("state");
                        if (key == 0) {
                            javax.swing.JOptionPane.showMessageDialog(null, "发送成功!");
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(null, "发送失败!");
                        }

                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        finish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (emailOrPhoneNumberText.getText().trim().length() == 0) {
                        javax.swing.JOptionPane.showMessageDialog(null, "用户名不能为空");
                        return;
                    }
                    if (passwordField.getText().trim().length() == 0) {
                        javax.swing.JOptionPane.showMessageDialog(null, "密码不能为空");
                        return;
                    }
                    if (confirmPassword.getText().trim().length() == 0) {
                        javax.swing.JOptionPane.showMessageDialog(null, "确认密码不能为空");
                        return;
                    }
                    if (!confirmPassword.getText().trim().equals(passwordField.getText().trim())) {
                        System.out.println(confirmPassword.getText().trim()+" "+passwordField.getText().trim());
                        javax.swing.JOptionPane.showMessageDialog(null, "密码和确认密码必须相等!");
                        return;
                    }
                    if (confirmIdentify.getText().trim().length() == 0) {
                        javax.swing.JOptionPane.showMessageDialog(null, "验证码不能为空");
                        return;
                    }

                    {
                        Socket socket = new Socket(Config.IP, Config.REGISTER_PORT);
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(("{\"type\":\"reg\",\"username\":\"" + emailOrPhoneNumberText.getText() + "\"" +
                                ",\"password1\":\"" + passwordField.getText() + "\"" +
                                ",\"password2\":\"" + confirmPassword.getText() + "\"" +
                                ",\"code\":\"" + confirmIdentify.getText() + "\"}").getBytes());
                        outputStream.flush();

                        //接受响应
                        String str = getString(inputStream);
                        //System.out.println(str);
                        JSONObject json = JSONObject.fromObject(str);

                        int key = json.getInt("state");
                        if (key == 0) {
                            javax.swing.JOptionPane.showMessageDialog(null, "注册成功，请登录!");
                        } else if (key == 1) {
                            javax.swing.JOptionPane.showMessageDialog(null, "用户名已存在!");
                        } else if (key == 2) {
                            javax.swing.JOptionPane.showMessageDialog(null, "验证码错误，请重新获得。");
                        } else if (key == 3) {
                            javax.swing.JOptionPane.showMessageDialog(null, "未知错误！");
                        }
                        emailOrPhoneNumberText.setText("");
                        passwordField.setText("");
                        confirmPassword.setText("");
                        confirmIdentify.setText("");

                        inputStream.close();
                        outputStream.close();
                        socket.close();
                        registerFrame.dispose();
                        new login();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public String getString(InputStream in) throws IOException {
        byte[] bytes = new byte[1024 * 10];
        int len = in.read(bytes);
        return new String(bytes, 0, len);
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

    public static void main(String[] args) {
        registerInterface registerInterface = new registerInterface();
    }

    private void Iconified(JButton jButton) {
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerFrame.setExtendedState(1);
            }
        });
    }

    private void close(JButton jbutton) {
        jbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

}

