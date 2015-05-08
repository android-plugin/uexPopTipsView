package org.zywx.wbpalmstar.plugin.uexpoptipsview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopTipsBean;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class EUExPopTipsView extends EUExBase {

    private static final String DATA_PARAMS = "data_params";
    private static final String CALLBACK_ONSELECTED = "uexPopTipsView.onItemSelected";
    private static final int MSG_OPEN = 0;
    private static final int MSG_CLOSE = 1;
    private static final String TAG_OPTIONACTIVITY = "OptionActivity";
    private Context mContext;
    public static LocalActivityManager mgr;
    private OnPopItemSelectedListener mListener;
    private PopTipsBean mBean;
    private static boolean mIsOpen = false;

    public EUExPopTipsView(Context context, EBrowserView view) {
        super(context, view);
        mContext = context;
        mgr = ((ActivityGroup) mContext)
                .getLocalActivityManager();
        mListener = new OnPopItemSelectedListener() {
            
            @Override
            public void onItemSelected(int index) {
                jsCallback(CALLBACK_ONSELECTED, 0, EUExCallback.F_C_INT, index);
            }
        };
    }

    @Override
    protected boolean clean() {
        close(null);
        return false;
    }

    public void open(String[] params){
        if(params == null || params.length == 0){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.what = MSG_OPEN;
        msg.obj = this;
        Bundle bd = new Bundle();
        bd.putStringArray(DATA_PARAMS, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openMsg(String[] params) {
        if(mIsOpen){
            closeMsg();
        }
        String jsonData = params[0];
        parsePopTipsData(jsonData);
        Intent intent = new Intent();
        intent.setClass(mContext, OptionActivity.class);
        OptionActivity.setOnPopSelectedListener(mListener);
        OptionActivity.setDataBean(mBean);
        Window window = mgr.startActivity(TAG_OPTIONACTIVITY, intent);
        View popMenuView = window.getDecorView();
        popMenuView.requestFocus();
        popMenuView.setFocusable(true);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.leftMargin = 0;
        lp.topMargin = 0;
        addView2CurrentWindow(popMenuView, lp);
    }

    public void close(String[] params){
        Message msg = new Message();
        msg.what = MSG_CLOSE;
        msg.obj = this;
        mHandler.sendMessage(msg);
    }
    
    private void closeMsg(){
        if (mIsOpen) {
            Window window = mgr.destroyActivity(TAG_OPTIONACTIVITY, true);
            if (window != null) {
                View view = window.getDecorView();
                removeViewFromCurrentWindow(view);
            }
            mIsOpen = false;
        }
    }
    
    private void addView2CurrentWindow(View child,
            RelativeLayout.LayoutParams parms) {
        int l = (int) (parms.leftMargin);
        int t = (int) (parms.topMargin);
        int w = parms.width;
        int h = parms.height;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
        lp.gravity = Gravity.NO_GRAVITY;
        lp.leftMargin = l;
        lp.topMargin = t;
        adptLayoutParams(parms, lp);
        mBrwView.addViewToCurrentWindow(child, lp);
        mIsOpen = true;
    }

    @Override
    public void onHandleMessage(Message msg) {
        if(msg == null){
            return;
        }
        String[] params = msg.getData().getStringArray(DATA_PARAMS);
        switch (msg.what) {
        case MSG_OPEN:
            if(params == null){
                return;
            }
            openMsg(params);
            break;
        case MSG_CLOSE:
            closeMsg();
            break;
        default:
            break;
        }
    }

    private void parsePopTipsData(String jsonData) {
        try {
            mBean = new PopTipsBean();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray(PopTipsBean.TAG_LABELS);
            String[] labels = new String[jsonArray.length()];
            for (int i = 0; i < labels.length; i++) {
                labels[i] = jsonArray.optString(i);
            }
            mBean.setLabels(labels);
            if(jsonObject.has(PopTipsBean.TAG_BG_COLOR)){
                mBean.setBgColor(jsonObject.getString(PopTipsBean.TAG_BG_COLOR));
            }
            if(jsonObject.has(PopTipsBean.TAG_CENTER_X)){
                mBean.setCenterX(jsonObject.getInt(PopTipsBean.TAG_CENTER_X));
            }
            if(jsonObject.has(PopTipsBean.TAG_CENTER_Y)){
                mBean.setCenterY(jsonObject.getInt(PopTipsBean.TAG_CENTER_Y));
            }
            if(jsonObject.has(PopTipsBean.TAG_DIVIDER_COLOR)){
                mBean.setDividerColor(jsonObject.getString(PopTipsBean.TAG_DIVIDER_COLOR));
            }
            if(jsonObject.has(PopTipsBean.TAG_TEXT_H_COLOR)){
                mBean.setTextHColor(jsonObject.getString(PopTipsBean.TAG_TEXT_H_COLOR));
            }
            if(jsonObject.has(PopTipsBean.TAG_TEXT_N_COLOR)){
                mBean.setTextNColor(jsonObject.getString(PopTipsBean.TAG_TEXT_N_COLOR));
            }
            if(jsonObject.has(PopTipsBean.TAG_TEXT_SIZE)){
                mBean.setTextSize(jsonObject.getInt(PopTipsBean.TAG_TEXT_SIZE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            errorCallback(0, 0, "error params!");
        }
        
    }

    public interface OnPopItemSelectedListener{
        public void onItemSelected(int index);
    }
}
