package org.zywx.wbpalmstar.plugin.uexpoptipsview;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.EUExPopTipsView.OnPopItemSelectedListener;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopTipsBean;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopTipsBgLinearLayout;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.PopUtils;
import org.zywx.wbpalmstar.plugin.uexpoptipsview.util.TriangleView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OptionView extends FrameLayout {

    private static final int TIP_BG_ID = 1;
    private static final int DIVIDE_WIDTH = 1;
    private OnPopItemSelectedListener mListener;
    private PopTipsBean mBean;
    private Context mContext;

    public OptionView(Context context, PopTipsBean bean,
                      OnPopItemSelectedListener listener) {
        super(context);
        this.mContext = context;
        this.mBean = bean;
        this.mListener = listener;
        initView();
    }


    private void initView() {
        if(mBean == null){
            mBean = new PopTipsBean();
        }
        int mBgColor = mBean.getBgColor();
        int mTextColor = mBean.getTextNColor();
        int mHighlightTextColor = mBean.getTextHColor();
        int mDividerColor = mBean.getDividerColor();
        int mTextSize = mBean.getTextSize();
        String[] mStringData = mBean.getLabels();
        int mCenterX = mBean.getCenterX();
        int mCenterY = mBean.getCenterY();
        int mRadius = EUExUtil.dipToPixels(8);
        int mTriangleRadius = EUExUtil.dipToPixels(12);
        int mTextPadding = EUExUtil.dipToPixels(10);
        int mBorderMargin = EUExUtil.dipToPixels(5);
        int mContentHeight = EUExUtil.dipToPixels(50);
        ColorStateList mColorList = PopUtils.createColorStateList(
                mTextColor,
                mHighlightTextColor,
                mHighlightTextColor,
                mTextColor);
        LayoutInflater.from(mContext).inflate(
                EUExUtil.getResLayoutID("plugin_uexpoptipsview_bg_layout"),
                this, true);
        RelativeLayout bgLayout = (RelativeLayout) findViewById(
                EUExUtil.getResIdID("plugin_uexpoptipsview_bg_rl"));
        
        PopTipsBgLinearLayout tipsBg = new PopTipsBgLinearLayout(mContext, mBgColor, mRadius);
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
            TextView txt = new TextView(mContext);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(mTextPadding, 0, mTextPadding, 0);
            txt.setTextColor(mColorList);
            txt.setTextSize(mTextSize);
            txt.setText(mStringData[i]);
            final float textWidth = txt.getPaint().measureText(mStringData[i]);
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
                TextView line = new TextView(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DIVIDE_WIDTH,
                        LayoutParams.MATCH_PARENT);
                line.setLayoutParams(lp);
                line.setBackgroundColor(mDividerColor);
                tipsBg.addView(line);
                totalWidth += DIVIDE_WIDTH;
            }
        }
        RelativeLayout.LayoutParams tipsBgLp = new RelativeLayout.LayoutParams(
                totalWidth, mContentHeight);
        tipsBgLp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        bgLayout.addView(tipsBg, tipsBgLp);
        
        TriangleView triangle = new TriangleView(mContext, mBgColor, mTriangleRadius);
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
    }

    public int getFontHeight(float fontSize)
    {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent) + 2;
    }

}
