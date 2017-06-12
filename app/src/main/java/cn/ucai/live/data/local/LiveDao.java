package cn.ucai.live.data.local;

import java.util.List;
import java.util.Map;

import cn.ucai.live.data.model.Audient;
import cn.ucai.live.data.model.Gift;
import cn.ucai.live.data.restapi.LiveManager;
import cn.ucai.live.utils.L;

/**
 * Created by Administrator on 2017/6/9.
 */

public class LiveDao {
    private static final String TAG = "LiveDao";
    /*---------------------礼物的数据库--------------------------*/

    public static final String GIFT_TABLE_NAME = "live_gift";
    public static final String GIFT_COLUMN_ID = "gift_id";
    public static final String GIFT_COLUMN_NAME = "gift_name";
    public static final String GIFT_COLUMN_URL = "gift_url";
    public static final String GIFT_COLUMN_PRICE = "gitf_price";

    /*---------------------观众的数据库-----------------------------*/
    public static final String GET_USER_NICK_TABLE_NAME = "get_user_nick";
    public static final String USER_NAME = "user_name";
    public static final String USER_NICK = "user_nick";
    public static final String AVATAR_URL = "user_avatar_url";


    public void setGiftList(List<Gift> list){
        LiveDBManager.getInstance().saveGiftList(list);
        L.e(TAG,"save data to database");
    }
    public Map<Integer, Gift> getGiftList(){
        L.e(TAG,"getGiftList()");
        return LiveDBManager.getInstance().getGiftList();
    }

    public void setAudient(Audient audient) {
        LiveDBManager.getInstance().saveAudient(audient);
    }

}
