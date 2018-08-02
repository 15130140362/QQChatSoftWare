package config;

import javax.xml.transform.Result;
import java.sql.*;

/**
 * Created by hello on 2018/6/12.
 */
public class jdbc {

    //我这个由于版本问题写了很长
    //只用写到qq那里就行了，qq是数据库名称，不写的话说明你用的是哪个库statement.executeUpdate("user 数据库名")
    public  static final String url="jdbc:mysql://127.0.0.1:3306/qq?useSSL=true&serverTimezone=UTC&characterEncoding=utf8&useUnicode=true";

    public Connection connection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return  DriverManager.getConnection(url,"root", "15130140362");
    }

    public void insert() throws SQLException, ClassNotFoundException {
        Connection connection=connection();
        Statement statement=connection.createStatement();
        statement.execute("INSERT INTO employee VALUES ('laolice',3000,3000)");
        //查询语句
      /*  ResultSet resultSet=statement.executeQuery("select * from employee");
        while(resultSet.next())
        {
            System.out.print(resultSet.getString("name"));
            System.out.print(resultSet.getString("year"));
            System.out.print(resultSet.getString("money"));
            System.out.println();
        }
        */
      //删除语句
        //statement.execute("delete from employee where name='laoli'");
        statement.close();
        connection.close();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        jdbc jd=new jdbc();
        jd.insert();
    }
}
