package com.example.andre.robotprojekt;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


/**
 * This class is in the bakground of the tabs who contains the login as connect to the specific device
 * who was click in the profile and read and send with the device, it also put out the location of the user
 * if the user has GPS service on.
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class BluetoothActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{ //BluetoothAdapter.LeScanCallback,

    /* Provides the entry point to Google Play services. */
    protected GoogleApiClient mGoogleApiClient;
    protected TextView LocationView; /* put text in the textField wih GPS Coordinates */
    protected LocationHelper locationThread;

    private Handler _Handler = new Handler(); /* Post the thread with GPS service to the UI */
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ID = "id";
    public String mDeviceName;
    public String mDeviceAddress;
    protected boolean isMenushow = true; /* sets the menu enabled */
    private static BluetoothService mBluetoothLeService; /**/
    public static boolean isBluetoothReady = true; /**/

    /**************************************************************************
     * VARIABLE NAME : mServiceConnection
     * DESCRIPTION   : Bind the Activity to the service
     * NOTE          : -
     **************************************************************************/
    public final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothService.LocalBinder) service).getService();
            mBluetoothLeService.setmActivityHanlder(mMessageHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothLeService = null;
        }
    };

    /**************************************************************************
     * VARIABLE NAME : mMessageHandler
     * DESCRIPTION   : Handle message's between The service class and this activity
     * NOTE          : -
     **************************************************************************/
    public Handler mMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            switch (msg.what){
                case BluetoothService.GATT_CONNECTED:
                    isMenushow = false; /* Disable menu when connected */
                    invalidateOptionsMenu(); /* Update the menu and to disable it */
                    BluetoothActivity.this.mBluetoothLeService.readCharacteristic(BluetoothService.gattUUID.SERVICE.toString(), BluetoothService.gattUUID.DATA_CHARACTERISTIC.toString());
                    break;
                case BluetoothService.GATT_DISCONNECTED:
                    isMenushow = true; /* If device is disconnected enable menu connection */
                    invalidateOptionsMenu(); /* Update the menu and to enable it */
                    break;
                case BluetoothService.GATT_SERVICE_DISCOVERED:
                    isMenushow = false; /* disables menu if connection succsses*/
                    invalidateOptionsMenu(); /* Update the menu and to disable it */
                    break;
                case BluetoothService.GATT_CHARACTERISTIC_READ:
                    bundle = msg.getData();
                    if(bundle.get(BluetoothService.PARACEL_UUID).toString().equals(BluetoothService.gattUUID.DATA_CHARACTERISTIC.toString())){
                        byte[] b = bundle.getByteArray(BluetoothService.PARACEL_VALUE);
                    }
                    break;
                case BluetoothService.GATT_REMOTE_RSSI:
                    break;
            }
        }
    };

    /**************************************************************************
     * FUNCTION NAME : onCreateOptionsMenu
     * DESCRIPTION   : if connected to the device the menu item will not appear otherwise we want
     *                 to activate the menu to let the user connect to the device
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isMenushow){ /* loop through the list and disable all items */
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
            return true;
        }
        getMenuInflater().inflate(R.menu.connect_menu, menu); /* otherwise show connect in the menu */
        return true;
    }

    /**************************************************************************
     * FUNCTION NAME : onOptionsItemSelected
     * DESCRIPTION   : If the connect button is pushed we connect to the device
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_connect:
                if(mBluetoothLeService != null){
                    if(mBluetoothLeService.connect(mDeviceAddress)) {
                        isMenushow = false; /* if connection was succsessfull disable menu */
                        invalidateOptionsMenu(); /* Update the menu */
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    /**************************************************************************
     * FUNCTION NAME : onCreate
     * DESCRIPTION   : Init Tabs menu and the thread who takes coordiantes from
     *                 the GPS service
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient(); /* build google GPS service */
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRA_NAME); /* From the activity we searched for devices we collect the device name*/
        mDeviceAddress = intent.getStringExtra(EXTRA_ID); /* collect the ID */
        setContentView(R.layout.bluetooth_le); /* Set the conentView of the activity with FragmentTabHost */

        Intent gattServiceIntent = new Intent(this, BluetoothService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        /**************************************************************************
         * TABS
         * DESCRIPTION   : Create a tabs for the user to quickly switch between different windows
         * NOTE          : The tabs is used in many applications such as JU Mobile, Youtube, facebook, etc..
         **************************************************************************/
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(
                tabHost.newTabSpec(getString(R.string.Tag_1)).setIndicator(getString(R.string.BtnCtr), null),
                ButtonFragment.class, null);
        tabHost.addTab(
                tabHost.newTabSpec(getString(R.string.Tag_2)).setIndicator(getString(R.string.JoyStckCtr), null),
                JoystickFragment.class, null);
        tabHost.addTab(
                tabHost.newTabSpec(getString(R.string.Tag_3)).setIndicator(getString(R.string.SenCtr), null),
                SensorFragment.class, null);
        tabHost.addTab(
                tabHost.newTabSpec(getString(R.string.Tag_4)).setIndicator(getString(R.string.HandSteering), null),
                HandsomeSteering.class, null);

        LocationView = (TextView) findViewById(R.id.ShowAdress);

        locationThread = new LocationHelper(this, mGoogleApiClient);

        /**************************************************************************
         * TREAD
         * DESCRIPTION   : Every 10s we attempt taking the location of the user
         * NOTE          : if user not have internet or position service we can't
         *                 take the position of user
         **************************************************************************/
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(!mGoogleApiClient.isConnected())
                            mGoogleApiClient.connect();
                         Thread.sleep(10000);
                        _Handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String test = "";
                                test = locationThread.doInBackground();
                                LocationView.setText(test);
                            }
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    /**************************************************************************
     * FUNCTION NAME : buildGoogleApiClient
     * DESCRIPTION   : Build Google API Client witch makes GPS service aviable
     * NOTE          :
     **************************************************************************/
    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**************************************************************************
     * FUNCTION NAME : onResume
     * DESCRIPTION   : We want to reconnect to the GPS service to take the location of the user
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }


    /**************************************************************************
     * FUNCTION NAME : onStop
     * DESCRIPTION   : if user exit the program we want to save battery power and close the GPS service
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        /* Disconnect if user leave */
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    /**************************************************************************
     * FUNCTION NAME : onDestroy
     * DESCRIPTION   : if user choises to exit the app or start a new activity we want to
     *                 unbind the service we are connectet to
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    /**************************************************************************
     * FUNCTION NAME : SendDatatoBluetooth
     * DESCRIPTION   : if we are not connected to any device we shall not send any data
     * NOTE          :
     **************************************************************************/
    public static void sendDataToBluetooth(byte CMD, byte leftMotorSpeed, byte rightMotorSpeed) {
        if (mBluetoothLeService != null) {
            if (mBluetoothLeService.writeCharacteristic(BluetoothService.gattUUID.SERVICE.toString(),
                    BluetoothService.gattUUID.DATA_CHARACTERISTIC.toString(),
                    new byte[]{CMD, leftMotorSpeed, rightMotorSpeed})) {
            }
        }
    }

    /* These functon is only for google playservice API */
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}