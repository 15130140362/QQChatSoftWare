package model;

import java.util.Vector;

/**
 * Created by hello on 2018/5/9.
 */
public class groupChatInfo{
    private String myuid;
    private String touid;
    private String msg;
    private String type;
    private Vector<friendListInfo> vector;

    public Vector<friendListInfo> getVector() {
        return vector;
    }

    public void setVector(Vector<friendListInfo> vector) {
        this.vector = vector;
    }

    public String getMyuid() {
        return myuid;
    }

    public void setMyuid(String myuid) {
        this.myuid = myuid;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

