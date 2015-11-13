package com.example.s521950.tabs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trilok on 10-06-2015.
 */
public class Tab1 extends Fragment {
    TextView latitude;
     TextView longitude
             ;
    HashMap<String,String> attach=new HashMap<>();
    StringBuilder sb=new StringBuilder();
    String json,json2;
    HttpURLConnection urlConnection;
    Tab2 tab2;
    Button submit;
    StringBuilder originalLat=new StringBuilder();
    StringBuilder originalLon=new StringBuilder();
    StringBuilder address = new StringBuilder();
    StringBuilder latitudeBuilder=new StringBuilder();
    StringBuilder longitudeBuilder=new StringBuilder();
    StringBuilder getlocation=new StringBuilder();
    EditText location2,remains,description;
    Location location;
    CheckBox fineAcc;
    RadioButton water,fire,electricity,neutral,others;
    RadioGroup radiogroup;
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
    JSONObject report,IncidentWall;
    StringBuilder message=new StringBuilder();
    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    MyLocationListener mylistener;
    StringBuilder type=new StringBuilder();
    Criteria criteria;
    String provider;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tab2=new Tab2();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("width",""+width);
        Log.i("Height", "" + height);
        if(width==800&&height>1200) {
            v = inflater.inflate(R.layout.tab1, container, false);
        }
        else{
            v = inflater.inflate(R.layout.tab1_mobile, container, false);
        }
description=(EditText)v.findViewById(R.id.description);
         spi = (Spinner) v.findViewById(R.id.spinner);
        remains=(EditText)v.findViewById(R.id.remains);
b=(Button)v.findViewById(R.id.submit);
        radiogroup=(RadioGroup)v.findViewById(R.id.radiogroup);
        water=(RadioButton)v.findViewById(R.id.water);
        fire=(RadioButton)v.findViewById(R.id.fire);
        electricity=(RadioButton)v.findViewById(R.id.electricity);
        neutral=(RadioButton)v.findViewById(R.id.neutral);
        others=(RadioButton)v.findViewById(R.id.others);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.water){
                    type.delete(0, type.length());
                    type.append("Water");
                }
                else if(checkedId==R.id.electricity){
                    type.delete(0,type.length());
                    type.append("Electricity");
                }
                else if(checkedId==R.id.fire){
                    type.delete(0,type.length());
                    type.append("Fire");
                }
                else if(checkedId==R.id.neutral){
                    type.delete(0,type.length());
                    type.append("Natural");
                }
                else if(checkedId==R.id.others){
                    type.delete(0,type.length());
                    type.append("Others");
                }
            }
        });
location2=(EditText)v.findViewById(R.id.convertlocation);
        b.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                     builder2.setTitle("Are you sure, do you want to submit the form ?");
                                     builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
//                           int selectedId=radiogroup.getCheckedRadioButtonId();
//                                             if(selectedId==water.getId()){
//                                                 type.append("Water");
//                                             }
//                                             else if(selectedId==electricity.getId()){
//                                                 type.append("Electricity");
//                                             }
//                                             else if(selectedId==neutral.getId()){
//                                                 type.append("Natural");
//                                             }
//                                             else if(selectedId==fire.getId()){
//                                                 type.append("Fire");
//                                             }
//                                             else if(selectedId==others.getId()){
//                                                 type.append("Others");
//                                             }

                                             try {
                                                 report.put("type", type.toString());
                                                 message.append(" and the Type of Hazard here is " + type.toString());
                                                 message.append(", I am at "+address.toString()+".");
                                                 message.append("   "+description.getText().toString());
                                                 IncidentWall.put("message",message.toString());
                                             }
                                             catch (JSONException js){

                                             }
                                             type.delete(0,type.length());
                                             json = report.toString();
                                             json2=IncidentWall.toString();
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
                if (!parent.getSelectedItem().equals("Select severity of damage")) {
                    severity.delete(0, severity.length());
                    severity.append(parent.getSelectedItem().toString());
                    //Toast.makeText(getContext(), severity.toString(), Toast.LENGTH_LONG).show();
                }
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
            location2.setText("");
            fineAcc.setChecked(false);
            water.setChecked(false);
            electricity.setChecked(false);
            neutral.setChecked(false);
            fire.setChecked(false);
            others.setChecked(false);
            description.setText("");
            new ReportsAsyncTask().execute();
            super.onPostExecute(aDouble);
        }
    }
    private class ReportsAsyncTask extends AsyncTask<String,Integer,Double> {
        @Override
        protected Double doInBackground(String... params) {
           postIncident();
            return null;

        }

        @Override
        protected void onPostExecute(Double aDouble) {


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
                address.delete(0,address.length());
                JSONObject object = new JSONObject(getlocation.toString());
                JSONArray jsonArray = object.getJSONArray("results");
                address.append(jsonArray.getJSONObject(1).optString("name") + "," + jsonArray.getJSONObject(1).optString("vicinity"));
                message.delete(0,message.length());
//                Log.i("gmapsAdress", address.toString());
//                System.out.println("trilok" + address.toString());
                location2.setText(address.toString());
                report = new JSONObject();
                IncidentWall=new JSONObject();
                report.put("userid", tab2.UserID);
                report.put("groupid", tab2.GroupId);
                report.put("incedentid",tab2.IncidentID);
                IncidentWall.put("postedby",tab2.UserID);
                IncidentWall.put("incident",tab2.IncidentID);
                JSONObject locationObject=new JSONObject();
                locationObject.put("latitude", originalLat.toString());
                locationObject.put("longitude", originalLon.toString());
                report.put("location",locationObject);
                report.put("severity", severity.toString());
                report.put("remains", remains.getText().toString());
                message.append("The level of severity here is " + severity.toString());
                message.append(" and number of remains are "+remains.getText().toString());

                super.onPostExecute(aDouble);
            }
            catch(JSONException je){

            }
        }
    }
    public void mapsData(){
        try {
            URL url2 = new URL("https://maps.googleapis.com/maps/api/place/search/json?location="+ originalLat.toString()+","+originalLon.toString()+"&radius=100&sensor=true&key="+key);
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
            originalLat.delete(0,originalLat.length());
            originalLon.delete(0,originalLon.length());
            double lat=location.getLatitude();
            double lon=location.getLongitude();
            originalLat.append(lat);
            originalLon.append(lon);
            latitude = (TextView) v.findViewById(R.id.latitude);
            longitude = (TextView) v.findViewById(R.id.longitude);
            longitudeBuilder.delete(0,longitudeBuilder.length());
            latitudeBuilder.delete(0,latitudeBuilder.length());
            address.delete(0,address.length());
            if(fineAcc.isChecked()) {

                latitude.setText("Latitude: " + String.valueOf(roundTwoDecimals(lat)));
                longitude.setText("Longitude: " + String.valueOf(roundTwoDecimals(lon)));
                if(address.length()==0) {
                    latitudeBuilder.append(String.valueOf(roundTwoDecimals(lat)));
                    longitudeBuilder.append(String.valueOf(roundTwoDecimals(lon)));
                    new MyAsyncTask().execute();
                }
                else
                    address.delete(0,address.length());
            }
            else{
                longitudeBuilder.delete(0,longitudeBuilder.length());
                latitudeBuilder.delete(0,latitudeBuilder.length());
                address.delete(0,address.length());
                originalLat.delete(0,originalLat.length());
                originalLon.delete(0,originalLon.length());
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
            URL url = new URL("http://csgrad07.nwmissouri.edu:3000/memberReports");
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
    public void postIncident(){
//        MultipartUtility multipart = null;
//        try {
//            multipart = new MultipartUtility("http://csgrad07.nwmissouri.edu:3000/addIncedentStatus", "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        multipart.addFormField("message",message.toString());
//        multipart.addFormField("incident",Tab2.IncidentID);
//        multipart.addFormField("postedby",Tab2.UserID);
//        List<String> response = null;
//        try {
//            response = multipart.finish();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("SERVER REPLIED:");
//
//        for (String line : response) {
//            System.out.println(line);
//        }

        try {
            URL url = new URL("http://csgrad07.nwmissouri.edu:3000/addIncedentStatusMobile?message="+URLEncoder.encode(message.toString(),"UTF-8")+"&incident="+URLEncoder.encode(Tab2.IncidentID,"UTF-8")+"&postedby="+URLEncoder.encode(Tab2.UserID,"UTF-8"));
            System.out.println(message.toString());
           message.delete(0,message.length());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            //urlConnection.setRequestProperty("", "UTF-8");
                 //urlConnection.setRequestProperty("Content-Type", "multipart/form-data");
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            urlConnection.connect();
            writer.flush();
            writer.close();

            int HttpResult =urlConnection.getResponseCode();
            System.out.println(HttpResult);
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                StringBuilder responseText=new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseText.append(line);
                }
                br.close();
                System.out.println(responseText.toString());

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
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


}