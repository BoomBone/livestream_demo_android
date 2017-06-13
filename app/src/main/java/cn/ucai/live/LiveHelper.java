package cn.ucai.live;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.model.EasePreferenceManager;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.ucai.live.data.local.LiveDBManager;
import cn.ucai.live.data.local.LiveDao;
import cn.ucai.live.data.model.Gift;
import cn.ucai.live.data.restapi.LiveException;
import cn.ucai.live.data.restapi.LiveManager;
import cn.ucai.live.ui.activity.MainActivity;
import cn.ucai.live.utils.L;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

/**
 * Created by Administrator on 2017/6/8.
 */

public class LiveHelper {
    private static final String TAG = "LiveHelper";

    private static LiveHelper instance = null;
    private LiveModel liveModel = null;
    private String username;
    private Context appContext;
    private User currentAppUser = null;
    private Map<Integer, Gift> giftMap;
    private EaseUI easeUI;
    private Map<String, User> appContactList;
    private List<Gift> giftList;

    public LiveHelper() {
    }

    public synchronized static LiveHelper getInstance() {
        if (instance == null) {
            instance = new LiveHelper();
        }
        return instance;
    }

    /*-----------------------应用启动时执行的init方法-----------------------------*/
    public void init(Context context) {
        liveModel = new LiveModel();
        appContext = context;
        EaseUI.getInstance().init(context, null);
        easeUI = EaseUI.getInstance();
        setEaseUIProviders();

        EMClient.getInstance().setDebugMode(true);

        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    onUserException(LiveConstants.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onUserException(LiveConstants.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    onUserException(LiveConstants.ACCOUNT_FORBIDDEN);
                }
            }
        });
        getGiftListFromServer();
    }

    private void setEaseUIProviders() {
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }

            @Override
            public User getAppUser(String username) {
                return getAppUserInfo(username);
            }
        });
    }

    //    private User getAppUserInfo(String username) {
//        User user = null;
//        if (username == EMClient.getInstance().getCurrentUser()) {
//            return getCurrentAppUserInfo();
//        }
//        if (user == null) {
//            user = new User(username);
//        }
//        return user;
//    }
    private User getAppUserInfo(String username) {
        User user = null;
        if (username.equals(EMClient.getInstance().getCurrentUser()))
            return getCurrentAppUserInfo();
        user = getAppContactList().get(username);
        if (user == null) {
            user = new User(username);
        }
        return user;
    }


    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception) {
        EMLog.e(TAG, "onUserException: " + exception);
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(exception, true);
        appContext.startActivity(intent);
    }
    /*--------------------------------------------------------------------------*/


    /*---------------------------异步加载用户数据-------------------------------------*/
    public void syncUserInfo(final String username) {
        L.e(TAG, "syncUserInfo");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    User user = LiveManager.getInstance().loadUserInfo(username);
                    if (user != null) {
                        setCurrentAppUserNick(user.getMUserNick());
                        setCurrentAppUserAvatar(user.getAvatar());
                    }
                } catch (LiveException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /*--------------创建了一个空User并通过SharePreference给他赋值-----------------*/
    public synchronized User getCurrentAppUserInfo() {
        L.e(TAG, "getCurrentAppUserInfo()");
        if (currentAppUser == null) {
            String username = EMClient.getInstance().getCurrentUser();
            currentAppUser = new User(username);
            String nick = getCurrentUserNick();
            currentAppUser.setMUserNick((nick != null) ? nick : username);
            currentAppUser.setAvatar(getCurrentUserAvatar());
        }
        return currentAppUser;
    }

    /*--------------------get set 方法-------------------------------------*/
    private void setCurrentAppUserNick(String nickname) {
        getCurrentAppUserInfo().setMUserNick(nickname);
        EasePreferenceManager.getInstance().setCurrentUserNick(nickname);
    }

    private String getCurrentUserNick() {
        return EasePreferenceManager.getInstance().getCurrentUserNick();
    }

    private void setCurrentAppUserAvatar(String avatar) {
        getCurrentAppUserInfo().setAvatar(avatar);
        EasePreferenceManager.getInstance().setCurrentUserAvatar(avatar);
    }

    private String getCurrentUserAvatar() {
        return EasePreferenceManager.getInstance().getCurrentUserAvatar();
    }


    public void setCurrentUserName(String username) {
        this.username = username;
        liveModel.setCurrentUserName(username);
    }


    public String getCurrentUsernName() {
        if (username == null) {
            username = liveModel.getCurrentUsernName();
        }
        return username;
    }
    /*----------------------当登出后清空内存和SharePreference--------------------*/

    public synchronized void reset() {
        currentAppUser = null;
        EasePreferenceManager.getInstance().removeCurrentUserInfo();
    }
    /*-----------------------------------------------------------------------*/


    /*---------------------set get礼物列表----------------------------------------*/
    public void setGiftList(Map<Integer, Gift> list) {
        L.e(TAG, "save data to cache");
        if (list == null) {
            if (giftMap != null) {
                giftMap.clear();
            }
            return;
        }

        giftMap = list;
    }

    public Map<Integer, Gift> getGiftList() {
        L.e(TAG, "getGiftList()");
        if (giftMap == null) {
            giftMap = liveModel.getGiftList();
        }

        // return a empty non-null object to avoid app crash
        if (giftMap == null) {
            giftMap = new HashMap<>();
            return new HashMap<Integer, Gift>();
        }

        return giftMap;
    }

    /*-----------------------获取礼物列表----------------------------------------*/
    public List<Gift> getGiftLists(){
        if(giftList==null){
            giftList = new ArrayList<>();
            //数据库有礼物,遍历
            if(getGiftList().size()>0){
                Iterator<Map.Entry<Integer, Gift>> iterator = giftMap.entrySet().iterator();
                while (iterator.hasNext()){
                    giftList.add(iterator.next().getValue());
                }
                //排序
                Collections.sort(giftList, new Comparator<Gift>() {
                    @Override
                    public int compare(Gift o1, Gift o2) {
                        return o1.getGprice()-o2.getGprice();
                    }
                });
            }
        }

        return giftList;
    }
    /*----------------------从服务器获取礼物列表-----------------------------------------*/
    public void getGiftListFromServer() {
            L.e(TAG, "getGiftListFromServer()");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    L.e(TAG, "getGiftListFromServer()..." + getGiftList().size());
                    if (getGiftList().size() == 0) {
                        try {
                            List<Gift> list = LiveManager.getInstance().loadGiftList();
                            L.e(TAG, "list=" + list);
                            if (list != null) {
                                Map<Integer, Gift> map = new HashMap<Integer, Gift>();
                                for (Gift gift : list) {
                                    map.put(gift.getId(), gift);
                                }
                                //save data to cache
                                setGiftList(map);
                                //save data to database
                                LiveDao dao = new LiveDao();
                                dao.setGiftList(list);
                            }
                        } catch (LiveException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

    }

    /*---------------------保存到内存----------------------------*/
    public void saveAppContact(User user) {
        L.e(TAG, "saveAppContact,user1=" + user);
        getAppContactList().put(user.getMUserName(), user);
        L.e(TAG, "saveAppContact,user2=" + getAppContactList().get(user.getMUserName()));
    }

    /*-----------------从内存中获取昵称-------------------------------*/
    public Map<String, User> getAppContactList() {
        if (appContactList == null) {
            appContactList = new HashMap<String, User>();
        }
        return appContactList;
    }
}
