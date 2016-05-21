package com.example.andre.robotprojekt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.View;

/**
 * Class SensorHelper will handle and feel for how the user hold the phone and send commands to the
 * bluetoth accordning to the angle its held in
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class HandsomeSteeringHelper extends View  implements SensorEventListener {

    protected SensorManager sensorManager;
    protected Sensor accelerometer;
    protected Sensor magnetometer;
    protected SensorEventListener listener;
    protected float[] gravityValues = new float[3];
    protected float[] geomagneticValues = new float[3];
    protected Paint paint;
    protected Paint Max;
    protected Activity mActivity;
    protected double x,y, Ox, Oy, dx, dy, angle, radius, c;

    private Thread thread;
    private boolean isThread = true;

    /**************************************************************************
     * DESTRUCTOR    : HandsomeSteeringHelper
     * DESCRIPTION   : Setup sensor manager with the specific sensor we are going to use
     * NOTE          : Thread who send data via Bluetooth LE
     **************************************************************************/
    public HandsomeSteeringHelper(Context context, Activity activity) {
        super(context);
        mActivity = activity;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME); /* Register a listener who listen to the data the sensor provides */
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME); /* Register a listener who listen to the data the sensor provides */

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setStyle(Paint.Style.FILL);
        Max = new Paint();
        Max.setColor(Color.GRAY);
        Max.setStyle(Paint.Style.FILL);
        radius = 200;
        thread = new Thread() { /* Start a new thread to send specific data depending on the angle of how the user is holding the device */
            @Override
            public void run() {
                while(isThread){
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isThread)
                        stopThread();
                    if(BluetoothActivity.isBluetoothReady){
                        if(angle >= 247 && angle < 292 ) { /* STICK_UP */
                            BluetoothActivity.sendDataToBluetooth((byte) 3, (byte) ((int)c / 4), (byte) ((int)c / 4));
                        }
                        else if(angle >= 292 && angle < 337 ) { /* STICK_UPRIGHT */
                            if(angle >= 292 && angle < 315){
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = (c - speedRight);
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)(speedLeft/4), (byte) (int)((speedRight/4) + 10));
                            }
                            if(angle >= 315 && angle < 337){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)(speedLeft/4), (byte) (int)(speedRight/4));
                            }
                        }
                        else if(angle >= 337 || angle < 22 ) { /* STICK_RIGHT */
                            BluetoothActivity.sendDataToBluetooth((byte) 1, (byte) ((int)c / 5), (byte) ((int)c / 5));
                        }
                        else if(angle >= 22 && angle < 67 ) { /* STICK_DOWNRIGHT */
                            if(angle >= 22 && angle < 45){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)0, (byte) (int)(speedRight/4), (byte) (int)(speedLeft/4));
                            }
                            if(angle >= 45 && angle < 67){
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)0, (byte) (int)((speedRight/4) + 10), (byte) (int)(speedLeft/4));
                            }
                        }
                        else if(angle >= 67 && angle < 112 ) { /* STICK_DOWN */
                            BluetoothActivity.sendDataToBluetooth((byte) 0, (byte) ((int)c / 4), (byte) ((int)c / 4));
                        }
                        else if(angle >= 112 && angle < 157 ) { /* STICK_DOWNLEFT */
                            if(angle >= 112 && angle < 135) {
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)0, (byte) (int)(speedLeft / 4), (byte) (int)((speedRight / 4) + 10));
                            }
                            if(angle >= 135 && angle < 157){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte) 0, (byte) (int) (speedLeft / 4), (byte) (int) (speedRight / 4));
                            }
                        }
                        else if(angle >= 157 && angle < 202 ) { /* STICK_LEFT */
                            BluetoothActivity.sendDataToBluetooth((byte) 2, (byte) ((int)c / 5), (byte) ((int)c / 5));
                        }
                        else if(angle >= 202 && angle < 247 ) { /* STICK_UPLEFT */
                            if(angle >= 202 && angle < 225 ){
                                double speedRight = Math.abs(c * Math.cos(85));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)(speedRight / 4), (byte) (int)(speedLeft / 4));
                            }
                            if(angle >= 225 && angle < 247){
                                double speedRight = Math.abs(c * Math.cos(70));
                                double speedLeft = c - speedRight;
                                BluetoothActivity.sendDataToBluetooth((byte)3, (byte) (int)((speedRight/4) + 10), (byte) (int)(speedLeft/4));
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
     * DESTRUCTOR    : onSensorChanged
     * DESCRIPTION   : Listen for changes the sensor provides
     * NOTE          :
     **************************************************************************/
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, gravityValues, 0, event.values.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, geomagneticValues, 0, event.values.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* N/A */
    }

    /**************************************************************************
     * DESTRUCTOR    : onDraw
     * DESCRIPTION   : Calculate the rotation matrix to get X, Y, Z coordinate and map them to the
     *                 Phones coordinate system and then send data depending on the rotation of the
     *                 phone
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float[] R = new float[9];
        boolean success = SensorManager.getRotationMatrix(R, null, gravityValues, geomagneticValues); /* this function collects the I in the passed rotation matrix */

        if (success) { /* if the rotation matrix success we calculate the rotation  */
            float R2[] = new float[9];
            int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation(); /* get the display rotation */

            switch (rotation) {
                case Surface.ROTATION_0: // Default orientation is used.
                    SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Y, R2);
                    break;
                case Surface.ROTATION_90: // Default orientation + 90 degrees.
                    SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, R2);
                    break;
                case Surface.ROTATION_180: // Default orientation + 180 degrees
                    SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, R2);
                    break;
                case Surface.ROTATION_270: // Default orientation + 270 degrees
                    SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, R2);
                    break;
            }
            /* This bellow will map the rotation of the phone to the coordinate system on the phone */
            float[] rotations = new float[3];
            SensorManager.getOrientation(R2, rotations); /* Calculates the orientation of the rotation */

            double xRotation = rotations[1];
            double yRotation = rotations[2];
            double zRotation = rotations[0];

            int width = getWidth();
            int height = getHeight();
            Ox = width/2; /* Set origin X value */
            Oy = height/2; /* Set origin Y value */
            x = width / 2 + yRotation * width / 4; /* Sensetivity  */
            y = height / 2 + -xRotation * height / 4; /* Sensetivity  */

            Calculate(x, y);
            int tdx = (int)dx;
            int tdy = (int)dy;
            int tangle = (int)angle;
            canvas.drawText("dx:" + Double.toString(tdx) + " dy: " + Double.toString(tdy) + " ang: " + tangle, 50, 100, paint);
            canvas.drawCircle(getWidth()/2, getHeight()/2, (float)radius, Max);
            canvas.drawCircle((float) x, (float) y, 50, paint);
        }
        postInvalidateDelayed(10); /* Makes on draw like a thread */
    }

    /**************************************************************************
     * DESTRUCTOR    : onDetachedFromWindow
     * DESCRIPTION   : Unregister the SensorListener to stop drain battery and stop the thread so
     *                 it not will send data to the H/W
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sensorManager.unregisterListener(listener); /* Stop listening for Sensor changes  */
        isThread = false; /* Stop sending data */
    }

    /**************************************************************************
     * FUNCTION NAME : Calculate
     * DESCRIPTION   : If user drags the cursor outside of the big circle then we
     *                 calculate so that the cursor not goes over the big circle
     * NOTE          : c is the hypotenuse we determinate the speed we send to the robot
     **************************************************************************/
    private void Calculate(double xx, double yy){
        dx = xx - Ox;
        dy = yy - Oy;
        float angleMath = (float)Math.atan(dy/dx); //Math.abs(dy/dx)
        c = Math.hypot(dx, dy);
        if(c > radius){ //out of bounds
            if(dx > 0 && dy > 0) { //bottom right
                xx = Ox + (radius * (float) Math.cos(angleMath));
                yy = Oy + (radius * (float) Math.sin(angleMath));
                angle = (float)Math.toDegrees(Math.atan(dy / dx)); //Math.abs(dy/dx)
            }
            else if(dx > 0 && dy < 0){ //top right
                xx = Ox + (radius * (float) Math.cos(angleMath));
                yy = Oy + (radius * (float) Math.sin(angleMath));
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 360; //Math.abs(dy/dx)
            }
            else if( dx < 0 && dy < 0){ //top left
                xx = Ox - (radius * (float) Math.cos(angleMath));
                yy = Oy - (radius * (float) Math.sin(angleMath));
                angle = (float)Math.toDegrees(Math.atan(dy / dx)) + 180; //Math.abs(dy/dx)
            }
            else if( dx < 0 && dy > 0 ) { //bottom left
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
                angle = 0; /* if its in the middle */
            x = dx + Ox;
            y = dy + Oy;
        }
        x = xx;
        y = yy;
    }

}
