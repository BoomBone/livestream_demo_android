package cn.ucai.live.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.ucai.live.I;
import cn.ucai.live.LiveApplication;
import cn.ucai.live.data.model.Audient;
import cn.ucai.live.data.model.Gift;
import cn.ucai.live.utils.L;

/**
 * Created by Administrator on 2017/6/9.
 */

public class LiveDBManager {
    private static final String TAG = "LiveDBManager";
    static private LiveDBManager dbMgr = new LiveDBManager();
    private LiveDBOpenHelper dbHelper;

    private LiveDBManager() {
        dbHelper = LiveDBOpenHelper.getInstance(LiveApplication.getInstance().getApplicationContext());
    }

    public static synchronized LiveDBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new LiveDBManager();
        }
        return dbMgr;
    }

    /**
     * save contact list
     *
     * @param list
     */
    synchronized public void saveGiftList(List<Gift> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(LiveDao.GIFT_TABLE_NAME, null, null);
            for (Gift gift : list) {
                ContentValues values = new ContentValues();
                values.put(LiveDao.GIFT_COLUMN_ID, gift.getId());
                if (gift.getGname() != null)
                    values.put(LiveDao.GIFT_COLUMN_NAME, gift.getGname());
                if (gift.getGurl() != null)
                    values.put(LiveDao.GIFT_COLUMN_URL, gift.getGurl());
                if (gift.getGprice() != null)
                    values.put(LiveDao.GIFT_COLUMN_PRICE, gift.getGprice());
                db.replace(LiveDao.GIFT_TABLE_NAME, null, values);
            }
        }
    }

    /**
     * get contact list
     *
     * @return
     */
    synchronized public Map<Integer, Gift> getGiftList() {
        L.e(TAG, "getGiftList()");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<Integer, Gift> gifts = new Hashtable<Integer, Gift>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + LiveDao.GIFT_TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                int giftId = cursor.getInt(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_ID));
                String giftName = cursor.getString(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_NAME));
                String giftUrl = cursor.getString(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_URL));
                int giftPrice = cursor.getInt(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_PRICE));
                Gift gift = new Gift();
                gift.setId(giftId);
                gift.setGname(giftName);
                gift.setGurl(giftUrl);
                gift.setGprice(giftPrice);
                gifts.put(giftId, gift);
            }
            cursor.close();
        }
        L.e(TAG, "gift=" + gifts);
        return gifts;
    }

    synchronized public void saveAudient(Audient audient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(LiveDao.GET_USER_NICK_TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            values.put(LiveDao.USER_NAME, audient.getUsername());
            if (audient.getNickname() != null) {
                values.put(LiveDao.USER_NICK, audient.getNickname());
            }
            values.put(LiveDao.AVATAR_URL, System.currentTimeMillis());

            db.replace(LiveDao.GET_USER_NICK_TABLE_NAME, null, values);
            //insert into students (stu_id,stu_name,stu_birthday,stu_phone,stu_age,stu_address) values (1004,'吴小飞','1993-08-06','18888666666',24,'北京市北京优才')
            //插入

        }
    }

    synchronized public String getAudient(String username) {
        L.e(TAG, "getGiftList()");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String userNick = null;
        if (db.isOpen()) {
            String sql = "select " + LiveDao.USER_NICK + " from " + LiveDao.GET_USER_NICK_TABLE_NAME + " where " + LiveDao.USER_NAME + " = " + username;
            L.e(TAG,"sql="+sql);
            Cursor cursor = db.rawQuery(sql, null);
            userNick = cursor.getString(cursor.getColumnIndex(LiveDao.USER_NICK));
            cursor.close();
        }
        return userNick;
    }
}
