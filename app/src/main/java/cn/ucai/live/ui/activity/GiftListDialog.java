package cn.ucai.live.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseImageView;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.live.I;
import cn.ucai.live.LiveHelper;
import cn.ucai.live.R;
import cn.ucai.live.data.model.Gift;

/**
 * Created by wei on 2017/3/3.
 */

public class GiftListDialog extends DialogFragment {

    @BindView(R.id.rv_gift)
    RecyclerView rvGift;
    @BindView(R.id.tv_my_bill)
    TextView tvMyBill;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    Unbinder unbinder;
    GiftAdapter adapter;
    List<Gift> list;

    public void setOnClickListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    View.OnClickListener mOnClickListener;

    public GiftListDialog() {
    }

//    public RoomUserManagementDialog(String chatroomId) {
//        this.chatroomId = chatroomId;
//    }

    public static GiftListDialog newInstance() {
        GiftListDialog dialog = new GiftListDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_list, container, false);
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
        list = LiveHelper.getInstance().getGiftLists();
        if (list.size() > 0) {
            initView();
            initData(list);
        } else {
            //download gift list data
            LiveHelper.getInstance().getGiftListFromServer();
        }


    }

    private void initView() {
        GridLayoutManager gm = new GridLayoutManager(getContext(), I.GIFT_COLUMN_COUNT);
        rvGift.setLayoutManager(gm);
    }

    private void initData(List<Gift> list) {
        adapter = new GiftAdapter(list, getContext());
        rvGift.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    class GiftAdapter extends RecyclerView.Adapter<GiftHolder> {
        List<Gift> giftList;
        Context context;

        public GiftAdapter(List<Gift> giftList, Context context) {
            this.giftList = giftList;
            this.context = context;
        }

        @Override
        public GiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final GiftHolder holder = new GiftHolder(View.inflate(context, R.layout.item_gift, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(GiftHolder holder, int position) {
            Gift gift = giftList.get(position);
            if (gift != null) {
                holder.tvGiftName.setText(gift.getGname());
                holder.tvGiftPrice.setText("¥" + gift.getGprice());
                EaseUserUtils.setAppGift(context, gift.getGurl(), holder.ivGiftThumb);
                holder.itemView.setTag(gift.getId());
                holder.itemView.setOnClickListener(mOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return giftList.size();
        }
    }

    static class GiftHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGiftThumb)
        ImageView ivGiftThumb;
        @BindView(R.id.tvGiftName)
        TextView tvGiftName;
        @BindView(R.id.tvGiftPrice)
        TextView tvGiftPrice;
        @BindView(R.id.layout_gift)
        LinearLayout layoutGift;

        GiftHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
