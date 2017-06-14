package cn.ucai.live.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.live.LiveHelper;
import cn.ucai.live.R;
import cn.ucai.live.data.restapi.LiveException;
import cn.ucai.live.data.restapi.LiveManager;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/6/14.
 */

public class MyProfileActivity extends BaseActivity {
    @BindView(R.id.blance)
    TextView mBlance;
    int balance = 0;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_user_avatar)
    CircleImageView mIvUserAvatar;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.recharge)
    TextView mRecharge;


    private IntentFilter filter;
    private ReChargeChangeReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);
        ButterKnife.bind(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        filter = new IntentFilter();
        filter.addAction("balance change");
        receiver = new ReChargeChangeReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final int balance = LiveManager.getInstance().getBalance(EMClient.getInstance().getCurrentUser());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBlance.setText(String.valueOf(balance));
                        }
                    });
                } catch (LiveException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        EaseUserUtils.setAppUserAvatar(this, EMClient.getInstance().getCurrentUser(), mIvUserAvatar);
        EaseUserUtils.setAppUserNick(EMClient.getInstance().getCurrentUser(),mTvUsername);
    }

    @OnClick(R.id.recharge)
    public void onReChargeClicked() {
        final ReChargeDialog reCharge = ReChargeDialog.newInstance();
        reCharge.show(getSupportFragmentManager(), "ReChargeDialog");
    }


    @OnClick(R.id.btn_logout)
    public void onlogoutClicked() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                LiveHelper.getInstance().reset();
                finish();
                startActivity(new Intent(MyProfileActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
    class ReChargeChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            initView();
        }
    }
}
