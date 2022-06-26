package com.mango.convexhull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PointView extends View {

    Paint paint= new Paint();
    PointF pointA;
    int color;

    public void setColor(int color) {
        this.color = color;
    }

    public PointView(Context context) {
        super(context);
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setPointA(PointF pointA) {
        this.pointA = pointA;
    }


    @SuppressLint("Range")
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(color==1?Color.BLUE:Color.YELLOW);
        paint.setStrokeWidth(20);
//        canvas.drawLine(pointA.x,pointA.y,pointB.x,pointB.y,paint);
        canvas.drawCircle(pointA.x,pointA.y,color==1?30:15,paint);
        super.onDraw(canvas);
    }

    public void draw(){
        invalidate();
        requestLayout();
    }
}
