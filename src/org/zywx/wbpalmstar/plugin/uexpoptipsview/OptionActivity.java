package org.zywx.wbpalmstar.plugin.uexpoptipsview;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.EUExPopTipsView.OnPopItemSelectedListener;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopTipsBean;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopTipsBgLinearLayout;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopUtils;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.TriangleView;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OptionActivity extends Activity {

    private static final String TAG = "OptionActivity";
    private static final int TIP_BG_ID = 1;
    private static OnPopItemSelectedListener mListener;
    private RelativeLayout bgLayout;
    private String mBgColor;
    private int mRadius;
    private String[] mStringData;
    private ColorStateList mColorList;
    private String mTextColor;
    private String mHightlightTextColor;
    private String mDividerColor;
    private int mDividerWidth = 1;
    private int mTextSize;
    private int mTextPadding;
    private int mBorderMargin;
    private int mTriangleRadius;
    private static PopTipsBean mBean;
    private int mContentHeight;
    private int mCenterX;
    private int mCenterY;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if(mBean == null){
            mBean = new PopTipsBean();
        }
        mBgColor = mBean.getBgColor();
        mTextColor = mBean.getTextNColor();
        mHightlightTextColor = mBean.getTextHColor();
        mDividerColor = mBean.getDividerColor();
        mTextSize = mBean.getTextSize();
        mStringData = mBean.getLabels();
        mCenterX = mBean.getCenterX();
        mCenterY = mBean.getCenterY();
        mRadius = EUExUtil.dipToPixels(8);
        mTriangleRadius = EUExUtil.dipToPixels(12);
        mTextPadding = EUExUtil.dipToPixels(10);
        mBorderMargin = EUExUtil.dipToPixels(5);
        mContentHeight = EUExUtil.dipToPixels(50);
        mColorList = PopUtils.createColorStateList(
                Color.parseColor(mTextColor),
                Color.parseColor(mHightlightTextColor),
                Color.parseColor(mHightlightTextColor),
                Color.parseColor(mTextColor));
        setContentView(EUExUtil.getResLayoutID("plugin_uexpoptipsview_bg_layout"));
        bgLayout = (RelativeLayout) findViewById(
                EUExUtil.getResIdID("plugin_uexpoptipsview_bg_rl"));
        
        PopTipsBgLinearLayout tipsBg = new PopTipsBgLinearLayout(this, mBgColor, mRadius);
        tipsBg.setId(TIP_BG_ID);
        tipsBg.setOrientation(LinearLayout.HORIZONTAL);
        tipsBg.setGravity(Gravity.CENTER_HORIZONTAL);
        tipsBg.setPadding(0, mBorderMargin, 0, mBorderMargin);
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        int totalWidth = 0;
        int totalHeight = 0;
        totalHeight = mContentHeight + mTriangleRadius;
        int textH = getFontHeight(mTextSize);
        for (int i = 0; i < mStringData.length; i++) {
            final int index = i;
            TextView txt = new TextView(this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(mTextPadding, 0, mTextPadding, 0);
            txt.setTextColor(mColorList);
            txt.setTextSize(mTextSize);
            txt.setText(mStringData[i]);
            final float textWidth = txt.getPaint().measureText(mStringData[i]);
            Log.i(TAG, "textWidth"+ i +" = " + textWidth);
            totalWidth = totalWidth + (int)textWidth + 2 * mTextPadding + mStringData[i].length() * 3;
            txt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemSelected(index);
                    }
                }
            });
            txt.setLayoutParams(txtLp);
            tipsBg.addView(txt);
            
            if(i != mStringData.length - 1){
                TextView line = new TextView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mDividerWidth,
                        LayoutParams.MATCH_PARENT);
                line.setLayoutParams(lp);
                line.setBackgroundColor(Color.parseColor(mDividerColor));
                tipsBg.addView(line);
                totalWidth += mDividerWidth;
                //int textH1 = getTextViewHeight(line);
                //Log.i(TAG, "textH1 = " + textH1);
            }
        }
        RelativeLayout.LayoutParams tipsBgLp = new RelativeLayout.LayoutParams(
                totalWidth, mContentHeight);
        tipsBgLp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        bgLayout.addView(tipsBg, tipsBgLp);
        
        TriangleView triangle = new TriangleView(this, mBgColor, mTriangleRadius);
        RelativeLayout.LayoutParams triangleLp = new RelativeLayout.LayoutParams(
                2 * mTriangleRadius, mTriangleRadius);
        triangleLp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        triangleLp.addRule(RelativeLayout.BELOW, TIP_BG_ID);
        bgLayout.addView(triangle, triangleLp);
        RelativeLayout.LayoutParams bgLp = new RelativeLayout.LayoutParams(
                totalWidth, totalHeight);
        bgLp.leftMargin = mCenterX - totalWidth / 2;
        bgLp.topMargin = mCenterY - totalHeight;
        bgLayout.setLayoutParams(bgLp);
        
        Log.i(TAG, "mTextPadding = " + mTextPadding);
        Log.i(TAG, "totalWidth = " + totalWidth);
        Log.i(TAG, "totalHeight = " + totalHeight);
        Log.i(TAG, "textH = " + textH);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int width = bgLayout.getWidth();
        int height = bgLayout.getHeight();
        Log.i(TAG, width + " , " + height);
        super.onWindowFocusChanged(hasFocus);
    }

    public static void setOnPopSelectedListener(
            OnPopItemSelectedListener listener) {
        mListener = listener;
    }

    public static void setDataBean(PopTipsBean bean){
         mBean = bean;
    }

    public int getFontHeight(float fontSize)
    {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent) + 2;
    }
    
    private int getTextViewHeight(TextView pTextView) {
        Layout layout = pTextView.getLayout();
        int desired = layout.getLineTop(pTextView.getLineCount());
        int padding = pTextView.getCompoundPaddingTop() + pTextView.getCompoundPaddingBottom();
        return desired + padding;
    }

}
