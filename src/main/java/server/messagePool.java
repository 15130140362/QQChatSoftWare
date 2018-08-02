package server;

import model.FileContent;
import model.msg;
import net.sf.json.JSONObject;
import ui.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 消息池,会把所有的消息接收到池里面进行存储
 * Created by hello on 2018/4/29.
 */
public class messagePool {
    private messagePool() {
    }

    private static messagePool messagePool = new messagePool();

    public static messagePool getMessagePool() {
        return messagePool;
    }

    public static HashMap<String, LinkedList<msg>> hashMap = new HashMap<String, LinkedList<msg>>();


    //不管谁的消息，都存储在池里面
    public void addMessage(String json) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(json);
        msg m = new msg();
        FileContent fileContent = new FileContent();
        String type = jsonObject.getString("type");
        if (type.equals("sendfile")) {
            String touid = jsonObject.getString("touid");
            String myuid = jsonObject.getString("myuid");
            String filename = jsonObject.getString("filename");
            String stringBuffer = jsonObject.getString("stringBuffer");
            fileContent.setMyuid(myuid);
            fileContent.setTouid(touid);
            fileContent.setStringBuffer(stringBuffer);
            fileContent.setFilename(filename);

            File receive = new File("f:/");
            StringBuilder path = new StringBuilder("f:/" + filename);
            File newfile = new File(path.toString() + ".txt");
            int i = 1;

            while (newfile.exists()) {
                String s = path.toString().split("\\(")[0];
                path.delete(0, path.length());
                path.append(s);
                path.append("(").append(i).append(")");
                newfile = new File(path.toString() + ".txt");
                i++;
            }
            path.append(".txt");
            newfile = new File(path.toString());
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newfile), "utf-8"));
                bufferedWriter.write(stringBuffer.toString());
                bufferedWriter.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            javax.swing.JOptionPane.showMessageDialog(null, "文件成功保存至：" + path);
            m.setMsg("您收到一个文件,存储于：" + path);

            if (chatInterface.uid.equals(myuid)) {
                chatInterface.addOtherMsg(m);
            } else {
                friendFace friendFace = friendList.list.get(myuid);
                friendFace.addMessage(m);
            }
        } else if (type.equals("msg")) {
            String touid = jsonObject.getString("touid");
            String myuid = jsonObject.getString("myuid");
            String msg = jsonObject.getString("msg");
            String code = jsonObject.getString("code");
            //接受好的消息包装在对象列表中
            m.setTouid(touid);
            m.setMyuid(myuid);
            m.setMsg(msg);
            m.setType(type);
            m.setCode(code);
            if (chatInterface.uid.equals(myuid)) {
                chatInterface.addOtherMsg(m);
            } else {
                friendFace friendFace = friendList.list.get(myuid);
                friendFace.addMessage(m);
            }
        } else if (type.equals("groupInfo")) {
            String touid = jsonObject.getString("touid");
            String myuid = jsonObject.getString("myuid");
            String msg = jsonObject.getString("msg");
            m.setType(type);
            m.setMsg(msg);
            m.setTouid(touid);
            m.setMyuid(myuid);
            if(chatInterface.uid.equals(touid)){
                chatInterface.addGroupMsg(m);
            } else {
                groupFace groupFace =groupList.list.get(touid);
                groupFace.addMessage(m);
            }
        }

     /*   LinkedList<msg> list = hashMap.get(myuid);
        if (list == null) {
            list = new LinkedList<model.msg>();
        }
        list.add(m);
        hashMap.put(myuid, list);*/
    }
}
