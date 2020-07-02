package seungju.fishcaketycoon;
/**
 * Created by LKH on 2015-12-04.
 * and edited by LKH & SeungJu
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class CharacterActivity extends Activity {

    SharedPreferences pref;
    SharedPreferences.Editor edit;

    DBAdapter adap;

    GameCharacter[] user;

    ImageButton[] slotImageBtn;
    ImageButton[] deleteBtn;
    ImageButton exitImageBtn;

    ImageView[] level_img;
    ImageView[] heart_img;
    ImageView[] coin_img;
    ImageView[] time_img;

    EditText[] ediText;

    TextView[] textLevel;
    TextView[] textHeart;
    TextView[] textCoin;
    TextView[] textTime;

    int[] slot_id = {R.id.character_slot1, R.id.character_slot2, R.id.character_slot3};
    int[] delete_id = {R.id.character_delete1, R.id.character_delete2, R.id.character_delete3};
    int[] slot_edit_id = {R.id.character_slot1_nick, R.id.character_slot2_nick, R.id.character_slot3_nick};
    int[] text_level_id = {R.id.character_slot1_level, R.id.character_slot2_level, R.id.character_slot3_level};
    int[] text_heart_id = {R.id.character_slot1_heart, R.id.character_slot2_heart, R.id.character_slot3_heart};
    int[] text_coin_id = {R.id.character_slot1_coin, R.id.character_slot2_coin, R.id.character_slot3_coin};
    int[] text_time_id = {R.id.character_slot1_time, R.id.character_slot2_time, R.id.character_slot3_time};

    int[] img_level_id = {R.id.character_slot1_level_img, R.id.character_slot2_level_img, R.id.character_slot3_level_img};
    int[] img_heart_id = {R.id.character_slot1_heart_img, R.id.character_slot2_heart_img, R.id.character_slot3_heart_img};
    int[] img_coin_id = {R.id.character_slot1_coin_img, R.id.character_slot2_coin_img, R.id.character_slot3_coin_img};
    int[] img_time_id = {R.id.character_slot1_time_img, R.id.character_slot2_time_img, R.id.character_slot3_time_img};

    int focus;

    boolean PAUSE = false;
    boolean clickLong;

    HashMap<Integer, Integer> slot_hash = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> delete_hash = new HashMap<Integer, Integer>();


    FrameLayout.LayoutParams[] lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        System.out.println("character selected");

        lp = new FrameLayout.LayoutParams[3];
        pref = getSharedPreferences(Const.pref_my, MODE_PRIVATE);
        edit = pref.edit();

        textCoin = new TextView[3];
        textHeart = new TextView[3];
        textLevel = new TextView[3];
        textTime = new TextView[3];

        level_img = new ImageView[3];
        heart_img = new ImageView[3];
        coin_img = new ImageView[3];
        time_img = new ImageView[3];

        exitImageBtn = (ImageButton) findViewById(R.id.character_exit);
        adap = new DBAdapter(getApplicationContext());
        ediText = new EditText[3];
        focus = 0;
        clickLong = false;

        user = new GameCharacter[3];
        deleteBtn = new ImageButton[3];

        for (int i = 0; i < 3; i++) {
            textLevel[i] = (TextView) findViewById(text_level_id[i]);
            textHeart[i] = (TextView) findViewById(text_heart_id[i]);
            textCoin[i] = (TextView) findViewById(text_coin_id[i]);
            textTime[i] = (TextView) findViewById(text_time_id[i]);

            level_img[i] = (ImageView) findViewById(img_level_id[i]);
            heart_img[i] = (ImageView) findViewById(img_heart_id[i]);
            coin_img[i] = (ImageView) findViewById(img_coin_id[i]);
            time_img[i] = (ImageView) findViewById(img_time_id[i]);

            deleteBtn[i] = (ImageButton) findViewById(delete_id[i]);
            deleteBtn[i].setEnabled(false);
            deleteBtn[i].setVisibility(View.INVISIBLE);

            ediText[i] = (EditText) findViewById(slot_edit_id[i]);
            ediText[i].setEnabled(false);
        }

        InformationSetting();

        slotImageBtn = new ImageButton[3];
        for (int i = 0; i < 3; i++) {
            delete_hash.put(delete_id[i], i);
            slot_hash.put(slot_id[i], i);
            slotImageBtn[i] = (ImageButton) findViewById(slot_id[i]);
        }
        for (int i = 0; i < 3; i++) {
            deleteBtn[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    int index = delete_hash.get(id);
                    adap.set(index + 1, Const.pref_exist, 0);

                    ediText[index].setEnabled(false);
                    ediText[index].setText("Empty");
                    textLevel[index].setText("");
                    textHeart[index].setText("");
                    textCoin[index].setText("");
                    textTime[index].setText("");
                    level_img[index].setVisibility(View.INVISIBLE);
                    heart_img[index].setVisibility(View.INVISIBLE);
                    coin_img[index].setVisibility(View.INVISIBLE);
                    time_img[index].setVisibility(View.INVISIBLE);

                    deleteBtn[index].setEnabled(false);
                    deleteBtn[index].setVisibility(View.INVISIBLE);
                    lp[index].setMargins(0, 0, 0, 0);
                    slotImageBtn[index].setLayoutParams(lp[index]);
                    focus = 0;
                    clickLong = false;
                }
            });
        }

        for(int i = 0; i < 3; i++){
            lp[i] = new FrameLayout.LayoutParams(slotImageBtn[i].getLayoutParams());
            slotImageBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (clickLong == true) {
                        clickLong = false;
                        return;
                    }
                    int id = view.getId();
                    int index = slot_hash.get(id);

                    if (focus != index + 1) {
                        for (int i = 0; i < 3; i++) {
                            ediText[i].setEnabled(false);
                            deleteBtn[i].setEnabled(false);
                            deleteBtn[i].setVisibility(View.INVISIBLE);
                            lp[i].setMargins(0, 0, 0, 0);
                            slotImageBtn[i].setLayoutParams(lp[i]);
                        }
                        lp[index].setMargins(12, 12, 12, 12);
                        slotImageBtn[index].setLayoutParams(lp[index]);
                        if (focus >= 1 && focus <= 3) {
                            if (adap.get(focus, Const.pref_exist, 0) == 1) {
                                ediText[focus - 1].setText(adap.get(focus, Const.pref_nickName, "Empty"));
                            } else ediText[focus - 1].setText("Empty");
                        }
                        if (adap.get(index + 1, Const.pref_exist, 0) == 0) {
                            ediText[index].setText("");
                            ediText[index].setEnabled(true);
                            ediText[index].requestFocus();
                        } else {
                            textLevel[index].setText(Integer.toString(adap.get(index + 1, Const.pref_level, 0)));
                            textHeart[index].setText(Integer.toString(adap.get(index + 1, Const.pref_heart, 0)));
                            textCoin[index].setText(Integer.toString(adap.get(index + 1, Const.pref_coin, 0)));
                            textTime[index].setText(Integer.toString(adap.get(index + 1, Const.pref_play_time, 0)/10));
                            level_img[index].setVisibility(View.VISIBLE);
                            heart_img[index].setVisibility(View.VISIBLE);
                            coin_img[index].setVisibility(View.VISIBLE);
                            time_img[index].setVisibility(View.VISIBLE);
                        }
                        focus = index + 1;
                    } else {
                        focus = 0;
                        ediText[index].setEnabled(false);
                        getInfoByDB(index + 1);

                        deleteBtn[index].setEnabled(false);
                        deleteBtn[index].setVisibility(View.INVISIBLE);

                        textLevel[index].setText(Integer.toString(adap.get(index + 1, Const.pref_level, 0)));
                        textHeart[index].setText(Integer.toString(adap.get(index + 1, Const.pref_heart, 0)));
                        textCoin[index].setText(Integer.toString(adap.get(index + 1, Const.pref_coin, 0)));
                        textTime[index].setText(Integer.toString(adap.get(index + 1, Const.pref_play_time, 0)/10));
                        level_img[index].setVisibility(View.VISIBLE);
                        heart_img[index].setVisibility(View.VISIBLE);
                        coin_img[index].setVisibility(View.VISIBLE);
                        time_img[index].setVisibility(View.VISIBLE);

                        edit.putString("bake_state", "00000_00000_00000_00000_00000_00000_00000_00000_00000_00000_00000_00000");
                        edit.putString("customer_state", "0000000_0000000_0000000");
                        edit.commit();

                        if(user[index].heart > 0) {
                            lp[index].setMargins(0, 0, 0, 0);
                            slotImageBtn[index].setLayoutParams(lp[index]);
                            Intent main_view = new Intent(CharacterActivity.this, MainActivity.class);
                            startActivity(main_view);
                        }
                    }
                }
            });
            slotImageBtn[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int id = view.getId();
                    int index = slot_hash.get(id);
                    if (1 <= focus && 3 >= focus && focus == index + 1 && adap.get(index + 1, Const.pref_exist, 0) == 1) {
                        deleteBtn[index].setVisibility(View.VISIBLE);
                        deleteBtn[index].setEnabled(true);
                        clickLong = true;
                    }
                    return false;
                }
            });
        }
        exitImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterActivity.this.finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!PAUSE) {
            PAUSE = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (PAUSE) {
                    InformationSetting();
                    PAUSE = false;
                }
            }
        });
    }

    public void getInfoByDB(int i) {
        if (adap.get(i, Const.pref_exist, 0) == 0) { // there is no character with slot i
            System.out.println("made the new character");
            adap.set(i, Const.pref_exist, 1);
            adap.set(i, Const.pref_level, 1);
            adap.set(i, Const.pref_coin, 100);
            adap.set(i, Const.pref_nickName, ediText[i - 1].getText().toString());
            adap.set(i, Const.pref_heart, 7);

            adap.set(i, Const.pref_paat, 20);
            adap.set(i, Const.pref_shu, 20);
            adap.set(i, Const.pref_flour, 20);
            adap.set(i, Const.pref_green_flour, 20);
            adap.set(i, Const.pref_flour_paat, 3);
            adap.set(i, Const.pref_gflour_paat, 3);
            adap.set(i, Const.pref_flour_shu, 3);
            adap.set(i, Const.pref_gflour_shu, 3);
            adap.set(i, Const.pref_play_time, 0);
        }

        user[i - 1].level = adap.get(i, Const.pref_level, 1);
        user[i - 1].coin = adap.get(i, Const.pref_coin, 100);
        user[i - 1].nickName = adap.get(i, Const.pref_nickName, "USER");
        user[i - 1].heart = adap.get(i, Const.pref_heart, 3);

        user[i - 1].paat = adap.get(i, Const.pref_paat, 20);
        user[i - 1].shu = adap.get(i, Const.pref_shu, 20);
        user[i - 1].flour = adap.get(i, Const.pref_flour, 20);
        user[i - 1].green_flour = adap.get(i, Const.pref_green_flour, 20);

        user[i - 1].flour_paat = adap.get(i, Const.pref_flour_paat, 3);
        user[i - 1].gflour_paat = adap.get(i, Const.pref_gflour_paat, 3);
        user[i - 1].flour_shu = adap.get(i, Const.pref_flour_shu, 3);
        user[i - 1].gflour_shu = adap.get(i, Const.pref_gflour_shu, 3);

        user[i - 1].play_time = adap.get(i, Const.pref_play_time, 0);

        edit.putInt(Const.pref_level, user[i - 1].level);
        edit.putInt(Const.pref_coin, user[i - 1].coin);
        edit.putString(Const.pref_nickName, user[i - 1].nickName);
        edit.putInt(Const.pref_heart, user[i - 1].heart);

        edit.putInt(Const.pref_paat, user[i - 1].paat);
        edit.putInt(Const.pref_shu, user[i - 1].shu);
        edit.putInt(Const.pref_flour, user[i - 1].flour);
        edit.putInt(Const.pref_green_flour, user[i - 1].green_flour);

        edit.putInt(Const.pref_flour_paat, user[i - 1].flour_paat);
        edit.putInt(Const.pref_gflour_paat, user[i - 1].gflour_paat);
        edit.putInt(Const.pref_flour_shu, user[i - 1].flour_shu);
        edit.putInt(Const.pref_gflour_shu, user[i - 1].gflour_shu);

        edit.putInt(Const.pref_play_time, user[i - 1].play_time);
        edit.putInt(Const.pref_slotNum, i);

        edit.commit();

        // data are loaded successfully
    }

    void InformationSetting() {
        for (int i = 0; i < 3; i++) {
            if (adap.get(i + 1, Const.pref_exist, 0) == 0) {
                ediText[i].setText("Empty");
                textLevel[i].setText("");
                textHeart[i].setText("");
                textCoin[i].setText("");
                textTime[i].setText("");
                level_img[i].setVisibility(View.INVISIBLE);
                heart_img[i].setVisibility(View.INVISIBLE);
                coin_img[i].setVisibility(View.INVISIBLE);
                time_img[i].setVisibility(View.INVISIBLE);

            } else {
                ediText[i].setText(adap.get(i + 1, Const.pref_nickName, ""));
                textLevel[i].setText(Integer.toString(adap.get(i + 1, Const.pref_level, 0)));
                textHeart[i].setText(Integer.toString(adap.get(i + 1, Const.pref_heart, 0)));
                textCoin[i].setText(Integer.toString(adap.get(i + 1, Const.pref_coin, 0)));
                textTime[i].setText(Integer.toString(adap.get(i + 1, Const.pref_play_time, 0)/10));
                level_img[i].setVisibility(View.VISIBLE);
                heart_img[i].setVisibility(View.VISIBLE);
                coin_img[i].setVisibility(View.VISIBLE);
                time_img[i].setVisibility(View.VISIBLE);
            }
            user[i] = new GameCharacter();
        }
    }

}