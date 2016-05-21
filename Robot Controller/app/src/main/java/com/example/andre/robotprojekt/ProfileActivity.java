package com.example.andre.robotprojekt;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;


/**
 * Searching for bluetooth devices and put the out in a viewHolder on the screen
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */

public class ProfileActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private SparseArray<BluetoothDevice> _mDevice;
    private ListAdapter mLeDeviceListAdapter;

    private static final long SCAN_TIMEOUT = 5000;
    public static class ViewHolder {
        public TextView text;
    }

    private ScanCallback scanCallback;
    private static final int MSG_PROGRESS = 201;
    private static final int MSG_DISMISS = 202;
    private static final int MSG_CLEAR = 301;
    private ProgressDialog _progressDialog;

    /**************************************************************************
     * VARIABLE NAME : _Handler
     * DESCRIPTION   : Sets up a progressdialog with modifyed text
     * NOTE          :
     **************************************************************************/
    private Handler _Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_PROGRESS:
                    _progressDialog.setMessage((String) msg.obj);
                    _progressDialog.show();
                    break;
                case MSG_DISMISS:
                    _progressDialog.hide();
                    break;
                case MSG_CLEAR:
                    _progressDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**************************************************************************
     * FUNCTION NAME : onCreate
     * DESCRIPTION   : Initalize the clickListner in the Viewholder, Setting up a progressDialog
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        _progressDialog = new ProgressDialog(this);
        _progressDialog.setIndeterminate(true);
        _progressDialog.setCancelable(false);

        _mDevice = new SparseArray<BluetoothDevice>();

        final BluetoothManager bluetoothManager = (BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mLeDeviceListAdapter = new ListAdapter();
        ListView listView = (ListView) this.findViewById(R.id.deviceList);
        listView.setAdapter(mLeDeviceListAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                Intent intent = new Intent(ProfileActivity.this, BluetoothActivity.class);
                intent.putExtra(BluetoothActivity.EXTRA_NAME, device.getName());
                intent.putExtra(BluetoothActivity.EXTRA_ID, device.getAddress());
                startActivity(intent);
            }
        });
    }

    /**************************************************************************
     * FUNCTION NAME : onCreateOptionsMenu
     * DESCRIPTION   : Create a scan button on the menu to save room on the screen
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmeny, menu);
        return true;
    }
    /**************************************************************************
     * FUNCTION NAME : onOptionsItemSelected
     * DESCRIPTION   : Create a listner who will scan for devices if the scan button is clicked
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_scan:
                _mDevice.clear();
                startScan();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    /**************************************************************************
     * FUNCTION NAME : startScan
     * DESCRIPTION   : Start scanning for other Bluetooth low energy devices
     * NOTE          :
     **************************************************************************/
    private void startScan(){
        _Handler.sendMessage(Message.obtain(null, MSG_PROGRESS, getString(R.string.Searching_for_devices)));
        final List<BluetoothDevice> devicesfound = new ArrayList<>();

        final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner(); //search for devices

        if(scanCallback == null) {
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);

                    if (!devicesfound.contains(devicesfound)) {
                        mLeDeviceListAdapter.addDevice(result.getDevice());
                        mLeDeviceListAdapter.notifyDataSetChanged();
                        _Handler.sendMessage(Message.obtain(null, MSG_CLEAR, null));
                    }
                }
            };
        }

        _Handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothLeScanner.startScan(scanCallback);
            }
        }, SCAN_TIMEOUT);
    }

    /**************************************************************************
     * FUNCTION NAME : onStop
     * DESCRIPTION   : Stop scanning for devices
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.stopScan(scanCallback);
    }

    /**************************************************************************
     * CLASS         : ListAdapter
     * DESCRIPTION   : Create a ViewHolder to store and show every device on the screen
     * NOTE          :
     **************************************************************************/
    private class ListAdapter extends BaseAdapter{
        private ArrayList<BluetoothDevice> mLeDevices;
        public ListAdapter(){
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
        }
        public void addDevice(BluetoothDevice device){
            if(!mLeDevices.contains(device))
                mLeDevices.add(device);
        }
        public BluetoothDevice getDevice(int pos){
            return mLeDevices.get(pos);
        }
        public void clear() {
            mLeDevices.clear();
        }


        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return mLeDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            if(view == null) {
                view = ProfileActivity.this.getLayoutInflater().inflate(R.layout.profile_listrow, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) view.findViewById(R.id.textView);
                view.setTag(viewHolder);
            }
            else
                viewHolder = (ViewHolder) view.getTag();
            BluetoothDevice device = mLeDevices.get(position);
            final String deviceName = device.getName();
            if(deviceName != null && deviceName.length() > 0)
                viewHolder.text.setText(deviceName);
            else
                viewHolder.text.setText("Unknown Devnce");
            return view;
        }
    }
}
