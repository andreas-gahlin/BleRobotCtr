package com.example.andre.robotprojekt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class handels the calculation for the Joystick view and send the commands it produce
 * to the bluetooth device.
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class CustomSurfaceView extends View implements View.OnTouchListener {
    protected Paint paint;
    protected Paint Max;
    protected float x,y, Ox, Oy, dx, dy, angle, radius;
    private double c;
    private Thread thread;
    private boolean isThread = true;
    /**************************************************************************
     * FUNCTION NAME : CustomSurfaceView
     * DESCRIPTION   : Everytime this windows start we initilaze this window
     * NOTE          :
     **************************************************************************/
    public CustomSurfaceView(Context context) {
        super(context);
        paint = new Paint(); /* Paint makes the circle that user drags */
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setStyle(Paint.Style.FILL);
        Max = new Paint(); /* Max is the max circle of radius 200 */
        Max.setColor(Color.GRAY);
        Max.setStyle(Paint.Style.FILL);
        setOnTouchListener(this);
        radius = 200;

        thread = new Thread() {
            @Override
            public void run() {
                while(isThread){
                    try {
                        Thread.sleep(150); /* Makes thread sleep for not overflow the H/W */
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isThread)
                        stopThread();
                    if(BluetoothActivity.isBluetoothReady){
                        if(angle >= 247 && angle < 292 ) { /* STICK_UP */
                            BluetoothActivity.sendDataToBluetooth((byte) 3, (byte) ((int)c / 2), (byte) ((int)c / 2));
                        }
                        else if(angle >= 292 && angle < 337 ) { /* STICK_UPRIGHT */
                            if(angle >= 292 && angle < 315){
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = (c - speedRight);
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)(speedLeft/2), (byte) (int)((speedRight/2) + 10));
                            }
                            if(angle >= 315 && angle < 337){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)(speedLeft/2), (byte) (int)(speedRight/2));
                            }
                        }
                        else if(angle >= 337 || angle < 22 ) { /* STICK_RIGHT; */
                            BluetoothActivity.sendDataToBluetooth((byte) 1, (byte) ((int)c / 3), (byte) ((int)c / 3));
                        }
                        else if(angle >= 22 && angle < 67 ) { /* STICK_DOWNRIGHT; */
                            if(angle >= 22 && angle < 45){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)0, (byte) (int)(speedRight/2), (byte) (int)(speedLeft/2));
                            }
                            if(angle >= 45 && angle < 67){
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)0, (byte) (int)((speedRight/2) + 10), (byte) (int)(speedLeft/2));
                            }
                        }
                        else if(angle >= 67 && angle < 112 ) { /* STICK_DOWN; */
                            BluetoothActivity.sendDataToBluetooth((byte) 0, (byte) ((int)c / 2), (byte) ((int)c / 2));
                        }
                        else if(angle >= 112 && angle < 157 ) { /* STICK_DOWNLEFT; */
                            if(angle >= 112 && angle < 135){
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)0, (byte) (int)(speedLeft/2), (byte) (int)((speedRight/2) + 10));
                            }
                            if(angle >= 135 && angle < 157){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte) 0, (byte) (int) (speedLeft / 2), (byte) (int) (speedRight / 2));
                            }
                        }
                        else if(angle >= 157 && angle < 202 ) { /* STICK_LEFT; */
                            BluetoothActivity.sendDataToBluetooth((byte) 2, (byte) ((int)c / 3), (byte) ((int)c / 3));
                        }
                        else if(angle >= 202 && angle < 247 ) { /* STICK_UPLEFT */
                            if(angle >= 202 && angle < 225 ){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)(speedRight/2), (byte) (int)(speedLeft/2));
                            }
                            if(angle >= 225 && angle < 247){
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)((speedRight/2) + 10), (byte) (int)(speedLeft/2));
                            }
                        }
                    }
                }
            }
        };
        thread.start();
    }
    /**************************************************************************
     * FUNCTION NAME : stopThread
     * DESCRIPTION   : synchronized function will will stop the thread completly
     * NOTE          :
     **************************************************************************/
    private synchronized void stopThread() {
        if (thread != null)
            thread = null;
    }

    /**************************************************************************
     * FUNCTION NAME : onDetachedFromWindow
     * DESCRIPTION   : This function will run every time the user switch window and to stop
     *                 sending data via ths window we stop the thread
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isThread = false; /* Stops the thread */
    }
    /**************************************************************************
     * FUNCTION NAME : onDraw
     * DESCRIPTION   : Override function witch update the ball on the screen
     * NOTE          :
     **************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, Max);
        canvas.drawText("dx: " + Float.toString(dx) + " dy: " + Float.toString(dy) + " Angle: " + Float.toString(angle),
                40, 40, paint);

        if(x == 0 && y == 0) { /* Init to set the ball at the middle */
            x = (int) getWidth()/2;
            Ox = x; /* set the Origo x coordinate */
            y = (int) getHeight()/2;
            Oy = y; /* set the Origo Y coordinate */
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, 50, paint); /* Drawing a circle in the middle of the screen */
        }
        else
            canvas.drawCircle(x, y, 50, paint); /* Draw the current circle while user drag finger around screen */
        postInvalidateDelayed(20); /* Makes the like a thread */
    }

    /**************************************************************************
     * FUNCTION NAME : onTouch
     * DESCRIPTION   : Override function wich will run everytime the user touches the screen
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Calculate(event.getX(), event.getY()); /* Calculate where the user has the finger with a x and y coordinate */
                break;
            case MotionEvent.ACTION_UP: /* here we put the circle to the mittle and stop the robot */
                x = (int)getWidth()/2; /* Sets the ball to the middle of the screen */
                y = (int)getHeight()/2;
                Calculate(x, y);
                break;
            case MotionEvent.ACTION_MOVE: /* get new x and y coordinate if the user moves his finger */
                Calculate(event.getX(), event.getY());
                break;
        }
        return true;
    }


    /**************************************************************************
     * FUNCTION NAME : Calculate
     * DESCRIPTION   : If user drags the cursor outside of the big cirkle then we
     *                 calculate so that the cursor not goes over the big cirkle
     * NOTE          : c is the hypotenuse we determinate the speed we send to the robot
     **************************************************************************/
    private void Calculate(float xx, float yy){
        dx = xx - Ox;
        dy = yy - Oy;
        float angleMath = (float)Math.atan(dy/dx);
        c = Math.hypot(dx, dy);
        if(c > radius){  /* if user drags the cursor outside the max box we calculate so the circle will not going outside max box */
            if(dx > 0 && dy > 0) { /* bottom right */
                xx = Ox + (radius * (float) Math.cos(angleMath));
                yy = Oy + (radius * (float) Math.sin(angleMath));
                angle = (float)Math.toDegrees(Math.atan(dy / dx));
            }
            else if(dx > 0 && dy < 0){ /*top right */
                xx = Ox + (radius * (float) Math.cos(angleMath));
                yy = Oy + (radius * (float) Math.sin(angleMath));
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 360;
            }
            else if( dx < 0 && dy < 0){ /* top left */
                xx = Ox - (radius * (float) Math.cos(angleMath));
                yy = Oy - (radius * (float) Math.sin(angleMath));
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 180;
            }
            else if( dx < 0 && dy > 0 ) { /* bottom left */
                xx = Ox - (radius * (float) Math.cos(angleMath));
                yy = Oy - (radius * (float) Math.sin(angleMath));
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 180;
            }
            c = 200;
        }
        else {
            if(dx > 0 && dy > 0) /* bottom right */
                angle = (float)Math.toDegrees(Math.atan(dy / dx));

            else if(dx > 0 && dy < 0) /* top right */
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 380;

            else if(dx < 0 && dy < 0) /* top left */
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 180;

            else if( dx < 0 && dy > 0) /* bottom left */
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 180;

            else
                angle = 0;

            x = dx + Ox;
            y = dy + Oy;
        }
        x = xx;
        y = yy;
    }
}