package cn.ucai.live.data.local;

import java.util.List;
import java.util.Map;

import cn.ucai.live.data.model.Gift;
import cn.ucai.live.data.restapi.LiveManager;
import cn.ucai.live.utils.L;

/**
 * Created by Administrator on 2017/6/9.
 */

public class LiveDao {
    private static final String TAG = "LiveDao";
    public static final String GIFT_TABLE_NAME = "live_gift";
    public static final String GIFT_COLUMN_ID = "gift_id";
    public static final String GIFT_COLUMN_NAME = "gift_name";
    public static final String GIFT_COLUMN_URL = "gift_url";
    public static final String GIFT_COLUMN_PRICE = "gitf_price";

    public void setGiftList(List<Gift> list){
        LiveDBManager.getInstance().saveGiftList(list);
        L.e(TAG,"save data to database");
    }
    public Map<Integer, Gift> getGiftList(){
        L.e(TAG,"getGiftList()");
        return LiveDBManager.getInstance().getGiftList();
    }
}
