package ui;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by hello on 2018/4/21.
 */
public class entrance {
    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        login login = new login();
    }
}
