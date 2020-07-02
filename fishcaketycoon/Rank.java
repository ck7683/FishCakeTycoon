package seungju.fishcaketycoon;

/**
 * Created by LKH on 2015-12-07.
 */
public class Rank {
    private int cnt;
    private Ranker[] ranker;

    public Rank() {
        cnt = 0;
        ranker = new Ranker[6];
        for (int i = 0; i < 6; i++) {
            ranker[i] = new Ranker();
        }
    }

    public int getCnt() {
        return cnt;
    }

    public Ranker getRanker(int rank) {
            if (1 <= rank && rank <= cnt)
            return ranker[rank - 1];
        else return null;
    }

    public void put(String psz_nickname, int psz_time) {
        if (cnt < 6) {
            ranker[cnt].set(psz_nickname, psz_time);
            cnt++;
        } else {
            ranker[cnt - 1].set(psz_nickname, psz_time);
        }
        for (int i = 0; i < cnt - 1; i++) {
            for (int l = i + 1; l < cnt; l++) {
                if (ranker[i].getTime() < ranker[l].getTime()) {
                    Ranker tmp = ranker[i];
                    ranker[i] = ranker[l];
                    ranker[l] = tmp;
                    tmp = null;
                }
            }
        }
    }
}
