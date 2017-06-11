package cn.ucai.live.data.local;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.live.utils.L;

/**
 * Created by Administrator on 2017/6/9.
 */

public class LiveDBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "LiveDBOpenHelper";

    private static int versionNumber = 1;
    private static LiveDBOpenHelper instance;

    private static final String GIFT_TABLE_CREATE = "CREATE TABLE "
            + LiveDao.GIFT_TABLE_NAME + " ("
            + LiveDao.GIFT_COLUMN_NAME + " TEXT, "
            + LiveDao.GIFT_COLUMN_URL + " TEXT, "
            + LiveDao.GIFT_COLUMN_PRICE + " INTEGER, "
            + LiveDao.GIFT_COLUMN_ID + " INTEGER PRIMARY KEY);";

    private static final String GET_USER_NICK_CREATE = "CREATE TABLE "
            + LiveDao.GET_USER_NICK_TABLE_NAME + " ("
            + LiveDao.USER_NICK + " TEXT, "
            + LiveDao.AVATAR_URL + " TEXT, "
            + LiveDao.USER_NAME + " TEXT PRIMARY KEY);";

    public LiveDBOpenHelper(Context context) {
        super(context, getDaseBaseNames(context), null, versionNumber);
    }

    private static String getDaseBaseNames(Context context) {
        return context.getPackageName()+".db";
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        L.e(TAG,"tablename,GIFT_TABLE_CREATE="+GIFT_TABLE_CREATE);
        L.e(TAG,"tablename,GET_USER_NICK_CREATE="+GET_USER_NICK_CREATE);
        db.execSQL(GIFT_TABLE_CREATE);
        db.execSQL(GET_USER_NICK_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static LiveDBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LiveDBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }
    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
