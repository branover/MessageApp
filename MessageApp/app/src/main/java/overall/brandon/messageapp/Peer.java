package overall.brandon.messageapp;

/**
 * Created by Brandon on 10/23/2016.
 */

public class Peer extends User {

    Integer onlinetime = 0;

    public Peer(String androidId, String alias, String ip, int port, String ipv6, Integer onlinetime) {
        super(androidId,alias,ip,port,ipv6);
        this.onlinetime = onlinetime;
    }

    public Integer getOnlineTime() {
        return onlinetime;
    }

    public void setOnlineTime(Integer onlineTime) {
        this.onlinetime = onlineTime;
    }
}
