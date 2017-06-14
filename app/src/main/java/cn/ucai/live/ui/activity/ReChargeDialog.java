package cn.ucai.live.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.live.R;
import cn.ucai.live.data.restapi.LiveException;
import cn.ucai.live.data.restapi.LiveManager;

/**
 * Created by wei on 2017/3/3.
 */

public class ReChargeDialog extends DialogFragment {


    @BindView(R.id.balance)
    TextView mBalance;
    Unbinder unbinder;
    int balence = 0;

    public void setOnClickListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    View.OnClickListener mOnClickListener;

    public ReChargeDialog() {
    }


    public static ReChargeDialog newInstance() {
        ReChargeDialog dialog = new ReChargeDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recharge_gift_list, container, false);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDate();
        initView(balence);
    }

    private void initView(int balance) {
        mBalance.setText(String.valueOf(balance));
    }

    private void initDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final int balance = LiveManager.getInstance().getBalance(EMClient.getInstance().getCurrentUser());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView(balance);
                        }
                    });
                } catch (LiveException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder.unbind();
        }
    }
}
