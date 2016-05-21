package com.example.andre.robotprojekt;

import android.content.Intent;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;


/**
 * Take the user inputs in the EditTextFields and store them into a database
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class RegisterActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /* Provides the entry point to Google Play services */
    protected GoogleApiClient mGoogleApiClient;
    /* Represents a geographical location */
    protected Location mLastLocation;
    protected String mLatitude;
    protected String mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_meny, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_Register:
                Toast.makeText(getApplicationContext(), R.string.ErrorPage, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_Signin:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.menu_Exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void onRegister(View view) {
        double Lat, Long, Alt, Acc;
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Toast.makeText(getApplicationContext(), R.string.Activate_Place, Toast.LENGTH_SHORT).show();
        }
        else if(location != null){

            Lat = location.getLatitude();
            Long = location.getLongitude();
            Alt = location.getAltitude();
            Acc = location.getAccuracy();

            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList;
            Address address;
            String locality = "";
            try { /* Trying to gecode your position */
                addressList = geocoder.getFromLocation(Lat, Long, 1); /* lat, long maxResults */
                address = addressList.get(0); /* Get the first hit your coordinates will match */
                locality = address.getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            EditText EditText_Name = (EditText) findViewById(R.id.Reg_EditText_Name);
            EditText EditText_Password = (EditText) findViewById(R.id.Reg_EditText_Password);
            String String_Name = EditText_Name.getText().toString();
            String String_Password = EditText_Password.getText().toString();
            String String_method = "Register";

            /* put these in Database */
            if (String_Name.equals("") && String_Password.equals(""))
                Toast.makeText(getApplicationContext(), R.string.Wrong_input, Toast.LENGTH_SHORT).show();
            else {
                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(String_Name, String_Password, String_method, locality); /* add geolocation */
            }
        }
        else
            Toast.makeText(getApplicationContext(), R.string.Activate_Place, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLongitude = String.format("%f",mLastLocation.getLatitude());
            mLatitude = String.format("%f", mLastLocation.getLongitude());
            Log.d("Google", "Lat: " + mLatitude + " Long: " + mLongitude);
        } else {
            Toast.makeText(this, R.string.Activate_Place, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}