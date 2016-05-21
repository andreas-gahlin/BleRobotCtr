package com.example.andre.robotprojekt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.UUID;

/**
 * This class will bind the activity to write and read things from the bluetooth device
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class BluetoothService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private Handler mActivityHanlder = null;
    public static final int GATT_CONNECTED = 1;
    public static final int GATT_DISCONNECTED = 2;
    public static final int GATT_SERVICE_DISCOVERED = 3;
    public static final int GATT_CHARACTERISTIC_READ = 4;
    public static final int GATT_REMOTE_RSSI = 5;

    public static final String PARACEL_UUID = "UUID";
    public static final String PARACEL_VALUE = "VALUE";
    public static final String PARACEL_RSSI = "RSSI";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;

    public static GattUUID gattUUID = new GattUUID(); /* Class with all provied Services & characteristics */

    /**************************************************************************
     * FUNCTION NAME : LocalBinder
     * DESCRIPTION   : Class who bind the service to the Activity
     * NOTE          :
     **************************************************************************/
    public class LocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    /**************************************************************************
     * FUNCTION NAME : setmActivityHanlder
     * DESCRIPTION   : Add a function to the service to enable the activity to
     *                 register with the service
     * NOTE          :
     **************************************************************************/
    public void setmActivityHanlder(Handler hanlder){ //set the activity will recive the message
        mActivityHanlder = hanlder;
    }

    /**************************************************************************
     * FUNCTION NAME : onCreate
     * DESCRIPTION   :
     * NOTE          :
     **************************************************************************/
    @Override
    public void onCreate(){
        if(mBluetoothManager == null){
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if(mBluetoothManager == null)
                return;
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if(mBluetoothAdapter == null)
            return;
    }

    /**************************************************************************
     * FUNCTION NAME : connect
     * DESCRIPTION   : Calling this function will connect the phone with the bluetooth device
     *                 (GattServer)
     * NOTE          :
     **************************************************************************/
    public boolean connect(final String address){
        if(mBluetoothAdapter == null || address == null)
            return false;
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if(device == null)
            return false;
        mBluetoothGatt = device.connectGatt(this, true, mGattCallback);
        return true;
    }

    /**************************************************************************
     * FUNCTION NAME : disconnect
     * DESCRIPTION   : Disconnect the phone with the bluetooth device (GattServer)
     * NOTE          :
     **************************************************************************/
    public void disconnect(){
        if(mBluetoothAdapter == null || mBluetoothGatt == null)
            return;
        mBluetoothGatt.disconnect();
    }

    /**************************************************************************
     * FUNCTION NAME : onBind
     * DESCRIPTION   : Binds the activity with the service
     * NOTE          :
     **************************************************************************/
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    /**************************************************************************
     * FUNCTION NAME : onUnbind
     * DESCRIPTION   : Unbind the Activity from the service
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onUnbind(Intent intent){
        return super.onUnbind(intent);
    }

    /**************************************************************************
     * VARIABLE NAME : mGattCallback
     * DESCRIPTION   : Is a gatt class profile "header" witch provide us with methods to send and
     *                 read data with the device
     * NOTE          :
     **************************************************************************/
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /**************************************************************************
         * FUNCTION NAME : onConnectionStateChange
         * DESCRIPTION   : When connection is changed we check if we successful connected to the H/W
         * NOTE          :
         **************************************************************************/
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(newState == BluetoothProfile.STATE_CONNECTED){
                Message msg = Message.obtain(mActivityHanlder, GATT_CONNECTED);
                msg.sendToTarget();
                mBluetoothGatt.discoverServices();
            }
            else if( newState == BluetoothProfile.STATE_DISCONNECTED){
                Message msg = Message.obtain(mActivityHanlder, GATT_DISCONNECTED);
                msg.sendToTarget();
            }
        }
        /**************************************************************************
         * FUNCTION NAME : onServicesDiscovered
         * DESCRIPTION   : When connection we need to discover all the provided service's from the H/W
         * NOTE          :
         **************************************************************************/
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) { //runs only 1 time
            Message msg = Message.obtain(mActivityHanlder, GATT_SERVICE_DISCOVERED);
            msg.sendToTarget();
        }

        /**************************************************************************
         * FUNCTION NAME : onCharacteristicRead
         * DESCRIPTION   : Reads the characteristic within the service
         * NOTE          :
         **************************************************************************/
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            if(status == BluetoothGatt.GATT_SUCCESS){
                Bundle bundle = new Bundle();
                bundle.putString(PARACEL_UUID, characteristic.getUuid().toString());
                bundle.putByteArray(PARACEL_VALUE, characteristic.getValue());
                Message msg = Message.obtain(mActivityHanlder, GATT_CHARACTERISTIC_READ);
                msg.setData(bundle);
                msg.sendToTarget();
            }
        }

        /**************************************************************************
         * FUNCTION NAME : onReadRemoteRssi
         * DESCRIPTION   : Reads the Distance from the H/W to the android phone
         * NOTE          :
         **************************************************************************/
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                Bundle bundle = new Bundle();
                bundle.putInt(PARACEL_RSSI, rssi);
                Message msg = Message.obtain(mActivityHanlder, GATT_REMOTE_RSSI);
                msg.setData(bundle);
                msg.sendToTarget();
            }
        }

        /**************************************************************************
         * FUNCTION NAME : onCharacteristicChanged
         * DESCRIPTION   : When Service and characteristic change value this function will be called
         * NOTE          :
         **************************************************************************/
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };

    /**************************************************************************
     * FUNCTION NAME : setCharacteristicNotification
     * DESCRIPTION   : With the Read service and characteristic we can set so we get
     *                 a notification when data is sent from the H/W
     * NOTE          :
     **************************************************************************/
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
            return;

        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (GattUUID.DATA_CHARACTERISTIC.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    GattUUID.CLIENT_CHARACTERISTIC_CONFIG);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }


    /**************************************************************************
     * FUNCTION NAME : writeCharacteristic
     * DESCRIPTION   : Write data to the bluetooth (BC118) with a characteristic
     * NOTE          :
     **************************************************************************/
    public boolean writeCharacteristic(String ServiceUuid, String characteristicUuid, byte[] value){
        if(mBluetoothAdapter == null || mBluetoothGatt == null)
            return false;
        BluetoothGattService gattService = mBluetoothGatt.getService(UUID.fromString(ServiceUuid));
        if(gattService == null)
            return false;
        BluetoothGattCharacteristic gattChar = gattService.getCharacteristic(UUID.fromString(characteristicUuid));
        if(gattChar == null)
            return false;
        gattChar.setValue(value);
        return mBluetoothGatt.writeCharacteristic(gattChar);
    }
    /**************************************************************************
     * FUNCTION NAME : readCharacteristic
     * DESCRIPTION   : Read a Characteristic value from the bluetooth device
     * NOTE          :
     **************************************************************************/
    public boolean readCharacteristic(String ServiceUuid, String characteristicUuid){
        if(mBluetoothAdapter == null || mBluetoothGatt == null)
            return false;

        BluetoothGattService gattService = mBluetoothGatt.getService(UUID.fromString(ServiceUuid));
        if(gattService == null)
            return false;

        BluetoothGattCharacteristic gattChar = gattService.getCharacteristic(UUID.fromString(characteristicUuid));
        if(gattChar == null)
            return false;

        return mBluetoothGatt.readCharacteristic(gattChar);
    }
}