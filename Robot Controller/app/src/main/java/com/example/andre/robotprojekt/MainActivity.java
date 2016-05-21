package com.example.andre.robotprojekt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;


/**
 * This class is a login screen where you have to login to use the application
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity{
    private BluetoothAdapter _bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    /**************************************************************************
     * FUNCTION NAME : onCreate
     * DESCRIPTION   : Check if bluetooth low energy is avaiable on your phone
     * NOTE          :
     **************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_login);
        setContentView(R.layout.activity_main);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        _bluetoothAdapter = bluetoothManager.getAdapter();

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }
        else {
            /* Make user turn on his bluetooth */
            if(_bluetoothAdapter == null || !_bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                Toast.makeText(getApplicationContext(), R.string.Bluetooth_on, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**************************************************************************
     * FUNCTION NAME : onCreateOptionsMenu
     * DESCRIPTION   : Create the menu so that you can change activity to register
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_meny, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**************************************************************************
     * FUNCTION NAME : onOptionsItemSelected
     * DESCRIPTION   : Open the activity the user clicks on in the menu
     * NOTE          :
     **************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_Register:
                startActivity(new Intent(this, RegisterActivity.class));
                return true;
            case R.id.menu_Signin: /* if user is on the activity we make a toast saying we are in the activity */
                Toast.makeText(getApplicationContext(), R.string.ErrorPage, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_Exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    /**************************************************************************
     * FUNCTION NAME : onSignIn
     * DESCRIPTION   : If user has Bluetooth Low Energy Support we sign in the user
     * NOTE          :
     **************************************************************************/
    public void onSignIn(View view) throws ExecutionException, InterruptedException {

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        } else {
            /* Make user turn on his bluetooth */
            if (_bluetoothAdapter == null || !_bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                Toast.makeText(getApplicationContext(), R.string.Bluetooth_on, Toast.LENGTH_LONG).show();
            }
        }

        if (_bluetoothAdapter.isEnabled()) {
            EditText EditText_Name = (EditText) findViewById(R.id.LoginTextfield_Name);
            EditText EditText_Password = (EditText) findViewById(R.id.LoginTextfield_password);
            String String_Name = EditText_Name.getText().toString();
            String String_Password = EditText_Password.getText().toString();
            String String_Method = "Login";

            /* put these in Database */
            BackgroundTask backgroundTask = new BackgroundTask(this);
            String ReadBack_Database = backgroundTask.execute(String_Name, String_Password, String_Method).get();

            /* put information in Database */
            if(ReadBack_Database.equals("true")) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
            }
        }
    }
}
