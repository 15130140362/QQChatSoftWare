package db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;

/**
 * Created by hello on 2018/4/18.
 */

public class DBManager {
    public  Connection connection;
    public  static final String url="jdbc:mysql://127.0.0.1:3306/qq?useSSL=true&serverTimezone=UTC&characterEncoding=utf8&useUnicode=true";
    public static DataSource dataSource=null;
    public static final String DRIVER_NAME="com.mysql.cj.jdbc.Driver";
    static {
        ComboPooledDataSource pool=new ComboPooledDataSource();
        try {
            pool.setDriverClass(DRIVER_NAME);
            pool.setUser("root");
            pool.setPassword("15130140362");
            pool.setJdbcUrl(url);
            pool.setMaxPoolSize(30);
            pool.setMinPoolSize(3);
            dataSource=pool;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            System.out.println("数据库加载失败");
        }
    }

    //大家通过此方法获取connection连接对象
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


/*
    public DBManager() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection= DriverManager.getConnection(url,"root",
                "15130140362");
        System.out.println("*****数据库连接成功*****");
    }*/

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

    }

}
