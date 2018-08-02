package model;

import db.DBManager;
import exception.PasswordNotFoundException;
import exception.StateException;
import exception.UsernameException;
import exception.UsernameNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by hello on 2018/4/21.
 */
public class UserService {


    public void registerUser(String username, String password) throws UsernameException, SQLException {
        Connection connection = null;
        String sql = "select * from users where email=?";
        connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            throw new UsernameException();
        }

        preparedStatement = connection.prepareStatement("INSERT into users(uid,email,password,createtime)" +
                "values(?,?,?,SYSDATE())");

        preparedStatement.setString(1, System.currentTimeMillis()
                + "R" + (int) (Math.random() * 10000));

        preparedStatement.setString(2, username);
        preparedStatement.setString(3, password);
        if (0 >= preparedStatement.executeUpdate()) {
            throw new UsernameException();
        }

    }

    /*获得好友列表信息
    * 返回好友列表信息
    * */


    public Vector<groupInfo> getMyGroupList(String uid) throws SQLException {
        Connection connection = DBManager.getConnection();
        String sql = "SELECT groupinfo.groupid,groupinfo.groupname,groupinfo.groupimage \n" +
                "from goup INNER JOIN groupinfo ON groupinfo.groupid=goup.groupid \n" +
                "\tand goup.groupmembernumber=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uid);
        ResultSet resultSet = preparedStatement.executeQuery();
        Vector<groupInfo> vector = new Vector<groupInfo>();
        while (resultSet.next()) {
            groupInfo group=new groupInfo();
            group.setGroupuid(resultSet.getString(1));
            group.setGroupname(resultSet.getString(2));
            group.setGroupimage(resultSet.getString(3));
            vector.add(group);
        }
        return vector;
    }

    public Vector<friendListInfo> getMemberInfo(String groupUid) throws SQLException {
        Connection connection = DBManager.getConnection();
        String sql="SELECT users.uid,users.netname,users.img FROM goup,users " +
                "where goup.groupid=? AND users.uid=goup.groupmembernumber";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, groupUid);
        ResultSet resultSet = preparedStatement.executeQuery();
        Vector<friendListInfo> vector = new Vector();
        while(resultSet.next()){
            friendListInfo friendListInfo = new friendListInfo();
            friendListInfo.setUid(resultSet.getString(1));
            friendListInfo.setNetname(resultSet.getString(2));
            friendListInfo.setImg(resultSet.getString(3));
            vector.add(friendListInfo);
        }
        return vector;
    }

    public Vector<friendListInfo> getmyFriendList(String uid) throws SQLException {
        Connection connection = DBManager.getConnection();
        String sql = "SELECT users.uid,users.img,users.netname,users.info FROM hy " +
                "INNER JOIN users  ON users.uid=hy.hyuid AND hy.uid=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uid);
        ResultSet resultSet = preparedStatement.executeQuery();
        Vector<friendListInfo> vector = new Vector();
        while (resultSet.next()) {
            friendListInfo friendListInfo = new friendListInfo();
            friendListInfo.setUid(resultSet.getString(1));
            friendListInfo.setImg(resultSet.getString(2));
            friendListInfo.setInfo(resultSet.getString(4));
            friendListInfo.setNetname(resultSet.getString(3));
            vector.add(friendListInfo);
        }
        return vector;
    }


    //个人资料查询
    public personInfo getUserInfo(String uid) throws SQLException {
        Connection connection = DBManager.getConnection();
        String sql = "SELECT * FROM users WHERE uid=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uid);
        ResultSet resultSet = preparedStatement.executeQuery();
        personInfo personInfo = new personInfo();
        if (resultSet.next()) {
            personInfo.setUid(resultSet.getString("uid"));
            personInfo.setPhonenumber(resultSet.getString("phonenumber"));
            personInfo.setBack(resultSet.getString("back"));
            personInfo.setEmail(resultSet.getString("email"));
            personInfo.setName(resultSet.getString("name"));
            personInfo.setSex(resultSet.getString("sex"));
            personInfo.setYy(resultSet.getInt("yy"));
            personInfo.setMm(resultSet.getInt("mm"));
            personInfo.setDd(resultSet.getInt("dd"));
            personInfo.setNetName(resultSet.getString("netname"));
            personInfo.setImg(resultSet.getString("img"));
            personInfo.setInfo(resultSet.getString("info"));
        }
        return personInfo;
    }

    public String loginForEmail(String email, String password) throws StateException, PasswordNotFoundException, SQLException, UsernameNotFoundException {
        String sql = "select * from users where email=?";
        return login(email, password, sql);
    }


    public String loginForPhone(String phone, String password) throws
            UsernameNotFoundException, PasswordNotFoundException, StateException, SQLException {
        String sql = "select * from users where phonenumber=?";
        return login(phone, password, sql);
    }


    private String login(String key, String password, String sql) throws
            UsernameNotFoundException, PasswordNotFoundException, StateException, SQLException {
        Connection connection = null;
        try {
            connection = DBManager.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getInt("state") == 0) {
                    //密码是否一致
                    if (resultSet.getString("password").equals(password)) {
                        return resultSet.getString(1);
                    } else {
                        throw new PasswordNotFoundException();
                    }
                } else {
                    throw new StateException();
                }
            } else {
                throw new UsernameNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("链接失败");
        } finally {
            // connection.close();
        }
        return "";
    }

    public static void main(String[] args) throws StateException, PasswordNotFoundException, SQLException, UsernameNotFoundException {
        new UserService().loginForEmail("1521949504@qq.com", "1");
    }
}
