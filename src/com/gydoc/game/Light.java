package com.gydoc.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 *
 */
public class Light {

    private int x;
    private int y;
    private LightOffView game;
    public static final int RADIUS = 20;
    private boolean on;
    private int colorInt;

    public Light(int x, int y, LightOffView game, boolean on) {
        this.x = x;
        this.y = y;
        this.on = on;
        this.game = game;
        determineColor();
    }

    private void determineColor() {
        if (on) {
            colorInt = 0xFFFF0000;
        } else {
            colorInt = 0xFFFFFFFF;
        }
    }

    public void changeColor() {
        on = !on;
        determineColor();
    }

    public boolean isOff() {
        return !on;
    }

    public void draw(Canvas canvas, Paint paint) {
        int tx = game.getTopX();
        int ty = game.getTopY();
        paint.setColor(colorInt);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(x, y, RADIUS, paint);
    }

    public boolean consumeTouch(MotionEvent event) {
        if (containedPoint(event.getX(), event.getY())) {
            changeColor();
            return true;
        } else {
            return false;
        }
    }

    private boolean containedPoint(float x, float y) {
        float xw = x - this.x;
        float yw = y - this.y;
        if (RADIUS*RADIUS > xw*xw + yw*yw) {
            return true;
        } else {
            return false;
        }
    }

}
