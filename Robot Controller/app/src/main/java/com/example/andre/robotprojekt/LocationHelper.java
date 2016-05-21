package com.example.andre.robotprojekt;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;


/**
 * This class will make take the location of the user
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */

public class LocationHelper  {
    protected Context mcontext;
    protected GoogleApiClient mGoogleApiClient;
    protected String mLastLocation;

    /**************************************************************************
     * FUNCTION NAME : LocationHelper
     * DESCRIPTION   : Take two arguments wich is contex of the app and the googleApiClient
     * NOTE          :
     **************************************************************************/
    public LocationHelper(Context context, GoogleApiClient googleApiClient){
        mcontext = context.getApplicationContext();
        mGoogleApiClient = googleApiClient;
    }

    /**************************************************************************
     * FUNCTION NAME : doInBackground
     * DESCRIPTION   : Print data on the gps data on the phone's activity
     * NOTE          :
     **************************************************************************/
    protected String doInBackground() {
        double Lat, Long, Alt, Acc;
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            Lat = location.getLatitude();
            Long = location.getLongitude();
            Alt = location.getAltitude();
            Acc = location.getAccuracy();
            Geocoder geocoder = new Geocoder(mcontext);
            List<Address> addressList;
            Address address;
            String locality = "";
            String UserAddress = "";
            try {
                addressList = geocoder.getFromLocation(Lat, Long, 1); /*lat, long maxResults */
                address = addressList.get(0);
                locality = address.getLocality();
                UserAddress = address.getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mLastLocation = ("City: " + locality + ". Adress: " + UserAddress );
            return mLastLocation;
        }
        else if(location == null)
            return "Aktivera platstjänster";
        else
        return "Uppdaterar postition";
    }
}
