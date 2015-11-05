package com.example.s521950.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    Toolbar toolbar;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
EditText emailid,password;
    HttpURLConnection urlConnection;
    private long fileSize = 0;
String json;
    int HttpResult;
    StringBuilder sb=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
emailid=(EditText)findViewById(R.id.emailET);
        password=(EditText)findViewById(R.id.passwordET);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.button2);
        Login = (Button) findViewById(R.id.submit);
    }

    public void Login(View v) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("emailid", emailid.getText().toString());
        jsonObject.put("password", password.getText().toString());
        json = jsonObject.toString();
        System.out.println(json);
        new MyAsyncTask().execute();
       if (sb.toString().equals(("invalid"))) {

//            progressBar = new ProgressDialog(v.getContext());
//            progressBar.setCancelable(true);
//            progressBar.setMessage("Logging in ...");
//
//            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressBar.setProgress(0);
//            progressBar.setMax(100);
//            progressBar.show();
//
//            //reset progress bar status
//            progressBarStatus = 0;
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
        protected Double doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {

            super.onPostExecute(aDouble);
        }
    }
    public void postData(){

        try {
            URL url = new URL("http://192.168.0.13:1000/login");
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
                  sb.delete(0,sb.length());
                }
                else{
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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