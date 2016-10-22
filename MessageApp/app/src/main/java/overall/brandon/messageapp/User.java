package overall.brandon.messageapp;

/**
 * Created by Brandon on 10/21/2016.
 */

public class User {

    String androidId;

    String alias;

    String ip;

    int port;

    public User(String androidId, String alias, String ip, int port) {
        this.androidId = androidId;
        this.alias = alias;
        this.ip = ip;
        this.port = port;
    }

    public User(String androidId) {
        this.androidId = androidId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIp() { return ip;
    }
    public void setIp(String ip) {this.ip = ip;}

    public int getPort() {return port; }

    public void setPort(int port) {this.port = port;}

}
