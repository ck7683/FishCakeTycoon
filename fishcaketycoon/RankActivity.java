package seungju.fishcaketycoon;
/**
 * Created by LKH on 2015-12-07.
 */

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RankActivity extends Activity {

    Rank rank;

    DBAdapter adap;

    TextView[] rank_nick_text;
    TextView[] rank_time_text;

    int[] rank_nick_id = {R.id.rank_nick1, R.id.rank_nick2, R.id.rank_nick3, R.id.rank_nick4, R.id.rank_nick5};
    int[] rank_time_id = {R.id.rank_time1, R.id.rank_time2, R.id.rank_time3, R.id.rank_time4, R.id.rank_time5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ImageButton btn_back = (ImageButton) findViewById(R.id.rank_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RankActivity.this.finish();
            }
        });

        adap = new DBAdapter(getApplicationContext());
        rank = new Rank();
        rank_nick_text = new TextView[5];
        rank_time_text = new TextView[5];
        for (int i = 0; i < 5; i++) {
            rank_nick_text[i] = (TextView) findViewById(rank_nick_id[i]);
            rank_time_text[i] = (TextView) findViewById(rank_time_id[i]);
        }

        getRankInfo();

        for (int i = 0; i < 5; i++) {
            Ranker ranker = rank.getRanker(i + 1);
            if (ranker == null) break;
            if (ranker.getTime() > 0) {
                rank_nick_text[i].setText(ranker.getNickname());
                rank_time_text[i].setText(Integer.toString(ranker.getTime() / 10));
            }
        }
    }

    void getRankInfo() {

        Cursor cursor = adap.get_rank(Const.pref_play_time);

        if(cursor.getCount() == 0){ // there is no ranking data
            return ;
        }

        cursor.moveToFirst();

        int selected = 0;

        do{
            String nick = cursor.getString(cursor.getColumnIndex(adap.getKey(Const.pref_nickName)));
            int time = cursor.getInt(cursor.getColumnIndex(adap.getKey(Const.pref_play_time)));
            rank.put(nick, time);
            selected++;
            if(selected == 5)break;
        } while( cursor.moveToNext());

    }

}


