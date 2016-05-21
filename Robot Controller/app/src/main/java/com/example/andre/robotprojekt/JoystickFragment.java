package com.example.andre.robotprojekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * This class will create a view of the joystick
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class JoystickFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new CustomSurfaceView(getContext());
    }

}
