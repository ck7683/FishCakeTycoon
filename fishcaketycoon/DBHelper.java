package seungju.fishcaketycoon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Gideon on 2015-12-02.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VER = 7; // schema 수정시, 버전 올리기

    private static final String DB_FILE = "fishcaketycoon.db";
    private static final String USER_DB_SCHEMA =
            "CREATE TABLE `TB_USERDATA` ( " +
                    "\t`SLOT`\tINTEGER, " +
                    "\t`EXIST`\tINTEGER, " +
                    "\t`NICKNAME`\tTEXT, " +
                    "\t`LEVEL`\tINTEGER, " +
                    "\t`MONEY`\tINTEGER, " +
                    "\t`PLAYTIME`\tINTEGER, " +
                    "\t`STOCK1`\tINTEGER, " +
                    "\t`STOCK2`\tINTEGER, " +
                    "\t`STOCK3`\tINTEGER, " +
                    "\t`STOCK4`\tINTEGER, " +
                    "\t`INGREDIENT1`\tINTEGER, " +
                    "\t`INGREDIENT2`\tINTEGER, " +
                    "\t`INGREDIENT3`\tINTEGER, " +
                    "\t`INGREDIENT4`\tINTEGER, " +
                    "\t`HEART`\tINTEGER, " +
                    "\tPRIMARY KEY(SLOT) " +
                    ");"; // db schema

    private static final String RANK_DB_SCHEMA =
            "CREATE TABLE `TB_RANK` (\n" +
                    "\t`NUMBER`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`NICKNAME`\tTEXT,\n" +
                    "\t`MONEY`\tINTEGER,\n" +
                    "\t`PLAYTIME`\tINTEGER\n" +
                    ");"; // 랭크 DB 스키마

    private static final String DB_START =
            "INSERT INTO TB_USERDATA(SLOT)\n" +
                    "SELECT 1 UNION SELECT 2 UNION SELECT 3;";
    // // 캐릭터 슬롯 3개 생성

    public DBHelper(Context context) {
        super(context,DB_FILE,null,DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = USER_DB_SCHEMA;
        db.execSQL(sql);
        sql = RANK_DB_SCHEMA;
        db.execSQL(sql);
        sql = DB_START;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        String sql_droptable = "DROP TABLE IF EXISTS " + "TB_USERDATA;";
        db.execSQL(sql_droptable);
        sql_droptable = "DROP TABLE IF EXISTS " + "TB_RANK;";
        db.execSQL(sql_droptable);
        onCreate(db);
    }

}
