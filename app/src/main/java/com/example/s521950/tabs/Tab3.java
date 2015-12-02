package com.example.s521950.tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Tab3 extends android.support.v4.app.Fragment {
HttpURLConnection urlConnection2;
     HashMap<String,String> messageAndUser;
    ArrayList<String> message=new ArrayList<>();
    StringBuilder loc=new StringBuilder();
    ListView lv;
    CustomAdapter customAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.tab3,container,false);
        lv=(ListView)v.findViewById(R.id.listview);
        new MyAsyncTask().execute();

        return v;
    }

    public void mapsData() throws JSONException {
        try {
            URL url2 = new URL("http://csgrad07.nwmissouri.edu:3000/getIncidentWall?incident="+Tab2.IncidentID);
            urlConnection2 = (HttpURLConnection) url2.openConnection();
            urlConnection2.setDoInput(true);
            urlConnection2.setRequestMethod("GET");
            urlConnection2.setUseCaches(false);
            urlConnection2.setConnectTimeout(10000);
            urlConnection2.setReadTimeout(10000);
            urlConnection2.connect();
            InputStream is2 = urlConnection2.getInputStream();
            BufferedReader Reader2 = new BufferedReader(
                    new InputStreamReader(is2, "UTF-8"));
            String input2 = "";
            while ((input2 = Reader2.readLine()) != null) {
                loc.append(input2);
                //System.out.println("location:"+loc.toString());

            }
            int statuscode = urlConnection2.getResponseCode();
            System.out.println(statuscode + "code is");
            JSONArray jsonArray;

            //System.out.println(loc.toString());
            Reader2.close();
            JSONObject jsonObject=new JSONObject(loc.toString());
            //System.out.println(jsonObject.toString());
            jsonArray=jsonObject.getJSONArray("members");
            messageAndUser=new HashMap<>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jo=jsonArray.getJSONObject(i);
                if(Tab2.memNames.containsKey(jo.getString("postedby"))) {
                    messageAndUser.put(Tab2.memNames.get(jo.getString("postedby"))+" "+i, jo.getString("message"));
                    message.add(messageAndUser.toString());
                }
                else
                    messageAndUser.put("Admin",jo.optString("message"));
            }
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
    private class MyAsyncTask extends AsyncTask<String,Integer,Double> {
        @Override
        protected Double doInBackground(String... params) {
            try {
                mapsData();
            }
            catch (JSONException je){
                System.out.println("Catch block in tabs2");
               // System.out.println(je.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
customAdapter=new CustomAdapter(messageAndUser);
            lv.setAdapter(customAdapter);
            System.out.println("test is"+messageAndUser.toString());
            //System.out.println(Tab2.memNames.toString());
           // System.out.println(loc.toString());
            super.onPostExecute(aDouble);
        }
    }
}
