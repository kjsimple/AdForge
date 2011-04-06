package com.gydoc.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.*;

/**
 *
 */
public class LightOffView extends View {

    public static final int MODE_NEW = 0;
    public static final int MODE_RUNNING = 1;
    private static final long DELAY = 50;
    private RefreshHandler refreshHandler = new RefreshHandler();
    private int mode;
    private Random random = new Random();
    private static final int TOP_X = 100;
    private static final int TOP_Y = 100;
    private static final int ROWS = 5;
    private static final int COLS = 5;
    private Map<Integer, Boolean> status;
    private Light[][] lights = new Light[ROWS][COLS];

    public int getTopX() {
        return TOP_X;
    }

    public int getTopY() {
        return TOP_Y;
    }

//    private

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LightOffView.this.update();
            LightOffView.this.invalidate();
        }

        public void sleep(long delay) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delay);
        }
    }

    private void update() {
        if (mode == MODE_RUNNING) {
            refreshHandler.sleep(DELAY);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Light light = lights[i][j];
                if (light != null) {
                    light.draw(canvas, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode == MODE_NEW) {
            mode = MODE_RUNNING;
            initGame();
            update();
        } else {
            boolean consumed = false;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    consumed = lights[i][j].consumeTouch(event);
                    if (consumed) {
                        changeOthers(i, j);
                        break ;
                    }
                }
                if (consumed) {
                    break ;
                }
            }
            boolean finish = true;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (!lights[i][j].isOff()) {
                        finish = false;
                    }
                }
            }
            update();
            if (finish) {
                mode = MODE_NEW;
            }
        }
        return super.onTouchEvent(event);
    }

    private void changeOthers(int row, int col) {
        if (row - 1 >= 0) {
            lights[row-1][col].changeColor();
        }
        if (col - 1 >= 0) {
            lights[row][col-1].changeColor();
        }
        if (col + 1 < COLS) {
            lights[row][col+1].changeColor();
        }
        if (row + 1 < ROWS) {
            lights[row+1][col].changeColor();
        }
    }

    private void initGame() {
        status = new HashMap();
        int y = TOP_Y;
        initOnOff();
        for (int i = 0; i < ROWS; i++) {
            int x = TOP_X;
            for (int j = 0; j < COLS; j++) {
                lights[i][j] = new Light(x, y, this, status.get(i*ROWS+j));
                x = x + 2*Light.RADIUS + 5;
            }
            y = y + 2*Light.RADIUS + 5;
        }
    }

    private void initOnOff() {
        int total = random.nextInt(10);
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < COLS*ROWS; i++) {
            data.add(i);
            status.put(i, false);
        }
        Random r = new Random();
        for (int i = 0; i < total; i++) {
            int index = r.nextInt(data.size());
            status.put(data.get(index), true);
            data.remove(index);
        }
    }

    public LightOffView(Context context) {
        super(context);
    }

    public LightOffView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightOffView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
