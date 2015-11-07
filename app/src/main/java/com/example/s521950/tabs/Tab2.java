package com.example.s521950.tabs;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

/**
 * Created by S521950.
 */
public class Tab2 extends Fragment {
    TextView tv,TeamName;
    StringBuilder latitudeBuilder=new StringBuilder();
    StringBuilder longitudeBuilder=new StringBuilder();
    StringBuilder TeamBuilder=new StringBuilder();
    String key="AIzaSyBta5hxAny09giEt88CcQzqBawTpJuXTGY";
    HttpURLConnection urlConnection,urlConnection2;
    StringBuilder loc=new StringBuilder();
TextView dateCreated,location;
    int HttpResult;
    public String input;
    StringBuilder sb=new StringBuilder();
    StringBuilder incidentName=new StringBuilder();
StringBuilder dateBuilder=new StringBuilder();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2,container,false);
TeamName=(TextView)v.findViewById(R.id.TeamName);
        tv=(TextView)v.findViewById(R.id.textView);
        dateCreated=(TextView)v.findViewById(R.id.DateCreated);
        location=(TextView)v.findViewById(R.id.Location);
      new MyAsyncTask().execute();

        return v;
    }
    private class MyAsyncTask extends AsyncTask<String,Integer,Double> {
        @Override
        protected Double doInBackground(String... params) {
            try {
                postData();

            }
            catch (JSONException je){

            }
mapsData();
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble){
            tv.setText(incidentName.toString());
            StringBuilder address=new StringBuilder();
            dateCreated.setText(dateBuilder.toString());
            try {
                JSONObject object = new JSONObject(loc.toString());
                JSONArray jsonArray = object.getJSONArray("results");
                        address.append(jsonArray.getJSONObject(1).optString("name")+","+jsonArray.getJSONObject(1).optString("vicinity"));
                Log.i("gmapsAdress",address.toString());
                System.out.println("trilok" + address.toString());
                location.setText(address.toString());
                }
            catch(JSONException je){

            }

            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(tv, "alpha",
                    0f, 1f);
            fadeIn.setDuration(1000);
            TeamName.setText(TeamBuilder.toString());
            ObjectAnimator dateAnim=ObjectAnimator.ofFloat(dateCreated,"alpha",0f,1f);
            dateAnim.setDuration(1000);
            AnimatorSet animatorSet = new AnimatorSet();

            animatorSet.play(dateAnim).after(fadeIn);
            animatorSet.start();
            System.out.println("latitude in the post" + latitudeBuilder.toString());
            super.onPostExecute(aDouble);
        }
    }
    public void postData() throws JSONException{
        try {
            URL url = new URL("http://192.168.0.13:1000/getIncedents");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            BufferedReader Reader = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));

            while((input=Reader.readLine())!=null){
                //System.out.println(input);
                sb.append(input);

            }

            Reader.close();
            //tv.setText("trilok");
            JSONObject jsonObject1=new JSONObject(sb.toString());
            String incidents=jsonObject1.getString("incedents");
            JSONArray incidentsArray=new JSONArray(incidents);
            for(int i=0;i<incidentsArray.length();i++){
                JSONArray array=incidentsArray.getJSONObject(i).getJSONArray("groups");
                for(int j=0;j<array.length();j++){
                    JSONArray array2=array.getJSONObject(j).getJSONArray("members");
                    for(int k=0;k<array2.length();k++){
                        JSONObject mem=array2.getJSONObject(k);

                        if(mem.getString("emailid").equals(LoginActivity.EmailId)){
                            incidentName.append(incidentsArray.getJSONObject(i).getString("name"));
String date=incidentsArray.getJSONObject(i).getString("createDate");
                            String[] dateArray= date.split("T");
                            String[] formatedDateArray=dateArray[0].split("-");
                            String year=formatedDateArray[0];
                            String month=formatedDateArray[1];
                            String day = formatedDateArray[2];
                            TeamBuilder.append(incidentsArray.getJSONObject(i).getJSONArray("groups").getJSONObject(j).getString("name"));
                            dateBuilder.append(month + "/" + day + "/" + year);
                            latitudeBuilder.append(incidentsArray.getJSONObject(i).getJSONObject("location").getString("latitude"));
                            longitudeBuilder.append(incidentsArray.getJSONObject(i).getJSONObject("location").getString("longitude"));
                           // System.out.println(latitudeBuilder.toString() + " " + longitudeBuilder.toString() + "");
                           // Log.i("Trilok",latitudeBuilder.toString()+""+longitudeBuilder.toString());
                        }
                    }
                }
            }
            HttpResult =urlConnection.getResponseCode();
            System.out.println(HttpResult);

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
                loc.append(input2);

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
}
