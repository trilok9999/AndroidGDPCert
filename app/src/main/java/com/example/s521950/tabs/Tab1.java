package com.example.s521950.tabs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by Trilok on 10-06-2015.
 */
public class Tab1 extends Fragment {
    TextView latitude;
     TextView longitude;
    Location location;
    CheckBox fineAcc;
    LocationManager locationManager;
View v;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    Button b;
    Button image;
    boolean isGPSEnabled = false;
    ImageView ivImage;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    MyLocationListener mylistener;

    Criteria criteria;
    String provider;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.tab1, container, false);
        Spinner spi = (Spinner) v.findViewById(R.id.spinner);
b=(Button)v.findViewById(R.id.submit);

        b.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                     builder2.setTitle("Are you sure, do you want to submit the form ?");
                                     builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {


                                             Toast.makeText(getContext(), "submitted", Toast.LENGTH_LONG).show();
                                         }
                                     });
                                     builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {


                                                     dialog.dismiss();
                                                 }
                                             }
                                     );

                                     builder2.show();

                                 }
                             }
        );
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getSelectedItem().equals("Select severity of damage"))
                    Toast.makeText(parent.getContext(), "" + parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        latitude = (TextView) v.findViewById(R.id.latitude);

        // Get the location mana
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the location provider

        criteria = new Criteria();

        longitude = (TextView) v.findViewById(R.id.longitude);
        fineAcc = (CheckBox) v.findViewById(R.id.choose);
fineAcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
            latitude.setText("");
            longitude.setText("");
        }
    }
});
        fineAcc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                latitude = (TextView) v.findViewById(R.id.latitude);
                longitude = (TextView) v.findViewById(R.id.longitude);
                if (fineAcc.isChecked()) {
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setCostAllowed(false);

                    // get the best provider depending on the criteria
                    isGPSEnabled = locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);

                    // getting network status
                    isNetworkEnabled = locationManager
                            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    provider = locationManager.getBestProvider(criteria, false);


                    // the last known location of this provider


                    try {
                        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

                    } catch (SecurityException se) {

                    }
                    mylistener = new MyLocationListener();


                    if (location != null) {

                        mylistener.onLocationChanged(location);

                    } else {

                        // leads to the settings because there is no last known location

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                        startActivity(intent);

                    }

                    // location updates: at least 1 meter and 200millsecs change
                    try {
                        locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
                    } catch (SecurityException se2) {

                    }
                }


            }


        });



        return v;
    }


    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(d));
    }
    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            double lat=location.getLatitude();
            double lon=location.getLongitude();

            latitude = (TextView) v.findViewById(R.id.latitude);
            longitude = (TextView) v.findViewById(R.id.longitude);
            latitude.setText("Latitude: "+String.valueOf(roundTwoDecimals(lat)));

            longitude.setText("Longitude: "+String.valueOf(roundTwoDecimals(lon)));

        }

        @Override

        public void onStatusChanged(String provider, int status, Bundle extras) {


        }



        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override

        public void onProviderDisabled(String provider) {


        }
    }


}