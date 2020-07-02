package seungju.fishcaketycoon;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by SeungJu on 2015-11-30.
 */

public class BoosterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booster);

        ImageButton btn_start = (ImageButton) findViewById(R.id.start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu_view = new Intent(BoosterActivity.this, MenuActivity.class);
                startActivity(menu_view);
            }
        });
    }
}
