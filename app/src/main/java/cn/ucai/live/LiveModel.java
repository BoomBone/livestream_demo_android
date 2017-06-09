package cn.ucai.live;

import com.hyphenate.easeui.model.EasePreferenceManager;

import java.util.Map;

import cn.ucai.live.data.local.LiveDao;
import cn.ucai.live.data.model.Gift;
import cn.ucai.live.utils.L;

/**
 * Created by Administrator on 2017/6/8.
 */

public class LiveModel {
    private static final String TAG = "LiveModel";
    public LiveModel() {
    }

    /**
     * set current username
     * @param username
     */
    public  void setCurrentUserName(String username){
        EasePreferenceManager.getInstance().setCurrentUserName(username);
    }

    /**
     * get current user's id
     */
    public  String getCurrentUsernName(){
        return EasePreferenceManager.getInstance().getCurrentUsername();
    }

    public Map<Integer, Gift> getGiftList() {
        L.e(TAG,"getGiftList()");
        LiveDao dao = new LiveDao();
        return dao.getGiftList();

    }
}
