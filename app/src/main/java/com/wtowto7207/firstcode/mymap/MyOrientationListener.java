package com.wtowto7207.firstcode.mymap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2015/8/4.
 */

public class MyOrientationListener implements SensorEventListener {

    private SensorManager manager;
    private Sensor mSensor;
    private Context mContext;

    private float locationX;

    public MyOrientationListener(Context context) {
        this.mContext = context;
    }

    public void start() {
        manager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (manager != null) {
            mSensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        if (mSensor != null) {
            if (manager != null) {
                manager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    public void stop() {
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //监听方向改变
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[SensorManager.DATA_X];
            if (Math.abs(x - locationX) > 1.0) {
                mOnOrientationListener.onOrientationChanged(x);
            }

            locationX = x;
        }
    }

    private OnOrientationListener mOnOrientationListener;

    public void setOnOrientationListener(OnOrientationListener OnOrientationListener) {
        this.mOnOrientationListener = OnOrientationListener;
    }

    public interface OnOrientationListener {
        void onOrientationChanged(float x);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}