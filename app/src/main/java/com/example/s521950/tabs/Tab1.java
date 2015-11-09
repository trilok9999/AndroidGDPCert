package com.example.s521950.tabs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Trilok on 10-06-2015.
 */
public class Tab1 extends Fragment {
    TextView latitude;
     TextView longitude
             ;
    StringBuilder sb=new StringBuilder();
    String json;
    HttpURLConnection urlConnection;
    Tab2 tab2;
    Button submit;
    StringBuilder address = new StringBuilder();
    StringBuilder latitudeBuilder=new StringBuilder();
    StringBuilder longitudeBuilder=new StringBuilder();
    StringBuilder getlocation=new StringBuilder();
    EditText location2,remains;
    Location location;
    CheckBox fineAcc;
    HttpURLConnection urlConnection2;
    LocationManager locationManager;
    StringBuilder severity=new StringBuilder();
    String key="AIzaSyBa5i8jJnXcDB2khD0KwYoJZG0xGAqyBJY";
View v;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    Button b;
    Button image;
    Spinner spi;
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
        tab2=new Tab2();
         v = inflater.inflate(R.layout.tab1, container, false);
         spi = (Spinner) v.findViewById(R.id.spinner);
        remains=(EditText)v.findViewById(R.id.remains);
b=(Button)v.findViewById(R.id.submit);

        StringBuilder type=new StringBuilder();
location2=(EditText)v.findViewById(R.id.convertlocation);
        b.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                     builder2.setTitle("Are you sure, do you want to submit the form ?");
                                     builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             new PostAsyncTask().execute();

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
                severity.append(parent.getSelectedItem().toString());
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
        if (!isChecked) {
            latitude.setText("");
            longitude.setText("");
            location2.setText("");
            address.delete(0,address.length());
        } else {
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
        }
    }
});




        return v;
    }
    private class PostAsyncTask extends AsyncTask<String,Integer,Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData();
            return null;

        }

        @Override
        protected void onPostExecute(Double aDouble) {
            spi.setSelection(0);
            remains.setText("");
            longitude.setText("");
            latitude.setText("");
            super.onPostExecute(aDouble);
        }
    }

    private class MyAsyncTask extends AsyncTask<String,Integer,Double> {
        @Override
        protected Double doInBackground(String... params) {
            mapsData();

            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble){
            try {

                JSONObject object = new JSONObject(getlocation.toString());
                JSONArray jsonArray = object.getJSONArray("results");
                address.append(jsonArray.getJSONObject(1).optString("name") + "," + jsonArray.getJSONObject(1).optString("vicinity"));
                Log.i("gmapsAdress", address.toString());
                System.out.println("trilok" + address.toString());
                location2.setText(address.toString());
                JSONObject report = new JSONObject();
                report.put("userid", tab2.UserID);
                report.put("groupid", tab2.GroupId);
                report.put("latitude", latitudeBuilder.toString());
                report.put("longitude", longitudeBuilder.toString());
                severity.delete(0,25);
                report.put("severity", severity.toString());
                report.put("remains", remains.getText().toString());
                json = report.toString();
                super.onPostExecute(aDouble);
            }
            catch(JSONException je){

            }
        }
    }
    public void mapsData(){
        try {
            URL url2 = new URL("https://maps.googleapis.com/maps/api/place/search/json?location="+ latitudeBuilder.toString()+","+longitudeBuilder.toString()+"&radius=100&sensor=true&key="+key);
            urlConnection2 = (HttpURLConnection) url2.openConnection();
            urlConnection2.setDoInput(true);
            urlConnection2.setRequestMethod("GET");
            urlConnection2.setUseCaches(false);
            urlConnection2.setConnectTimeout(10000);
            urlConnection2.setReadTimeout(10000);
            urlConnection2.connect();
            InputStream is2= urlConnection2.getInputStream();
            BufferedReader Reader2 = new BufferedReader(
                    new InputStreamReader(is2, "UTF-8"));
            String input2="";
            while((input2=Reader2.readLine())!=null){
//                System.out.println(input);
//                Log.i("MapsAPi",input);
                getlocation.append(input2);

            }

            Reader2.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.####");
        return Double.valueOf(twoDForm.format(d));
    }
    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            double lat=location.getLatitude();
            double lon=location.getLongitude();
            latitude = (TextView) v.findViewById(R.id.latitude);
            longitude = (TextView) v.findViewById(R.id.longitude);
            longitudeBuilder.delete(0,longitudeBuilder.length());
            latitudeBuilder.delete(0,latitudeBuilder.length());
            if(fineAcc.isChecked()) {

                latitude.setText("Latitude: " + String.valueOf(roundTwoDecimals(lat)));
                longitude.setText("Longitude: " + String.valueOf(roundTwoDecimals(lon)));
                if(address.length()==0) {
                    latitudeBuilder.append(String.valueOf(roundTwoDecimals(lat)));
                    longitudeBuilder.append(String.valueOf(roundTwoDecimals(lon)));
                    new MyAsyncTask().execute();
                }
            }
            else{
                longitudeBuilder.delete(0,longitudeBuilder.length());
                latitudeBuilder.delete(0,latitudeBuilder.length());
                address.delete(0,address.length());
            }

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
    public void postData(){

        try {
            URL url = new URL("http://192.168.0.13:1000/memberReports");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            // urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            urlConnection.connect();
severity.delete(0,severity.length());

            int HttpResult =urlConnection.getResponseCode();
            System.out.println(HttpResult);
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                System.out.println(sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }

        finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
    }



}