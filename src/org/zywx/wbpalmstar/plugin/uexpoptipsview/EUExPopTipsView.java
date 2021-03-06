package org.zywx.wbpalmstar.plugin.uexpoptipsview;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopTipsBean;

public class EUExPopTipsView extends EUExBase {

    private static final String DATA_PARAMS = "data_params";
    private static final String CALLBACK_ON_SELECTED = "uexPopTipsView.onItemSelected";
    private static final int MSG_OPEN = 0;
    private static final int MSG_CLOSE = 1;
    private static final String RESULT_INDEX = "index";
    private Context mContext;
    private OnPopItemSelectedListener mListener;
    private static boolean mIsOpen = false;
    private OptionView popMenuView;

    public EUExPopTipsView(Context context, EBrowserView view) {
        super(context, view);
        mContext = context;
        mListener = new OnPopItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                JSONObject json = new JSONObject();
                try {
                    json.put(RESULT_INDEX, index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBackPluginJs(CALLBACK_ON_SELECTED, json.toString());
            }
        };
    }
    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
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
        PopTipsBean dataVO = DataHelper.gson.fromJson(jsonData, PopTipsBean.class);
        if (dataVO == null) return;
        popMenuView = new OptionView(mContext, dataVO, mListener);
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
            if (popMenuView != null) {
                removeViewFromCurrentWindow(popMenuView);
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

    public interface OnPopItemSelectedListener{
        public void onItemSelected(int index);
    }
}
