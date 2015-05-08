package org.zywx.wbpalmstar.plugin.uexpoptipsview.util;

public class PopTipsBean {
    
    public static String TAG_CENTER_X = "centerX";
    public static String TAG_CENTER_Y = "centerY";
    public static String TAG_BG_COLOR = "bgColor";
    public static String TAG_TEXT_N_COLOR = "TextNColor";
    public static String TAG_TEXT_H_COLOR = "textHColor";
    public static String TAG_TEXT_SIZE = "textSize";
    public static String TAG_DIVIDER_COLOR = "dividerColor";
    public static String TAG_LABELS = "labels";

    private int centerX = 400;
    private int centerY = 400;
    private String bgColor = "#90000000";
    private String TextNColor = "#ffffff";
    private String textHColor = "#0000C6";
    private int textSize = 15;
    private String dividerColor = "#636363";
    private String[] labels = {"复制","粘贴","删除"};
    public int getCenterX() {
        return centerX;
    }
    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }
    public int getCenterY() {
        return centerY;
    }
    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
    public String getBgColor() {
        return bgColor;
    }
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
    public String getTextNColor() {
        return TextNColor;
    }
    public void setTextNColor(String textNColor) {
        TextNColor = textNColor;
    }
    public String getTextHColor() {
        return textHColor;
    }
    public void setTextHColor(String textHColor) {
        this.textHColor = textHColor;
    }
    public int getTextSize() {
        return textSize;
    }
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    public String getDividerColor() {
        return dividerColor;
    }
    public void setDividerColor(String dividerColor) {
        this.dividerColor = dividerColor;
    }
    public String[] getLabels() {
        return labels;
    }
    public void setLabels(String[] labels) {
        this.labels = labels;
    }

}
