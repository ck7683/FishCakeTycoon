package seungju.fishcaketycoon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by SeungJu on 2015-11-30.
 */
public class BakeActivity extends Activity {
    SharedPreferences mine;
    SharedPreferences.Editor edt;
    SoundPool mypool = null;

    ImageButton[] fishFrame = new ImageButton[12];
    TextView inGredients[] = new TextView[4];

    Fish fish[] = new Fish[12];

    HashMap <Integer, Integer> myId = new HashMap<Integer, Integer>();

    int ingredients[] = new int[4];
    static final int BURNED = 225;
    static final int WELLDONE = 175;
    static final int MEDIUM = 140;

    int snd; // sound
    int fsnd; // fire sound
//    int play_time;

    boolean FIRE, SHU, PAT, FLR, GFLR;

    int vid[] = {R.id.fish1, R.id.fish2, R.id.fish3, R.id.fish4,
            R.id.fish5, R.id.fish6, R.id.fish7, R.id.fish8,
            R.id.fish9, R.id.fish10, R.id.fish11, R.id.fish12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bake);

        // only my app
        mine = getSharedPreferences(Const.pref_my, MODE_PRIVATE);
        edt = mine.edit();

//        play_time = 0;

        FIRE = SHU = PAT = FLR = GFLR = false;

        for(int i=0; i<12; i++){
            myId.put(vid[i], i);
        }

        mypool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        snd = mypool.load(this, R.raw.button11, 1);
        fsnd = mypool.load(this, R.raw.btnfire, 1);

        ImageButton btn_fire = (ImageButton) findViewById(R.id.fire);

        btn_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int streamId = mypool.play(fsnd, 1.0F, 1.0F, 1, 0, 1.0F);
                // music, left volume, right volumn, priority, loop, speed
                // loop : -1 >> infinite loop
                FIRE = SHU = PAT = FLR = GFLR = false;
                FIRE = true;
            }
        });

        ImageButton btn_flr = (ImageButton) findViewById(R.id.flour);

        btn_flr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int streamId = mypool.play(snd, 1.0F, 1.0F, 1, 0, 1.0F);
                // music, left volume, right volumn, priority, loop, speed
                // loop : -1 >> infinite loop
                FIRE = SHU = PAT = FLR = GFLR = false;
                FLR = true;
            }
        });

        ImageButton btn_gflr = (ImageButton) findViewById(R.id.green_flour);

        btn_gflr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int streamId = mypool.play(snd, 1.0F, 1.0F, 1, 0, 1.0F);
                // music, left volume, right volumn, priority, loop, speed
                // loop : -1 >> infinite loop
                FIRE = SHU = PAT = FLR = GFLR = false;
                GFLR = true;
            }
        });

        ImageButton btn_pat = (ImageButton) findViewById(R.id.paat);
        btn_pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int streamId = mypool.play(snd, 1.0F, 1.0F, 1, 0, 1.0F);
                // music, left volume, right volumn, priority, loop, speed
                // loop : -1 >> infinite loop
                FIRE = SHU = PAT = FLR = GFLR = false;
                PAT = true;
            }
        });

        ImageButton btn_shu = (ImageButton) findViewById(R.id.shu);
        btn_shu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int streamId = mypool.play(snd, 1.0F, 1.0F, 1, 0, 1.0F);
                // music, left volume, right volumn, priority, loop, speed
                // loop : -1 >> infinite loop
                FIRE = SHU = PAT = FLR = GFLR = false;
                SHU = true;
            }
        });

        ImageButton btn_store = (ImageButton)findViewById(R.id.store);
        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int streamId = mypool.play(snd, 1.0F, 1.0F, 1, 0, 1.0F);
                // music, left volume, right volumn, priority, loop, speed
                // loop : -1 >> infinite loop
                Intent market_view = new Intent(BakeActivity.this, StoreActivity.class);
                startActivity(market_view);
                // BakeActivity.this.finish();
            }
        });

        ImageButton btn_exit = (ImageButton)findViewById(R.id.exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BakeActivity.this.finish();
            }
        });

        inGredients[0] = (TextView)findViewById(R.id.flour_text);
        inGredients[1] = (TextView)findViewById(R.id.green_flour_text);
        inGredients[2] = (TextView)findViewById(R.id.paat_text);
        inGredients[3] = (TextView)findViewById(R.id.shu_text);

        ingredients[0] = mine.getInt(Const.pref_flour, 0);
        ingredients[1] = mine.getInt(Const.pref_green_flour, 0);
        ingredients[2] = mine.getInt(Const.pref_paat, 0);
        ingredients[3] = mine.getInt(Const.pref_shu, 0);

        // settings

        String orign_state = mine.getString("bake_state", "00000_00000_00000_00000_00000_00000_00000_00000_00000_00000_00000_00000"); // default

        // 00000 : default, (X)FFFF : front burend, back burned
        // 1 : flour
        // 2 : flour + paat
        // 3 : flour + shu
        // 4 : gflour
        // 5 : gflour + paat
        // 6 : gflour + shu

        String state[] = orign_state.split("_");
        for (int i = 0; i < state.length && i < 12; i++) {
            String cur = state[i];
            if (cur.charAt(0) == '0') fish[i] = new Fish();
            else {
                fish[i] = new Fish(cur.charAt(0) - '0', hexToDecimal(cur.charAt(1), cur.charAt(2)), hexToDecimal(cur.charAt(3), cur.charAt(4)));
            }
        }

        for (int i = 0; i < 12; i++) {
            fishFrame[i] = (ImageButton) findViewById(vid[i]);
            fishFrame[i].setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {

                    FIRE = SHU = PAT = FLR = GFLR = false;

                    int id = myId.get(v.getId());
                    if(fish[id].fs > 0 || fish[id].bs > 0){
                        if(fish[id].fs >= WELLDONE && fish[id].fs < BURNED){
                            if(fish[id].bs >= WELLDONE && fish[id].bs < BURNED){
                                if(fish[id].type == 2){ // flour + paat
                                    edt.putInt(Const.pref_flour_paat, mine.getInt(Const.pref_flour_paat, 0) + 1);
                                    edt.commit();
                                }else if(fish[id].type == 3){ // flour + shu
                                    edt.putInt(Const.pref_flour_shu, mine.getInt(Const.pref_flour_shu, 0) + 1);
                                    edt.commit();
                                }else if(fish[id].type == 5){ // gflour + paat
                                    edt.putInt(Const.pref_gflour_paat, mine.getInt(Const.pref_gflour_paat, 0) + 1);
                                    edt.commit();
                                }else if(fish[id].type == 6){ // gflour + shu
                                    edt.putInt(Const.pref_gflour_shu, mine.getInt(Const.pref_gflour_shu, 0) + 1);
                                    edt.commit();
                                }
                            }
                        }
                        // is alive & take out
                        fish[id] = new Fish(); // set default
                    }else{
                        // ignore
                    }
                    return false;
                }
            });
            fishFrame[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int id = myId.get(v.getId());
                    if(fish[id].fs > 0 || fish[id].bs > 0){
                        // toggle
                        int tmp = fish[id].fs;
                        fish[id].fs = fish[id].bs;
                        fish[id].bs = tmp;
                    }
                    else if (FIRE) {
                        if (fish[id].fs == 0 && fish[id].bs == 0) {
                            // is not alive
                            if (fish[id].bread && fish[id].ingre) {
                                fish[id].fs = 1; // is alive
                            } else {
                                // ignore
                            }
                        }
                    } else if (FLR || GFLR) {
                        if (fish[id].fs == 0 && fish[id].bs == 0) {
                            if (FLR) {
                                int flour = mine.getInt(Const.pref_flour, 0);
                                if(flour > 0 && fish[id].type != 1){
                                    fish[id].type = 1;
                                    edt.putInt(Const.pref_flour, flour - 1);
                                    edt.commit();
                                    fish[id].bread = true;
                                    fish[id].ingre = false;
                                }else{
                                    // ignore
                                }
                            }
                            else {
                                int gflour = mine.getInt(Const.pref_green_flour, 0);
                                if(gflour > 0 && fish[id].type != 4){
                                    fish[id].type = 4;
                                    edt.putInt(Const.pref_green_flour, gflour - 1);
                                    edt.commit();
                                    fish[id].bread = true;
                                    fish[id].ingre = false;
                                }else{
                                    // ignore
                                }
                            }
                        } else {
                            // ignore
                        }
                    } else if (PAT || SHU) {
                        if (fish[id].fs == 0 && fish[id].bs == 0) {
                            if (fish[id].bread) {
                                if (PAT) {
                                    int pat = mine.getInt(Const.pref_paat, 0);
                                    if(pat > 0) {
                                        if (fish[id].type == 1 || fish[id].type == 4) {
                                            fish[id].type++;
                                            edt.putInt(Const.pref_paat, pat-1);
                                            edt.commit();
                                            fish[id].ingre = true;
                                        }
                                        else if (fish[id].type == 3 || fish[id].type == 6) {
                                            fish[id].type--;
                                            edt.putInt(Const.pref_paat, pat-1);
                                            edt.commit();
                                            fish[id].ingre = true;
                                        }
                                        else {
                                            // ignore
                                        }
                                    }else{
                                        // ignore
                                    }
                                }
                                else {
                                    int shu = mine.getInt(Const.pref_shu, 0);
                                    if(shu > 0) {
                                        if (fish[id].type == 1 || fish[id].type == 4) {
                                            fish[id].type += 2;
                                            edt.putInt(Const.pref_shu, shu-1);
                                            edt.commit();
                                            fish[id].ingre = true;
                                        }
                                        else if (fish[id].type == 2 || fish[id].type == 5) {
                                            fish[id].type++;
                                            edt.putInt(Const.pref_shu, shu-1);
                                            edt.commit();
                                            fish[id].ingre = true;
                                        }
                                        else {
                                            // ignore
                                        }
                                    }else{
                                        // ignore
                                    }
                                }
                            } else {
                                // ignore
                            }
                        } else {
                            // ignore
                        }
                    }
                }
            });
        }

        BakeMachine bm = new BakeMachine();
        bm.start();


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Debug.stopMethodTracing();

        String newBakeInfo = "";


        for(int i=0; i<12; i++){
            if(i > 0)newBakeInfo += "_";
            newBakeInfo += decToHex(fish[i].type);
            newBakeInfo += decimalToHex(fish[i].fs) + decimalToHex(fish[i].bs);
        }
        edt.putString("bake_state", newBakeInfo);
//        edt.putInt(Const.pref_play_time, mine.getInt(Const.pref_play_time, 0) + play_time);
        edt.commit();
    }


    class BakeMachine extends Thread {
//        boolean toggle = false;
        @Override
        public void run() {

           while(true) {
               if(mine.getInt(Const.pref_heart, 5) == 0){
                   BakeActivity.this.finish();
                   break;
               }
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
//                                      toggle ^= true;
//                                      if(toggle == true)play_time++;
                                      ingredients[0] = mine.getInt(Const.pref_flour, 15);
                                      ingredients[1] = mine.getInt(Const.pref_green_flour, 15);
                                      ingredients[2] = mine.getInt(Const.pref_paat, 15);
                                      ingredients[3] = mine.getInt(Const.pref_shu, 15);
                                      for(int i=0; i<4; i++)inGredients[i].setText(String.valueOf(ingredients[i]));

                                      for (int i = 0; i < 12; i++) {
                                          showState(i);
                                          if(fish[i].fs > 0 || fish[i].bs > 0) {
                                              // is alive
                                              if(fish[i].fs < 255)fish[i].fs++;
                                          }
                                      }

                                  }

                                  private void showState(int i) {
                                      if (fish[i].type == 0) {
                                          fishFrame[i].setImageResource(R.drawable.fish_default);
                                      } else {
                                          if (fish[i].fs > 0 || fish[i].bs > 0) { // is alive
                                              if(fish[i].fs >= BURNED){
                                                  fishFrame[i].setImageResource(R.drawable.fish_burned);
                                              }
                                              else if (fish[i].type < 4) {
                                                  if (fish[i].fs >= WELLDONE)
                                                      fishFrame[i].setImageResource(R.drawable.fish_white3);
                                                  else if (fish[i].fs > MEDIUM)
                                                      fishFrame[i].setImageResource(R.drawable.fish_white2);
                                                  else // is raw
                                                      fishFrame[i].setImageResource(R.drawable.fish_white1);
                                              } else {
                                                  if (fish[i].fs >= WELLDONE)
                                                      fishFrame[i].setImageResource(R.drawable.fish_green3);
                                                  else if (fish[i].fs > MEDIUM)
                                                      fishFrame[i].setImageResource(R.drawable.fish_green2);
                                                  else // is raw
                                                      fishFrame[i].setImageResource(R.drawable.fish_green1);
                                              }
                                          } else {
                                              if (fish[i].type == 1)
                                                  fishFrame[i].setImageResource(R.drawable.fish_base_white);
                                              else if (fish[i].type == 2)
                                                  fishFrame[i].setImageResource(R.drawable.fish_base_white_paat);
                                              else if (fish[i].type == 3)
                                                  fishFrame[i].setImageResource(R.drawable.fish_base_white_shu);
                                              else if (fish[i].type == 4)
                                                  fishFrame[i].setImageResource(R.drawable.fish_base_green);
                                              else if (fish[i].type == 5)
                                                  fishFrame[i].setImageResource(R.drawable.fish_base_green_paat);
                                              else if (fish[i].type == 6)
                                                  fishFrame[i].setImageResource(R.drawable.fish_base_green_shu);
                                          }
                                      }
                                      fishFrame[i].setActivated(true);

                                  }
                              }

                );

               try {
                   Thread.sleep(50);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
            }

        }
    }


    int isValue(char a) {
        if (a >= 'A') return 10 + a - 'A';
        else
            return a - '0';
    }

    int hexToDecimal(char a, char b) {
        return isValue(a) * 16 + isValue(b);
    }
    char decToHex(int a){
        if(a < 10)return (char)(a + '0');
        else
            return (char)(a-10+'A');
    }
    String decimalToHex(int a){
        String ret = "";
        ret += decToHex(a%16);
        a /= 16;
        ret = decToHex(a) + ret;
        return ret;
    }
}