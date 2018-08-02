package model;

/**
 * Created by hello on 2018/5/8.
 */
public class FileContent {
    private String type;
    private String myuid;
    private String touid;

    @Override
    public String toString() {
        return "FileContent{" +
                "type='" + type + '\'' +
                ", myuid='" + myuid + '\'' +
                ", touid='" + touid + '\'' +
                ", filename='" + filename + '\'' +
                ", stringBuffer='" + stringBuffer + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getMyuid() {
        return myuid;
    }

    public void setMyuid(String myuid) {
        this.myuid = myuid;
    }

    private String filename;
    private String stringBuffer;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStringBuffer() {
        return stringBuffer;
    }

    public void setStringBuffer(String stringBuffer) {
        this.stringBuffer = stringBuffer;
    }
}
