package seungju.fishcaketycoon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by SeungJu on 2015-11-30.
 */
public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton btn_select = (ImageButton) findViewById(R.id.char_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent character_view = new Intent(MenuActivity.this, CharacterActivity.class);
                startActivity(character_view);
                //Intent temp_view = new Intent(MenuActivity.this, MainActivity.class);
                //startActivity(temp_view);

            }
        });

        ImageButton btn_rule = (ImageButton) findViewById(R.id.game_rule);
        btn_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("2");
                Intent rule_view = new Intent(MenuActivity.this, RuleActivity.class);
                startActivity(rule_view);
            }
        });

        ImageButton btn_rank = (ImageButton) findViewById(R.id.game_rank);
        btn_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rank_view = new Intent(MenuActivity.this, RankActivity.class);
                startActivity(rank_view);
            }
        });

        ImageButton btn_exit = (ImageButton) findViewById(R.id.game_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.this.finish();
            }
        });

        SharedPreferences mine = getSharedPreferences("MyInfo", MODE_PRIVATE);
        // MODE_PRIVATE : only my app
        SharedPreferences.Editor edt = mine.edit();

        edt.putInt(Const.pref_slotNum, 0);
// default

        // 0000000 : default, (X)[LM]ABCD : white + paat (A), white + shu (B), green + paat (C), green + shu (D)
        // X : customer X, TTL(LM)

        edt.commit();
    }
}
