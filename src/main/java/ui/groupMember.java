package ui;

import javax.swing.*;
import java.net.URL;
/**
 * Created by hello on 2018/5/9.
 */

public class groupMember extends JPanel{
    private JLabel lbface;
    private JLabel lbname;
    private String lname;
    private String uid;

    groupMember(String img, String name, String uid) {
        lname = name;
        this.uid = uid;
        ClassLoader classLoader = groupMember.class.getClassLoader();
        URL url = classLoader.getResource("headImage/" + img + ".jpg");
        assert url != null;
        lbface = new JLabel(new ImageIcon(url));
        lbname = new JLabel();
        lbname.setText(name);
        lbface.setBounds(0, 0, 60, 40);
        lbname.setBounds(65, 10, 100, 20);
        this.setLayout(null);
        this.add(lbface);
        this.add(lbname);
    }


}
