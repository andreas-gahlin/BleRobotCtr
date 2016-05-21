package com.example.andre.robotprojekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * This class have buttons on the screen where if a button is clicked its sent to the bluetooth device
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class ButtonFragment extends Fragment {

    private Button Fragment_Left, Fragment_Right, Fragment_Fwd, Fragment_Back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mView =  inflater.inflate(R.layout.fragment_button, container, false);
        Fragment_Left = (Button) mView.findViewById(R.id.Fragment_BtnLeft);
        Fragment_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnLeft(mView);
            }
        });
        Fragment_Right = (Button) mView.findViewById(R.id.Fragment_BtnRight);
        Fragment_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnRight(mView);
            }
        });
        Fragment_Back = (Button) mView.findViewById(R.id.Fragment_BtnDown);
        Fragment_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnBack(mView);
            }
        });
        Fragment_Fwd = (Button) mView.findViewById(R.id.Fragment_BtnFwd);
        Fragment_Fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnFwd(mView);
            }
        });

        return mView;
    }

    public void onBtnBack(View view){
        if(BluetoothActivity.isBluetoothReady)
            BluetoothActivity.sendDataToBluetooth((byte) 0, (byte) 100, (byte) 100);
    }
    public void onBtnLeft(View view){
        if(BluetoothActivity.isBluetoothReady)
            BluetoothActivity.sendDataToBluetooth((byte) 2, (byte) 100, (byte) 100);
    }
    public void onBtnRight(View view){
        if(BluetoothActivity.isBluetoothReady)
            BluetoothActivity.sendDataToBluetooth((byte) 1, (byte) 100, (byte) 100);
    }
    public void onBtnFwd(View view){
        if(BluetoothActivity.isBluetoothReady)
            BluetoothActivity.sendDataToBluetooth((byte) 3, (byte) 100, (byte)100);
    }
}
