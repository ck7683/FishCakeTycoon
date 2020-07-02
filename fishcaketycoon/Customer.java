package seungju.fishcaketycoon;

import java.util.Random;

/**
 * Created by SeungJu on 2015-12-01.
 */
public class Customer {
    int type;
    int ttl; // time to live
    int wait; // wait for time
    int tcnt;

    boolean alive;

    Random random;
    Bread myreq;
    Bread bread[][] = {{new Bread(1, 0, 0, 0), new Bread(1, 0, 0, 0), new Bread(0, 1, 0, 0), new Bread(0, 1, 0, 0),
            new Bread(0, 0, 1, 0), new Bread(0, 0, 1, 0), new Bread(0, 0, 0, 1), new Bread(0, 0, 0, 1),
            new Bread(1, 1, 0, 0), new Bread(1, 0, 1, 0), new Bread(1, 0, 0, 1),
            new Bread(0, 1, 1, 0), new Bread(0, 1, 0, 1),
            new Bread(0, 0, 1, 1)},
            {new Bread(1, 1, 0, 0), new Bread(1, 0, 1, 0), new Bread(1, 0, 0, 1), new Bread(0, 1, 1, 0), new Bread(0, 1, 0, 1), new Bread(0, 0, 1, 1),
                    new Bread(1, 1, 0, 0), new Bread(1, 0, 1, 0), new Bread(1, 0, 0, 1), new Bread(0, 1, 1, 0), new Bread(0, 1, 0, 1), new Bread(0, 0, 1, 1),
                    new Bread(1, 1, 0, 0), new Bread(1, 0, 1, 0), new Bread(1, 0, 0, 1), new Bread(0, 1, 1, 0), new Bread(0, 1, 0, 1), new Bread(0, 0, 1, 1),
                    new Bread(1, 1, 1, 0), new Bread(1, 1, 0, 1), new Bread(1, 0, 1, 1), new Bread(0, 1, 1, 1),
                    new Bread(1, 1, 1, 0), new Bread(1, 1, 0, 1), new Bread(1, 0, 1, 1), new Bread(0, 1, 1, 1),
                    new Bread(1, 1, 1, 1)
            }
    };

    public Customer() {
        random = new Random();
        type = ttl = wait = 0;
        alive = false;
        myreq = new Bread();
    }

    public Customer(int level) {
        System.out.println("customer level " + level);

        random = new Random();
        type = random.nextInt(3) + 1;
        wait = random.nextInt(100) + 50;
        makeRequirement(level);
        ttl = 150 + (random.nextInt(105) + 1);
        alive = true;
    }

    public Customer(int _type, int _ttl, int _wp, int _ws, int _gp, int _gs) {
        type = _type;
        ttl = _ttl;
        myreq = new Bread(_wp, _ws, _gp, _gs);
        alive = true;
    }

    private void makeRequirement(int level) {
        // level 0 : easy
        // level 1 : nightmare
        // level 2 : korean

        int make = (level > 0 ? 1 : 0);

        int mod = bread[make].length;
        int select = random.nextInt(mod);

        Bread req = bread[make][select];

        int cnt = random.nextInt(3) + 1;
        if (level > 0) cnt += random.nextInt(level * 3);

        tcnt = cnt;

        int give;

        if (req.flour_paat > 0) {
            give = 0;
            if (cnt > 0) give = random.nextInt(cnt) / 2 + 1;
            req.flour_paat = give;
            cnt -= give;
        }

        if (req.flour_shu > 0) {
            give = 0;
            if (cnt > 0) give = random.nextInt(cnt) / 2 + 1;
            req.flour_shu = give;
            cnt -= give;
        }

        if (req.gflour_paat > 0) {
            give = 0;
            if (cnt > 0) give = random.nextInt(cnt) / 2 + 1;
            req.gflour_paat = give;
            cnt -= give;
        }

        if (req.gflour_shu > 0) {
            give = 0;
            if (cnt > 0) give = random.nextInt(cnt) / 2 + 1;
            req.gflour_shu = give;
            cnt -= give;
        }

        if (cnt > 0) {
            // remain bread. so assign !
            if (req.gflour_shu > 0) {
                req.gflour_shu += cnt;
            } else if (req.gflour_paat > 0) {
                req.gflour_paat += cnt;
            } else if (req.flour_shu > 0) {
                req.flour_shu += cnt;
            } else if (req.flour_paat > 0) {
                req.flour_paat += cnt;
            }
        }

        myreq = req;

    }
}
