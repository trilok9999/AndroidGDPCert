package com.example.s521950.tabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    Button button;
    Button Login;
    AlertDialog.Builder al;
    Toolbar toolbar;
    public static String EmailId;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
EditText emailid,password;
    TextView errorText;
    ImageView errorImage;
    StringBuilder temp=new StringBuilder();
    HttpURLConnection urlConnection;
    private long fileSize = 0;
String json;
    int HttpResult;
    StringBuilder sb=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("width",""+width);
        Log.i("Height", "" + height);
        if(width==800&&height>1200) {
            setContentView(R.layout.login);
        }
        else{
            setContentView(R.layout.login_mobile);
        }

emailid=(EditText)findViewById(R.id.emailET);
        errorText=(TextView)findViewById(R.id.errorText);
        errorImage=(ImageView)findViewById(R.id.errorImage);
        password=(EditText)findViewById(R.id.passwordET);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.button2);
        Login = (Button) findViewById(R.id.submit);
    }

    public void Login(View v) throws JSONException {
        TabsActivity ta=new TabsActivity();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("emailid", emailid.getText().toString());
        jsonObject.put("password", password.getText().toString());
        json = jsonObject.toString();
        System.out.println(json);
        new MyAsyncTask().execute();
        String jsonData="{\"success\":true,\"incedents\":[{\"name\":\"1\",\"type\":\"Electricity\",\"groups\":[{\"name\":\"1\",\"members\":[{\"fname\":\"prasanna\",\"lname\":\"k\",\"emailid\":\"d@g.com\",\"mobile\":\"6605412788\",\"county\":\"nod\",\"bloodgrp\":\"O+\",\"userid\":\"f17a7850-697a-11e5-8fe8-d394396cbb51\",\"groupid\":\"a46f0b00-6cfb-11e5-a001-33d59d12e68b\",\"img\":\"./f17a7850-697a-11e5-8fe8-d394396cbb51.jpg\"},{\"fname\":\"virshi\",\"lname\":\"y\",\"emailid\":\"t@g.com\",\"mobile\":\"6605412788\",\"county\":\"nod\",\"bloodgrp\":\"O+\",\"userid\":\"b7a69f80-6c97-11e5-a001-33d59d12e68b\",\"groupid\":\"a46f0b00-6cfb-11e5-a001-33d59d12e68b\",\"img\":\"./b7a69f80-6c97-11e5-a001-33d59d12e68b.jpg\"}],\"groupid\":\"a46f0b00-6cfb-11e5-a001-33d59d12e68b\"}],\"location\":{\"latitude\":90.45,\"longitude\":40.67},\"incedentid\":\"9be29470-6d5a-11e5-a001-33d59d12e68b\",\"status\":\"A\",\"createDate\":\"2015-10-08T01:19:23.447Z\",\"id\":\"9be29470-6d5a-11e5-a001-33d59d12e68b\"},{\"name\":\"\",\"type\":\"\",\"groups\":[],\"location\":{\"latitude\":null,\"longitude\":null},\"incedentid\":\"8951eff0-6d5e-11e5-a001-33d59d12e68b\",\"status\":\"A\",\"createDate\":\"2015-10-08T01:47:30.287Z\",\"id\":\"8951eff0-6d5e-11e5-a001-33d59d12e68b\"},{\"name\":\"Incident 1\",\"type\":\"Electricity\",\"groups\":[{\"name\":\"Group A\",\"members\":[{\"fname\":\"John\",\"lname\":\"Thomson\",\"emailid\":\"John@nw.com\",\"mobile\":\"6605412788\",\"county\":\"Nodaway\",\"bloodgrp\":\"O+\",\"userid\":\"f38d6ab0-7cbe-11e5-8d2e-11d19f7eb880\",\"groupid\":\"ac237c80-7cc0-11e5-8d2e-11d19f7eb880\",\"img\":\"./f38d6ab0-7cbe-11e5-8d2e-11d19f7eb880.jpg\"},{\"fname\":\"Oliver\",\"lname\":\"Queen\",\"emailid\":\"Oliver@nw.com\",\"mobile\":\"6605412788\",\"county\":\"nodaway\",\"bloodgrp\":\"B+\",\"userid\":\"425bd320-7cbf-11e5-8d2e-11d19f7eb880\",\"groupid\":\"ac237c80-7cc0-11e5-8d2e-11d19f7eb880\",\"img\":\"./425bd320-7cbf-11e5-8d2e-11d19f7eb880.jpg\"},{\"fname\":\"Ray\",\"lname\":\"Palmer\",\"emailid\":\"Ray@nw.com\",\"mobile\":\"6605412788\",\"county\":\"Nodaway\",\"bloodgrp\":\"B+\",\"userid\":\"6230ef00-7cbf-11e5-8d2e-11d19f7eb880\",\"groupid\":\"ac237c80-7cc0-11e5-8d2e-11d19f7eb880\",\"img\":\"./6230ef00-7cbf-11e5-8d2e-11d19f7eb880.jpg\"}],\"leader\":{\"userid\":\"f38d6ab0-7cbe-11e5-8d2e-11d19f7eb880\"},\"groupid\":\"ac237c80-7cc0-11e5-8d2e-11d19f7eb880\"}],\"location\":{\"latitude\":40.356,\"longitude\":-94.882},\"incedentid\":\"e18b0e60-7cc0-11e5-8d2e-11d19f7eb880\",\"status\":\"A\",\"createDate\":\"2015-10-27T15:39:16.422Z\",\"id\":\"e18b0e60-7cc0-11e5-8d2e-11d19f7eb880\"},{\"name\":\"Incident 2\",\"type\":\"Tornado\",\"groups\":[{\"name\":\"Group B\",\"members\":[{\"fname\":\"Jessica\",\"lname\":\"Jones\",\"emailid\":\"Jessica@nw.com\",\"mobile\":\"6605412788\",\"county\":\"Nodaway\",\"bloodgrp\":\"O+\",\"userid\":\"24b2bf00-7cbf-11e5-8d2e-11d19f7eb880\",\"groupid\":\"bcce4d30-7cc0-11e5-8d2e-11d19f7eb880\",\"img\":\"./24b2bf00-7cbf-11e5-8d2e-11d19f7eb880.jpg\"},{\"fname\":\"Nickson\",\"lname\":\"Graham\",\"emailid\":\"Nickson@nw.com\",\"mobile\":\"6605412788\",\"county\":\"nodaway\",\"bloodgrp\":\"B+\",\"userid\":\"86478520-7cbf-11e5-8d2e-11d19f7eb880\",\"groupid\":\"bcce4d30-7cc0-11e5-8d2e-11d19f7eb880\",\"img\":\"./86478520-7cbf-11e5-8d2e-11d19f7eb880.jpg\"},{\"fname\":\"Lex\",\"lname\":\"Luther\",\"emailid\":\"Lex@nw.com\",\"mobile\":\"6605412788\",\"county\":\"nodaway\",\"bloodgrp\":\"B+\",\"userid\":\"ab49dad0-7cbf-11e5-8d2e-11d19f7eb880\",\"groupid\":\"bcce4d30-7cc0-11e5-8d2e-11d19f7eb880\",\"img\":\"./ab49dad0-7cbf-11e5-8d2e-11d19f7eb880.jpg\"},{\"fname\":\"Marclum\",\"lname\":\"Merlen\",\"emailid\":\"Marclum@nw.com\",\"mobile\":\"6605412788\",\"county\":\"nodaway\",\"bloodgrp\":\"AB+\",\"userid\":\"cfaedf10-7cbf-11e5-8d2e-11d19f7eb880\",\"groupid\":\"bcce4d30-7cc0-11e5-8d2e-11d19f7eb880\",\"img\":\"./cfaedf10-7cbf-11e5-8d2e-11d19f7eb880.jpg\"}],\"leader\":{\"userid\":\"24b2bf00-7cbf-11e5-8d2e-11d19f7eb880\"},\"groupid\":\"bcce4d30-7cc0-11e5-8d2e-11d19f7eb880\"}],\"location\":{\"latitude\":38.5,\"longitude\":98},\"incedentid\":\"066d31e0-7cc1-11e5-8d2e-11d19f7eb880\",\"status\":\"A\",\"createDate\":\"2015-10-27T15:40:18.302Z\",\"id\":\"066d31e0-7cc1-11e5-8d2e-11d19f7eb880\"}]}";
        JSONObject jsonObject1=new JSONObject(jsonData);
        String incidents=jsonObject1.getString("incedents");
        JSONArray incidentsArray=new JSONArray(incidents);
        for(int i=0;i<incidentsArray.length();i++){
            JSONArray array=incidentsArray.getJSONObject(i).getJSONArray("groups");
            for(int j=0;j<array.length();j++){
                JSONArray array2=array.getJSONObject(j).getJSONArray("members");
                for(int k=0;k<array2.length();k++){
                    JSONObject mem=array2.getJSONObject(k);
                    Log.i("json",mem.toString());
                    if(mem.getString("emailid").equals(emailid.getText().toString())){
                        Log.i("json",mem.toString());

                    }
                }
            }

        }
        if (sb.toString().equals(("invalid")) || sb.toString().equals("inactive")) {



//
//            //reset filesize
//            fileSize = 0;
//
//            new Thread(new Runnable() {
//                public void run() {
//                    while (progressBarStatus < 100) {
//
//                        // process some tasks
//                        progressBarStatus = doSomeTasks();
//
//                        // your computer is too fast, sleep 1 second
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        // Update the progress bar
//                        progressBarHandler.post(new Runnable() {
//                            public void run() {
//                                progressBar.setProgress(progressBarStatus);
//                            }
//                        });
//                    }
//
//                    // ok, file is downloaded,
//                    if (progressBarStatus >= 100) {
//
//                        // sleep 2 seconds, so that you can see the 100%
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        // close the progress bar dialog
//                        progressBar.dismiss();
//                    }

       }
        else {

       }
//                }
//            }).start();


    }

    private class MyAsyncTask extends AsyncTask<String,Integer,Double> {
        @Override
        protected void onPreExecute() {
                        progressBar = new ProgressDialog(LoginActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Logging in ...");

            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();

            //reset progress bar status
//            progressBarStatus = 0;
            super.onPreExecute();
        }

        @Override
        protected Double doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
           if ((temp.toString().contains(("invalid")) || temp.toString().contains(("inactive")))&&(!temp.toString().contains("fname"))) {

//   al=new AlertDialog.Builder(LoginActivity.this);
//              // al.setIcon(R.drawable.home);
//al.setView(R.layout.custom_dialog);
//    al.setMessage("Login Failed. Please Try Again");
//    al.create().show();
               errorImage.setImageResource(R.drawable.error);
               errorText.setText("log in failed");
               emailid.setText("");
               password.setText("");
               emailid.setFocusable(true);
           } else{
               errorImage.setImageResource(0);
               errorText.setText("");

               temp.delete(0,temp.length());
           }
            progressBar.dismiss();
            super.onPostExecute(aDouble);
        }
    }
    public void postData(){

        try {
            URL url = new URL("http://csgrad07.nwmissouri.edu:3000/login");
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


            HttpResult =urlConnection.getResponseCode();
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
                if (!(sb.toString().equals(("invalid")) || sb.toString().equals(("inactive")))) {
                    Intent tabs = new Intent(getApplicationContext(), TabsActivity.class);
                    startActivity(tabs);
                    EmailId=emailid.getText().toString();
                    Log.i("EMAILID", EmailId);
                    temp.append(sb.toString());
                  sb.delete(0, sb.length());

                }
                else{
                    temp.append(sb.toString());
                    sb.delete(0, sb.length());

                }
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


public void Register(View v){

    Intent intent = new Intent(this, Registration.class);
    startActivity(intent);
}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public int doSomeTasks() {

        while (fileSize <= 1000000) {

            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            }
            // ...add your own

        }

        return 100;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}