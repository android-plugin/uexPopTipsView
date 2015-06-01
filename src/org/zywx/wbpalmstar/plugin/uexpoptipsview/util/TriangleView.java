package org.zywx.wbpalmstar.plugin.uexpoptipsview.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class TriangleView extends View {


    private Paint paint = new Paint();
    private Path path;
    private int mRadius;

    public TriangleView(Context context, int color, int radius) {
        super(context);
        mRadius = radius;
        this.paint.setAntiAlias(true);
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL);
        this.path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.path.moveTo(0.0F, 0.0F);
        this.path.lineTo(2 * mRadius, 0.0F);
        this.path.lineTo(mRadius, mRadius);
        this.path.close();
        canvas.drawPath(this.path, this.paint);
    }
}
