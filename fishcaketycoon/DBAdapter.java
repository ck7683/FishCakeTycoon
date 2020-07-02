package seungju.fishcaketycoon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Gideon on 2015-12-02.
 */

// db function for program
public class DBAdapter {

    private Context context; // database context

    static final String usertable = "TB_USERDATA";
    static final String ranktable = "TB_RANK";

    private SQLiteDatabase db; // database association object

    private HashMap<String, String> pars = new HashMap<String, String>();

    public DBAdapter(Context _context) {
        this.context = _context;
        this.open();

        // SLOT

        pars.put(Const.pref_nickName, "NICKNAME");
        pars.put(Const.pref_exist, "EXIST");
        pars.put(Const.pref_play_time, "PLAYTIME");

        pars.put(Const.pref_flour_paat, "STOCK1");
        pars.put(Const.pref_flour_shu, "STOCK2");
        pars.put(Const.pref_gflour_paat, "STOCK3");
        pars.put(Const.pref_gflour_shu, "STOCK4");

        pars.put(Const.pref_paat, "INGREDIENT1");
        pars.put(Const.pref_shu, "INGREDIENT2");
        pars.put(Const.pref_flour, "INGREDIENT3");
        pars.put(Const.pref_green_flour, "INGREDIENT4");

        pars.put(Const.pref_heart, "HEART");
        pars.put(Const.pref_level, "LEVEL");
        pars.put(Const.pref_coin, "MONEY");
    } // constructor

    public void open() throws SQLException {
        try {
            db = (new DBHelper(context).getWritableDatabase());
        } catch(SQLiteException e) {
            db = (new DBHelper(context).getReadableDatabase());
        }
    } // db open

    public void create(String name, int slot) {

        ContentValues values = new ContentValues();
        values.put("NICKNAME", name);
        String[] whereArgs = new String[]{Integer.toString(slot)};
        db.update(usertable, values,"SLOT=?",whereArgs);

        return;
    }

    // hashing function
    public void set(int slot, String tag, int value) {
        ContentValues values = new ContentValues();

        tag = pars.get(tag);

        values.put(tag, value);
        String[] whereArgs = new String[]{Integer.toString(slot)};
        db.update(usertable, values,"SLOT=?",whereArgs);
    }

    public void set(int slot, String tag, String value) {
        ContentValues values = new ContentValues();

        tag = pars.get(tag);
        values.put(tag, value);
        String[] whereArgs = new String[]{Integer.toString(slot)};
        db.update(usertable, values, "SLOT=?", whereArgs);
    }

    public int get(int slot, String tag, int fail_value) {

        String sql;
        int return_value;

        tag = pars.get(tag);

        sql = "SELECT \"" + tag + "\" FROM " + usertable + " WHERE SLOT="
                + Integer.toString(slot) + ";";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor == null || cursor.getCount() == 0)
            return fail_value;

        cursor.moveToFirst();
        int col = cursor.getColumnIndex(tag);

        if(col == -1) return  fail_value; // there is no column
        return_value =  cursor.getInt(col);
        return return_value;
    }

    public String get(int slot, String tag, String fail_value) {
        String sql;
        String return_value;

        tag = pars.get(tag);

        sql = "SELECT \"" + tag + "\" FROM " + usertable + " WHERE SLOT="
                + Integer.toString(slot) + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor == null || cursor.getCount() == 0)
            return fail_value;

        cursor.moveToFirst();
        int col = cursor.getColumnIndex(tag);
        if(col == -1) return  fail_value;
        return_value =  cursor.getString(col);
        return return_value;
    }

    public void save(int slot, int money, int time,
                     int stock1, int stock2, int stock3, int stock4,
                    int ingredient1, int ingredient2, int ingredient3, int ingredient4,
                     int heart) {

        ContentValues values = new ContentValues();
        values.put("MONEY", money);
        values.put("PLAYTIME", time);
        values.put("STOCK1", stock1);
        values.put("STOCK2", stock2);
        values.put("STOCK3", stock3);
        values.put("STOCK4", stock4);
        values.put("INGREDIENT1", ingredient1);
        values.put("INGREDIENT2", ingredient2);
        values.put("INGREDIENT3", ingredient3);
        values.put("INGREDIENT4", ingredient4);
        values.put("HEART", heart);

        String[] whereArgs = new String[]{Integer.toString(slot)};
        db.update(usertable, values,"SLOT=?",whereArgs);

        return;
    }

    public void load(int slot) {

        String sql;
        sql = "SELECT * FROM " + usertable + " WHERE SLOT="
                + Integer.toString(slot) + ";"; // 셀렉트 SQL문
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst(); // result : tuple, first tuple's information

        cursor.getInt(cursor.getColumnIndex("MONEY"));
        cursor.getInt(cursor.getColumnIndex("PLAYTIME"));
        cursor.getInt(cursor.getColumnIndex("STOCK1"));
        cursor.getInt(cursor.getColumnIndex("STOCK2"));
        cursor.getInt(cursor.getColumnIndex("STOCK3"));
        cursor.getInt(cursor.getColumnIndex("STOCK4"));
        cursor.getInt(cursor.getColumnIndex("INGREDIENT1"));
        cursor.getInt(cursor.getColumnIndex("INGREDIENT2"));
        cursor.getInt(cursor.getColumnIndex("INGREDIENT3"));
        cursor.getInt(cursor.getColumnIndex("INGREDIENT4"));
        cursor.getInt(cursor.getColumnIndex("HEART"));

        return;

    } // 게임로드

    public void record(String nickname, int time) { // for rank
        ContentValues values = new ContentValues();
        System.out.println(nickname + " " + time);
        values.put("NUMBER", time);
        values.put("MONEY", 0);
        values.put("NICKNAME", nickname);
//        values.put("MONEY", money);
        values.put("PLAYTIME", time);
//        String[] whereArgs = new String[]{Integer.toString(slot)};
        db.insert(ranktable, null, values);
        return;
    } // game over : ranking

    public Cursor get_rank(String criterion) {

        String sql;
        criterion = pars.get(criterion);
        sql = "SELECT * FROM " + ranktable + " ORDER BY "
                + criterion + " DESC;"; // 셀렉트 SQL문
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    } // get my rank data

    public void close() {
        db.close();
    }

    public String getKey(String key){
        return pars.get(key);
    }
}