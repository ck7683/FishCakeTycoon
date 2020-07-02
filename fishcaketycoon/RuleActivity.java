package seungju.fishcaketycoon;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RuleActivity extends Activity {

    int cnt;
    ImageButton img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        cnt = 0;
        img = (ImageButton)findViewById(R.id.ruleActivity_game_rule);
        img.setImageResource(R.drawable.capture_character);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cnt == 0) {
                    img.setImageResource(R.drawable.capture_main);
                } else if (cnt == 1) {
                    img.setImageResource(R.drawable.capture_bake);

                } else if (cnt == 2) {
                    img.setImageResource((R.drawable.capture_store));
                } else if (cnt == 3) {
                    RuleActivity.this.finish();
                }
                cnt++;
            }
        });
    }
}
