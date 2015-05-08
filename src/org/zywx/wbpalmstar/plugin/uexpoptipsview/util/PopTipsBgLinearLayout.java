package org.zywx.wbpalmstar.plugin.uexpoptipsview.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.widget.LinearLayout;

public class PopTipsBgLinearLayout extends LinearLayout {
    private ShapeDrawable bgDrawable;
    private Paint paint;

    public PopTipsBgLinearLayout(Context context, String color, int radius) {
        super(context);
        setWillNotDraw(false);
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(20.0F);
        float[] arrayOfFloat = new float[8];
        arrayOfFloat[0] = radius;
        arrayOfFloat[1] = radius;
        arrayOfFloat[2] = radius;
        arrayOfFloat[3] = radius;
        arrayOfFloat[4] = radius;
        arrayOfFloat[5] = radius;
        arrayOfFloat[6] = radius;
        arrayOfFloat[7] = radius;
        this.bgDrawable = new ShapeDrawable(new RoundRectShape(arrayOfFloat, null, null));
        this.bgDrawable.getPaint().setColor(Color.parseColor(color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.bgDrawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
        this.bgDrawable.draw(canvas);
        super.onDraw(canvas);
    }
}
