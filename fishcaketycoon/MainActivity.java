package seungju.fishcaketycoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by SeungJu on 2015-11-30.
 */
public class MainActivity extends Activity {

    SharedPreferences mine;
    SharedPreferences.Editor edt;

    DBAdapter adap;

    Customer customer[] = new Customer[3];

    Toast gameover, nightmare, korean;

    ImageButton customerFrame[] = new ImageButton[3];

    TextView Stock[] = new TextView[4];
    TextView[] req = new TextView[4];
    TextView[] inGredients = new TextView[4];
    TextView myHeart, customerTimer;

    HashMap<Integer, Integer> myId = new HashMap<Integer, Integer>();

    int cid[] = {R.drawable.guest1, R.drawable.guest2, R.drawable.guest3, R.drawable.guest4, R.drawable.guest5,
            R.drawable.guest6, R.drawable.guest7, R.drawable.guest8, R.drawable.guest9, R.drawable.guest10};
    int vid[] = {R.id.customer1, R.id.customer2, R.id.customer3};
    int selectedCustomer;
    int play_time;
    int level, heart;
    int ingredients[] = new int[4];
    int stock[] = new int[4];

    boolean TERMINATE, FINISH;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mine = getSharedPreferences("MyInfo", MODE_PRIVATE);
        edt = mine.edit();

        adap = new DBAdapter(getApplicationContext());

        play_time = 0;
        TERMINATE = FINISH = false;

        ImageButton btn_bake = (ImageButton) findViewById(R.id.bake_bread);
        btn_bake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu_view = new Intent(MainActivity.this, BakeActivity.class);
                startActivity(menu_view);
            }
        });

        ImageButton btn_store = (ImageButton) findViewById(R.id.store);
        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu_view = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(menu_view);
            }
        });

        ImageButton btn_exit = (ImageButton) findViewById(R.id.exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save & exit
                saveData();
                TERMINATE = true;
                MainActivity.this.finish();
            }
        });

        for (int i = 0; i < 3; i++) myId.put(vid[i], i);

        // settings

        String orign_state = mine.getString("customer_state", "0000000_0000000_0000000"); // default
        level = mine.getInt(Const.pref_level, 0); // default

        // 0000000 : default, (X)[LM]ABCD : white + paat (A), white + shu (B), green + paat (C), green + shu (D)
        // X : customer X, TTL(LM)

        req[0] = (TextView) findViewById(R.id.want_bread1);
        req[1] = (TextView) findViewById(R.id.want_bread2);
        req[2] = (TextView) findViewById(R.id.want_bread3);
        req[3] = (TextView) findViewById(R.id.want_bread4);

        myHeart = (TextView) findViewById(R.id.heart_text);
        customerTimer = (TextView) findViewById(R.id.timer_text);

        inGredients[0] = (TextView) findViewById(R.id.main_view_flour);
        inGredients[1] = (TextView) findViewById(R.id.main_view_green_flour);
        inGredients[2] = (TextView) findViewById(R.id.main_view_paat);
        inGredients[3] = (TextView) findViewById(R.id.main_view_shu);

        ingredients[0] = mine.getInt(Const.pref_flour, 15);
        ingredients[1] = mine.getInt(Const.pref_green_flour, 15);
        ingredients[2] = mine.getInt(Const.pref_paat, 15);
        ingredients[3] = mine.getInt(Const.pref_shu, 15);

        Stock[0] = (TextView) findViewById(R.id.flour_paat);
        Stock[1] = (TextView) findViewById(R.id.flour_shu);
        Stock[2] = (TextView) findViewById(R.id.gflour_paat);
        Stock[3] = (TextView) findViewById(R.id.gflour_shu);

        stock[0] = mine.getInt(Const.pref_flour_paat, 0);
        stock[1] = mine.getInt(Const.pref_flour_shu, 0);
        stock[2] = mine.getInt(Const.pref_gflour_paat, 0);
        stock[3] = mine.getInt(Const.pref_gflour_shu, 0);

        heart = mine.getInt(Const.pref_heart, 7);

        for (int i = 0; i < 4; i++) req[i].setText("0");

        gameover = Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT);
        nightmare = Toast.makeText(MainActivity.this, "LV UP ! NIGHTMARE", Toast.LENGTH_SHORT);
        korean = Toast.makeText(MainActivity.this, "LV UP ! KOREAN", Toast.LENGTH_SHORT);

        String state[] = orign_state.split("_");
        for (int i = 0; i < state.length && i < 3; i++) {
            String cur = state[i];
            if (cur.charAt(0) == '0') customer[i] = new Customer();
            else {
                customer[i] = new Customer(hexToDecimal(cur.charAt(0)), hexToDecimal(cur.charAt(1)) * 16 + hexToDecimal(cur.charAt(2)), hexToDecimal(cur.charAt(3)), hexToDecimal(cur.charAt(4)), hexToDecimal(cur.charAt(5)), hexToDecimal(cur.charAt(6)));
            }
        }

        int who = random.nextInt(3);
        customer[who] = new Customer(level);

        for (int i = 0; i < state.length && i < 3; i++) {
            customerFrame[i] = (ImageButton) findViewById(vid[i]);
            customerFrame[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int id = myId.get(v.getId());

                    if (customer[id].ttl > 0) {
                        // as.sign to bread
                        // if I have enough bread, then I'll give them to customer
                        if (customer[id].myreq.flour_paat <= stock[0]) {
                            if (customer[id].myreq.flour_shu <= stock[1]) {
                                if (customer[id].myreq.gflour_paat <= stock[2]) {
                                    if (customer[id].myreq.gflour_shu <= stock[3]) {

                                        stock[0] -= customer[id].myreq.flour_paat;
                                        stock[1] -= customer[id].myreq.flour_shu;
                                        stock[2] -= customer[id].myreq.gflour_paat;
                                        stock[3] -= customer[id].myreq.gflour_shu;

                                        edt.putInt(Const.pref_flour_paat, stock[0]);
                                        edt.putInt(Const.pref_flour_shu, stock[1]);
                                        edt.putInt(Const.pref_gflour_paat, stock[2]);
                                        edt.putInt(Const.pref_gflour_shu, stock[3]);
                                        edt.putInt(Const.pref_coin, mine.getInt(Const.pref_coin, 0) + customer[id].tcnt * Const.sales_price);

                                        edt.commit();

                                        customer[id] = new Customer(level);
                                        if (heart < 10) heart++;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
            });
            customerFrame[i].setOnClickListener(new View.OnClickListener() {
                // show requirement
                @Override
                public void onClick(View v) {

                    int id = myId.get(v.getId());

                    if (customer[id].wait == 0 && customer[id].ttl > 0) {
                        // show requirement on textView
                        int want[] = {customer[id].myreq.flour_paat, customer[id].myreq.flour_shu, customer[id].myreq.gflour_paat, customer[id].myreq.gflour_shu};

                        req[0].setText(String.valueOf(want[0]));
                        req[1].setText(String.valueOf(want[1]));
                        req[2].setText(String.valueOf(want[2]));
                        req[3].setText(String.valueOf(want[3]));

                        for (int j = 0; j < 4; j++) {
                            if (want[j] == 0) req[j].setTextColor(Color.BLACK);
                            else if (want[j] <= stock[j]) {
                                req[j].setTextColor(Color.GREEN);
                            } else {
                                req[j].setTextColor(Color.RED);
                            }
                        }
                        selectedCustomer = id + 1;
                    } else {
                        // ignore
                        for (int i = 0; i < 4; i++) {
                            req[i].setText("0");
                            req[i].setTextColor(Color.BLACK);
                        }
                        selectedCustomer = 0;
                    }
                }
            });
        }

        CustomerService cs = new CustomerService();
        cs.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Debug.stopMethodTracing();

        FINISH = true;

        if (!TERMINATE) {
            String newCustomerInfo = "";

            for (int i = 0; i < 3; i++) {
                if (i > 0) newCustomerInfo += "_";
                newCustomerInfo += decToHex(customer[i].type);
                newCustomerInfo += decimalToHex(customer[i].ttl);
                newCustomerInfo += decimalToHex(customer[i].myreq.flour_paat) + decimalToHex(customer[i].myreq.flour_shu) + decimalToHex(customer[i].myreq.gflour_paat) + decimalToHex(customer[i].myreq.gflour_shu);
            }
            System.out.println("save customer information");
            System.out.println("new information : " + newCustomerInfo);

            edt.putString("customer_state", newCustomerInfo);
            // useless
            edt.putInt(Const.pref_play_time, mine.getInt(Const.pref_play_time, 0) + play_time);
            edt.commit();
        }
    }

    class CustomerService extends Thread {

        @Override
        public void run() {

            while (!TERMINATE && !FINISH) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (heart == 0) {
                            if (!TERMINATE) {
                                TERMINATE = true;
                                gameover.show();

                                edt.putInt(Const.pref_play_time, mine.getInt(Const.pref_play_time, 0) + play_time);
                                edt.putInt(Const.pref_heart, heart);
                                edt.commit();

                                saveData();

                                adap.record(mine.getString(Const.pref_nickName, "null"), mine.getInt(Const.pref_play_time, 0));
                                Intent rank_view = new Intent(MainActivity.this, RankActivity.class);
                                startActivity(rank_view);

                                MainActivity.this.finish();
                                return;
                            }
                        }

                        play_time++;
                        int real_play_time = mine.getInt(Const.pref_play_time, 0) + play_time;
                        if(real_play_time % 100 == 0){
                            int nxtLevel = real_play_time / 100;
                            if(nxtLevel == Const.NIGHTMARE){
                                nightmare.show();
                                edt.putInt(Const.pref_level, 1);
                                edt.commit();
                                level = mine.getInt(Const.pref_level, nxtLevel);
//                                edt.putInt(Const.pref_play_time, real_play_time);
//                                play_time = 0;
//                                edt.commit();
                            }
                            else if(nxtLevel == Const.KOREAN){
                                korean.show();
                                edt.putInt(Const.pref_level, 2
                                );
                                edt.commit();
                                level = mine.getInt(Const.pref_level, nxtLevel);
//                                edt.putInt(Const.pref_play_time, real_play_time);
//                                play_time = 0;
//                                edt.commit();
                            }
                        }
                        myHeart.setText("+" + String.valueOf(heart));

                        ingredients[0] = mine.getInt(Const.pref_flour, 15);
                        ingredients[1] = mine.getInt(Const.pref_green_flour, 15);
                        ingredients[2] = mine.getInt(Const.pref_paat, 15);
                        ingredients[3] = mine.getInt(Const.pref_shu, 15);

                        stock[0] = mine.getInt(Const.pref_flour_paat, 0);
                        stock[1] = mine.getInt(Const.pref_flour_shu, 0);
                        stock[2] = mine.getInt(Const.pref_gflour_paat, 0);
                        stock[3] = mine.getInt(Const.pref_gflour_shu, 0);

                        if (selectedCustomer == 0 || customer[selectedCustomer - 1].wait > 0) {
                            customerTimer.setText("");
                            for (int i = 0; i < 4; i++) {
                                req[i].setText("0");
                                req[i].setTextColor(Color.BLACK);
                            }
                        } else
                            customerTimer.setText(String.valueOf(customer[selectedCustomer - 1].ttl));

                        for (int i = 0; i < 4; i++) {
                            inGredients[i].setText(String.valueOf(ingredients[i]));
                            Stock[i].setText(String.valueOf(stock[i]));
                        }

                        for (int i = 0; i < 3; i++) {
                            showCustomer(i);
                            if (customer[i].wait > 0) customer[i].wait--;
                            else {
                                if (customer[i].ttl > 0) {
                                    // is alive
                                    customer[i].ttl--;
                                    if (customer[i].ttl == 0) {
                                        if (customer[i].alive) {
                                            if (heart > 0) heart--;
                                        }
                                        if (selectedCustomer == i) selectedCustomer = 0;

                                        customer[i] = new Customer(level);
                                    }
                                } else {
                                    if (customer[i].ttl == 0) {
                                        if (customer[i].alive) {
                                            if (heart > 0) heart--;
                                        }
                                        if (selectedCustomer == i) selectedCustomer = 0;
                                        customer[i] = new Customer(level);
                                    }
                                }
                            }

                        }
                    }

                    private void showCustomer(int i) {
                        if (customer[i].wait == 0 && customer[i].ttl > 0) {
                            // is alive
                            if (customer[i].type == 1) {
                                if (customer[i].ttl > 160)
                                    customerFrame[i].setImageResource(R.drawable.guest1_come);
                                else if (customer[i].ttl > 80)
                                    customerFrame[i].setImageResource(R.drawable.guest1_wait);
                                else {
                                    customerFrame[i].setImageResource(R.drawable.guest1_angry);
                                }
                            } else if (customer[i].type == 2) {
                                if (customer[i].ttl > 160)
                                    customerFrame[i].setImageResource(R.drawable.guest2_come);
                                else if (customer[i].ttl > 80)
                                    customerFrame[i].setImageResource(R.drawable.guest2_wait);
                                else {
                                    customerFrame[i].setImageResource(R.drawable.guest2_angry);
                                }
                            } else if (customer[i].type == 3) {
                                if (customer[i].ttl > 160)
                                    customerFrame[i].setImageResource(R.drawable.guest3_come);
                                else if (customer[i].ttl > 80)
                                    customerFrame[i].setImageResource(R.drawable.guest3_wait);
                                else {
                                    customerFrame[i].setImageResource(R.drawable.guest3_angry);
                                }
                            }
                        } else {
                            // ignore

                            int who = random.nextInt(cid.length);
                            customerFrame[i].setImageResource(cid[who]);
                        }
                        customerFrame[i].setActivated(true);
                    }
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void saveData() {
        edt.putInt(Const.pref_play_time, mine.getInt(Const.pref_play_time, 0) + play_time);
        edt.putInt(Const.pref_heart, heart);
        edt.commit();

        int slotNum = mine.getInt(Const.pref_slotNum, 0);
        if (slotNum >= 1 && slotNum <= 3) {
            adap.set(slotNum, Const.pref_level, mine.getInt(Const.pref_level, 0));
            adap.set(slotNum, Const.pref_coin, mine.getInt(Const.pref_coin, 100));
            adap.set(slotNum, Const.pref_nickName, mine.getString(Const.pref_nickName, "USER"));
            adap.set(slotNum, Const.pref_heart, mine.getInt(Const.pref_heart, 7));

            adap.set(slotNum, Const.pref_paat, mine.getInt(Const.pref_paat, 20));
            adap.set(slotNum, Const.pref_shu, mine.getInt(Const.pref_shu, 20));
            adap.set(slotNum, Const.pref_flour, mine.getInt(Const.pref_flour, 20));
            adap.set(slotNum, Const.pref_green_flour, mine.getInt(Const.pref_green_flour, 20));

            adap.set(slotNum, Const.pref_flour_paat, mine.getInt(Const.pref_flour_paat, 3));
            adap.set(slotNum, Const.pref_gflour_paat, mine.getInt(Const.pref_gflour_paat, 3));
            adap.set(slotNum, Const.pref_flour_shu, mine.getInt(Const.pref_flour_shu, 3));
            adap.set(slotNum, Const.pref_gflour_shu, mine.getInt(Const.pref_gflour_shu, 3));

            adap.set(slotNum, Const.pref_play_time, mine.getInt(Const.pref_play_time, 0));

            System.out.println("saved completely");
        }
    }

    int isValue(char a) {
        if (a >= 'A') return 10 + a - 'A';
        else
            return a - '0';
    }

    int hexToDecimal(char a) {
        return isValue(a);
    }

    char decToHex(int a) {
        if (a < 10) return (char) (a + '0');
        else
            return (char) (a - 10 + 'A');
    }

    String decimalToHex(int a) {
        String ret = "";
        ret += decToHex(a % 16);
        a /= 16;
        ret = decToHex(a) + ret;
        // System.out.println(ret);
        return ret;
    }
}