package seungju.fishcaketycoon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by LKH on 2015-12-01.
 */
public class StoreActivity extends Activity {

    SharedPreferences mine;
    SharedPreferences.Editor edt;

    ShowCase cur_show;
    ShowCase[] showcase_s;
    Product paat1, paat5, shu1, shu5, flour1, flour5, gflour1, gflour5;
    Product selectProduct;
    Item paat, shu, flour, gflour;

    ImageButton[] showcaseImageBtn;

    TextView coin_text;

    HashMap<Integer, Integer> vid_hash = new HashMap<Integer, Integer>();

    int[] vid = {R.id.store_item1, R.id.store_item2, R.id.store_item3, R.id.store_item4,
            R.id.store_item5, R.id.store_item6, R.id.store_item7, R.id.store_item8};

    int coin;
//    int play_time;
    int cur_show_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        mine = getSharedPreferences(Const.pref_my, MODE_PRIVATE);
        edt = mine.edit();

//        play_time = 0;

        selectProduct = null;

        coin = mine.getInt(Const.pref_coin, 0);
        coin_text = (TextView) findViewById(R.id.store_coin);
        coin_text.setText(Integer.toString(coin));

        cur_show_index = 0;
        showcase_s = new ShowCase[Const.max_store_showcase];
        for (int i = 0; i < Const.max_store_showcase; i++) {
            showcase_s[i] = new ShowCase();
            if (i == 0) initShow1(showcase_s[i]);
        }
        moveShowCase(0);


        showcaseImageBtn = new ImageButton[8];
        for (int i = 0; i < 8; i++) {
            showcaseImageBtn[i] = (ImageButton) findViewById(vid[i]);
            vid_hash.put(vid[i], i);
        }
        buttonListenerFunction();

        for(int i = 0; i < 8; i++){
            showcaseImageBtn[i].setAlpha(200);
        }

        ProductImage pi = new ProductImage();
        pi.start();

    }

    void buttonListenerFunction() {

        ImageButton btn_main = (ImageButton) findViewById(R.id.store_back);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreActivity.this.finish();
            }
        });

        ImageButton btn_left = (ImageButton) findViewById(R.id.store_prev);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveShowCase(-1);
            }
        });

        ImageButton btn_right = (ImageButton) findViewById(R.id.store_next);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveShowCase(+1);
            }
        });

        ImageButton btn_buy = (ImageButton) findViewById(R.id.store_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectProduct != null) {
                    if (coin >= selectProduct.getTotalPrice()) {
                        int get = mine.getInt(selectProduct.getItem().getName(), 0);
                        coin = coin - selectProduct.getTotalPrice();
                        coin_text.setText(Integer.toString(coin));

                        edt.putInt(selectProduct.getItem().getName(), get + selectProduct.getCount());
                        edt.putInt(Const.pref_coin, coin);
                        edt.commit();
                    }
                }
            }
        });

        for (int i = 0; i < 8; i++) {
            showcaseImageBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = vid_hash.get(v.getId());
                    Product pd = showcase_s[cur_show_index].getProduct(index + 1);
                    selectProduct = pd;
                    if(pd != null){
                        ((TextView)findViewById(R.id.store_price)).setText(Integer.toString(pd.getTotalPrice()));
                        for(int i = 0; i < 8; i++){
                            showcaseImageBtn[i].setAlpha(200);
                        }
                        showcaseImageBtn[index].setAlpha(255);
                    }
                }
            });
        }
    }

    void moveShowCase(int moveValue) {
        cur_show_index += moveValue;
        if (cur_show_index < 0) cur_show_index = 0;
        if (cur_show_index >= Const.max_store_showcase)
            cur_show_index = Const.max_store_showcase - 1;
        if (Const.max_store_showcase > 0) cur_show = showcase_s[cur_show_index];
        else cur_show = null;
    }

    void initShow1(ShowCase show) {

        paat = new Item(Const.pref_paat, Const.paat_price);
        shu = new Item(Const.pref_shu, Const.shu_price);
        flour = new Item(Const.pref_flour, Const.flour_price);
        gflour = new Item(Const.pref_green_flour, Const.gflour_price);

        paat1 = new Product(paat, 1);
        paat1.setImageID(R.drawable.button_paat);
        paat5 = new Product(paat, 5);
        paat5.setImageID(R.drawable.button_paat_5);
        shu1 = new Product(shu, 1);
        shu1.setImageID(R.drawable.button_shu);
        shu5 = new Product(shu, 5);
        shu5.setImageID(R.drawable.button_shu_5);
        flour1 = new Product(flour, 1);
        flour1.setImageID(R.drawable.flour);
        flour5 = new Product(flour, 5);
        flour5.setImageID(R.drawable.flour_5);
        gflour1 = new Product(gflour, 1);
        gflour1.setImageID(R.drawable.green_flour);
        gflour5 = new Product(gflour, 5);
        gflour5.setImageID(R.drawable.green_flour_5);

        show.setProduct(1, paat1);
        show.setProduct(2, paat5);
        show.setProduct(3, shu1);
        show.setProduct(4, shu5);
        show.setProduct(5, flour1);
        show.setProduct(6, flour5);
        show.setProduct(7, gflour1);
        show.setProduct(8, gflour5);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Debug.stopMethodTracing();
//        edt.putInt(Const.pref_play_time, mine.getInt(Const.pref_play_time, 0) + play_time);
//        edt.commit();
    }

    class ProductImage extends Thread {
        @Override
        public void run() {

            while (true) {
                if(mine.getInt(Const.pref_heart, 5) == 0){
                    StoreActivity.this.finish();
                    break;
                }

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
//                                      play_time++;
                                      if (cur_show != null) {
                                          for (int i = 0; i < 8; i++) {
                                              if (cur_show.getProduct(i + 1) != null)
                                                  showcaseImageBtn[i].setImageResource(cur_show.getProduct(i + 1).getImageID());
                                              else showcaseImageBtn[i].setImageResource(0);
                                          }
                                      } else {
                                          for (int i = 0; i < 8; i++)
                                              showcaseImageBtn[i].setImageResource(0);
                                      }
                                  }
                              }

                );

                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}