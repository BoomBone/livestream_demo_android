package cn.ucai.live;

import com.hyphenate.easeui.model.EasePreferenceManager;

/**
 * Created by Administrator on 2017/6/8.
 */

public class LiveModel {
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
}
