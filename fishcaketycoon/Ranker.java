package seungju.fishcaketycoon;

/**
 * Created by LKH on 2015-12-07.
 */
public class Ranker {
    private String nickname;
    private int time;

    public void set(String psz_nickname, int psz_time) {
        nickname = new String(psz_nickname);
        time = psz_time;
    }

    public String getNickname() {
        if(nickname == null) return "";
        return new String(nickname);
    }

    public int getTime() {
        return time;
    }
}
