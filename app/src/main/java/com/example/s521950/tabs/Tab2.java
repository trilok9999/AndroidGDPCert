package com.example.s521950.tabs;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import java.util.ArrayList;

/**
 * Created by S521950.
 */
public class Tab2 extends Fragment {
    TextView tv,TeamName,memName;
    TextView name2,name3,name4;
    public static String UserID;
    public static String GroupId;
    Switch addView;
    StringBuilder teamNameBuilder=new StringBuilder();
    JSONArray membersArray;
    ArrayList<Integer> index=new ArrayList<>();
    CardView cv;
    ArrayList<String> teamDetails=new ArrayList<>();
    StringBuilder latitudeBuilder=new StringBuilder();
    StringBuilder longitudeBuilder=new StringBuilder();
    StringBuilder TeamBuilder=new StringBuilder();
    String key="AIzaSyBa5i8jJnXcDB2khD0KwYoJZG0xGAqyBJY";
    TextView name1;
    ArrayList<TextView> namesView=new ArrayList<>();
    HttpURLConnection urlConnection,urlConnection2;
    StringBuilder loc=new StringBuilder();
TextView dateCreated,location;
    int HttpResult;
    public String input;
    RelativeLayout insertPoint;
    StringBuilder sb=new StringBuilder();
    StringBuilder incidentName=new StringBuilder();
StringBuilder dateBuilder=new StringBuilder();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2,container,false);
TeamName=(TextView)v.findViewById(R.id.TeamName);

        name1=(TextView)v.findViewById(R.id.Name1);
        name2=(TextView)v.findViewById(R.id.Name2);
        name3=(TextView)v.findViewById(R.id.Name3);
        name4=(TextView)v.findViewById(R.id.Name4);
        namesView.add(name1);
        namesView.add(name2);
        namesView.add(name3);
        namesView.add(name4);
        memName=(TextView)v.findViewById(R.id.memName1);
        tv=(TextView)v.findViewById(R.id.textView);
        addView=(Switch)v.findViewById(R.id.addView);
        dateCreated=(TextView)v.findViewById(R.id.DateCreated);
        location=(TextView)v.findViewById(R.id.Location);
        cv=(CardView)v.findViewById(R.id.cv2);
         insertPoint=(RelativeLayout)v.findViewById(R.id.addlayout);
        insertPoint.removeView(cv);
      new MyAsyncTask().execute();

addView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            insertPoint.addView(cv);
        }
        else
            insertPoint.removeView(cv);
    }
});
        return v;
    }
    public JSONObject parseJsonGroup(JSONArray groupArray,String name) throws JSONException{
        String[] arr=name.split(" ");
for(int i=0;i<groupArray.length();i++){
    if(groupArray.getJSONObject(i).getString("fname").equals(arr[0])&&groupArray.getJSONObject(i).getString("lname").equals(arr[1])){
        return groupArray.getJSONObject(i);
    }
}


        return null;
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
        protected void onPostExecute(Double aDouble) {
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
            try {
                for(int i=0;i<membersArray.length();i++){
                    namesView.get(i).setText(membersArray.getJSONObject(i).getString("fname")+" "+membersArray.getJSONObject(i).getString("lname"));
                }

                }

            catch(JSONException je1){

            }
            animatorSet.play(dateAnim).after(fadeIn);
            animatorSet.start();
            System.out.println("latitude in the post" + latitudeBuilder.toString());
            name1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jo = parseJsonGroup(membersArray, name1.getText().toString());

                        AlertDialog.Builder details = new AlertDialog.Builder(getContext());
                        LinearLayout layout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setLayoutParams(parms);
                        layout.setGravity(Gravity.CLIP_VERTICAL);
                        layout.setPadding(2, 2, 2, 2);
                        TextView tv = new TextView(getContext());
                        tv.setText(teamNameBuilder.toString());
                        tv.setPadding(40, 40, 40, 40);
                        tv.setGravity(Gravity.CENTER);
                        int RGB = android.graphics.Color.rgb(118, 201, 69);
                        tv.setBackgroundColor(RGB);
                        tv.setTextSize(20);
                        LinearLayout namelayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams namelayoutparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        namelayout.setOrientation(LinearLayout.HORIZONTAL);
                        namelayout.setLayoutParams(namelayoutparms);
                        namelayout.setGravity(Gravity.CLIP_VERTICAL);
                        namelayout.setPadding(2, 2, 2, 2);
                        TextView name = new TextView(getContext());
                        name.setText("Name: ");
                        name.setTypeface(null, Typeface.BOLD_ITALIC);
                        name.setTextSize(20);
                        TextView tv1 = new TextView(getContext());
                        tv1.setText(jo.getString("fname") + " " + jo.getString("lname"));

                        tv1.setTextSize(20);
                        namelayout.addView(name);
                        namelayout.addView(tv1);
                        LinearLayout Emaillayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Emailparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Emaillayout.setOrientation(LinearLayout.HORIZONTAL);
                        Emaillayout.setLayoutParams(namelayoutparms);
                        Emaillayout.setGravity(Gravity.CLIP_VERTICAL);
                        Emaillayout.setPadding(2, 2, 2, 2);
                        TextView email = new TextView(getContext());
                        email.setText("Email: ");
                        email.setTypeface(null, Typeface.BOLD_ITALIC);
                        email.setTextSize(20);
                        TextView tv2 = new TextView(getContext());
                        tv2.setText(jo.getString("emailid"));
                        tv2.setTextSize(20);
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        LinearLayout MobileLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Mobileparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        MobileLayout.setOrientation(LinearLayout.HORIZONTAL);
                        MobileLayout.setLayoutParams(namelayoutparms);
                        MobileLayout.setGravity(Gravity.CLIP_VERTICAL);
                        MobileLayout.setPadding(2, 2, 2, 2);
                        TextView mobile = new TextView(getContext());
                        mobile.setText("Mobile: ");

                        mobile.setTypeface(null, Typeface.BOLD_ITALIC);
                        mobile.setTextSize(20);
                        TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        LinearLayout CountyLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Countyparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        CountyLayout.setOrientation(LinearLayout.HORIZONTAL);
                        CountyLayout.setLayoutParams(namelayoutparms);
                        CountyLayout.setGravity(Gravity.CLIP_VERTICAL);
                        CountyLayout.setPadding(2, 2, 2, 2);
                        TextView county = new TextView(getContext());
                        county.setText("County: ");
                        county.setTypeface(null, Typeface.BOLD_ITALIC);
                        county.setTextSize(20);
                        TextView tv4 = new TextView(getContext());
                        tv4.setText(jo.getString("county"));
                        tv4.setTextSize(20);
                        CountyLayout.addView(county);
                        CountyLayout.addView(tv4);

                        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        tv1Params.bottomMargin = 5;
//            layout.addView(tv1,tv1Params);
                        //layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(namelayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(Emaillayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(MobileLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(CountyLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        details.setView(layout);
                        //details.setTitle("trilpok");
                        // details.setMessage("Input Student ID");
                        details.setCustomTitle(tv);


                        // details.setMessage(message);
                        details.setCancelable(false);

                        // Setting Negative "Cancel" Button


                        // Setting Positive "Yes" Button
                        details.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                        AlertDialog alertDialog = details.create();

                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            // WindowManager$BadTokenException will be caught and the app would
                            // not display the 'Force Close' message
                            e.printStackTrace();
                        }
                    } catch (JSONException je) {

                    }
                }
            });
            name2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jo = parseJsonGroup(membersArray, name2.getText().toString());

                        AlertDialog.Builder details = new AlertDialog.Builder(getContext());
                        LinearLayout layout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setLayoutParams(parms);
                        layout.setGravity(Gravity.CLIP_VERTICAL);
                        layout.setPadding(2, 2, 2, 2);
                        TextView tv = new TextView(getContext());
                        tv.setText(teamNameBuilder.toString());
                        tv.setPadding(40, 40, 40, 40);
                        tv.setGravity(Gravity.CENTER);
                        int RGB = android.graphics.Color.rgb(118, 201, 69);
                        tv.setBackgroundColor(RGB);
                        tv.setTextSize(20);
                        LinearLayout namelayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams namelayoutparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        namelayout.setOrientation(LinearLayout.HORIZONTAL);
                        namelayout.setLayoutParams(namelayoutparms);
                        namelayout.setGravity(Gravity.CLIP_VERTICAL);
                        namelayout.setPadding(2, 2, 2, 2);
                        TextView name = new TextView(getContext());
                        name.setText("Name: ");
                        name.setTypeface(null, Typeface.BOLD_ITALIC);
                        name.setTextSize(20);
                        TextView tv1 = new TextView(getContext());
                        tv1.setText(jo.getString("fname") + " " + jo.getString("lname"));

                        tv1.setTextSize(20);
                        namelayout.addView(name);
                        namelayout.addView(tv1);
                        LinearLayout Emaillayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Emailparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Emaillayout.setOrientation(LinearLayout.HORIZONTAL);
                        Emaillayout.setLayoutParams(namelayoutparms);
                        Emaillayout.setGravity(Gravity.CLIP_VERTICAL);
                        Emaillayout.setPadding(2, 2, 2, 2);
                        TextView email = new TextView(getContext());
                        email.setText("Email: ");
                        email.setTypeface(null, Typeface.BOLD_ITALIC);
                        email.setTextSize(20);
                        TextView tv2 = new TextView(getContext());
                        tv2.setText(jo.getString("emailid"));
                        tv2.setTextSize(20);
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        LinearLayout MobileLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Mobileparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        MobileLayout.setOrientation(LinearLayout.HORIZONTAL);
                        MobileLayout.setLayoutParams(namelayoutparms);
                        MobileLayout.setGravity(Gravity.CLIP_VERTICAL);
                        MobileLayout.setPadding(2, 2, 2, 2);
                        TextView mobile = new TextView(getContext());
                        mobile.setText("Mobile: ");

                        mobile.setTypeface(null, Typeface.BOLD_ITALIC);
                        mobile.setTextSize(20);
                        TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        LinearLayout CountyLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Countyparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        CountyLayout.setOrientation(LinearLayout.HORIZONTAL);
                        CountyLayout.setLayoutParams(namelayoutparms);
                        CountyLayout.setGravity(Gravity.CLIP_VERTICAL);
                        CountyLayout.setPadding(2, 2, 2, 2);
                        TextView county = new TextView(getContext());
                        county.setText("County: ");
                        county.setTypeface(null, Typeface.BOLD_ITALIC);
                        county.setTextSize(20);
                        TextView tv4 = new TextView(getContext());
                        tv4.setText(jo.getString("county"));
                        tv4.setTextSize(20);
                        CountyLayout.addView(county);
                        CountyLayout.addView(tv4);

                        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        tv1Params.bottomMargin = 5;
//            layout.addView(tv1,tv1Params);
                        //layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(namelayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(Emaillayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(MobileLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(CountyLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        details.setView(layout);
                        //details.setTitle("trilpok");
                        // details.setMessage("Input Student ID");
                        details.setCustomTitle(tv);


                        // details.setMessage(message);
                        details.setCancelable(false);

                        // Setting Negative "Cancel" Button


                        // Setting Positive "Yes" Button
                        details.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                        AlertDialog alertDialog = details.create();

                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            // WindowManager$BadTokenException will be caught and the app would
                            // not display the 'Force Close' message
                            e.printStackTrace();
                        }
                    } catch (JSONException je) {

                    }
                }
            });
            name3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jo = parseJsonGroup(membersArray, name3.getText().toString());

                        AlertDialog.Builder details = new AlertDialog.Builder(getContext());
                        LinearLayout layout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setLayoutParams(parms);
                        layout.setGravity(Gravity.CLIP_VERTICAL);
                        layout.setPadding(2, 2, 2, 2);
                        TextView tv = new TextView(getContext());
                        tv.setText(teamNameBuilder.toString());
                        tv.setPadding(40, 40, 40, 40);
                        tv.setGravity(Gravity.CENTER);
                        int RGB = android.graphics.Color.rgb(118, 201, 69);
                        tv.setBackgroundColor(RGB);
                        tv.setTextSize(20);
                        LinearLayout namelayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams namelayoutparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        namelayout.setOrientation(LinearLayout.HORIZONTAL);
                        namelayout.setLayoutParams(namelayoutparms);
                        namelayout.setGravity(Gravity.CLIP_VERTICAL);
                        namelayout.setPadding(2, 2, 2, 2);
                        TextView name = new TextView(getContext());
                        name.setText("Name: ");
                        name.setTypeface(null, Typeface.BOLD_ITALIC);
                        name.setTextSize(20);
                        TextView tv1 = new TextView(getContext());
                        tv1.setText(jo.getString("fname") + " " + jo.getString("lname"));

                        tv1.setTextSize(20);
                        namelayout.addView(name);
                        namelayout.addView(tv1);
                        LinearLayout Emaillayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Emailparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Emaillayout.setOrientation(LinearLayout.HORIZONTAL);
                        Emaillayout.setLayoutParams(namelayoutparms);
                        Emaillayout.setGravity(Gravity.CLIP_VERTICAL);
                        Emaillayout.setPadding(2, 2, 2, 2);
                        TextView email = new TextView(getContext());
                        email.setText("Email: ");
                        email.setTypeface(null, Typeface.BOLD_ITALIC);
                        email.setTextSize(20);
                        TextView tv2 = new TextView(getContext());
                        tv2.setText(jo.getString("emailid"));
                        tv2.setTextSize(20);
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        LinearLayout MobileLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Mobileparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        MobileLayout.setOrientation(LinearLayout.HORIZONTAL);
                        MobileLayout.setLayoutParams(namelayoutparms);
                        MobileLayout.setGravity(Gravity.CLIP_VERTICAL);
                        MobileLayout.setPadding(2, 2, 2, 2);
                        TextView mobile = new TextView(getContext());
                        mobile.setText("Mobile: ");

                        mobile.setTypeface(null, Typeface.BOLD_ITALIC);
                        mobile.setTextSize(20);
                        TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        LinearLayout CountyLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Countyparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        CountyLayout.setOrientation(LinearLayout.HORIZONTAL);
                        CountyLayout.setLayoutParams(namelayoutparms);
                        CountyLayout.setGravity(Gravity.CLIP_VERTICAL);
                        CountyLayout.setPadding(2, 2, 2, 2);
                        TextView county = new TextView(getContext());
                        county.setText("County: ");
                        county.setTypeface(null, Typeface.BOLD_ITALIC);
                        county.setTextSize(20);
                        TextView tv4 = new TextView(getContext());
                        tv4.setText(jo.getString("county"));
                        tv4.setTextSize(20);
                        CountyLayout.addView(county);
                        CountyLayout.addView(tv4);

                        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        tv1Params.bottomMargin = 5;
//            layout.addView(tv1,tv1Params);
                        //layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(namelayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(Emaillayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(MobileLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(CountyLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        details.setView(layout);
                        //details.setTitle("trilpok");
                        // details.setMessage("Input Student ID");
                        details.setCustomTitle(tv);


                        // details.setMessage(message);
                        details.setCancelable(false);

                        // Setting Negative "Cancel" Button


                        // Setting Positive "Yes" Button
                        details.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                        AlertDialog alertDialog = details.create();

                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            // WindowManager$BadTokenException will be caught and the app would
                            // not display the 'Force Close' message
                            e.printStackTrace();
                        }
                    } catch (JSONException je) {

                    }
                }
            });
            name4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jo = parseJsonGroup(membersArray, name4.getText().toString());

                        AlertDialog.Builder details = new AlertDialog.Builder(getContext());
                        LinearLayout layout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setLayoutParams(parms);
                        layout.setGravity(Gravity.CLIP_VERTICAL);
                        layout.setPadding(2, 2, 2, 2);
                        TextView tv = new TextView(getContext());
                        tv.setText(teamNameBuilder.toString());
                        tv.setPadding(40, 40, 40, 40);
                        tv.setGravity(Gravity.CENTER);
                        int RGB = android.graphics.Color.rgb(118, 201, 69);
                        tv.setBackgroundColor(RGB);
                        tv.setTextSize(20);
                        LinearLayout namelayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams namelayoutparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        namelayout.setOrientation(LinearLayout.HORIZONTAL);
                        namelayout.setLayoutParams(namelayoutparms);
                        namelayout.setGravity(Gravity.CLIP_VERTICAL);
                        namelayout.setPadding(2, 2, 2, 2);
                        TextView name = new TextView(getContext());
                        name.setText("Name: ");
                        name.setTypeface(null, Typeface.BOLD_ITALIC);
                        name.setTextSize(20);
                        TextView tv1 = new TextView(getContext());
                        tv1.setText(jo.getString("fname") + " " + jo.getString("lname"));

                        tv1.setTextSize(20);
                        namelayout.addView(name);
                        namelayout.addView(tv1);
                        LinearLayout Emaillayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Emailparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Emaillayout.setOrientation(LinearLayout.HORIZONTAL);
                        Emaillayout.setLayoutParams(namelayoutparms);
                        Emaillayout.setGravity(Gravity.CLIP_VERTICAL);
                        Emaillayout.setPadding(2, 2, 2, 2);
                        TextView email = new TextView(getContext());
                        email.setText("Email: ");
                        email.setTypeface(null, Typeface.BOLD_ITALIC);
                        email.setTextSize(20);
                        TextView tv2 = new TextView(getContext());
                        tv2.setText(jo.getString("emailid"));
                        tv2.setTextSize(20);
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        LinearLayout MobileLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Mobileparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        MobileLayout.setOrientation(LinearLayout.HORIZONTAL);
                        MobileLayout.setLayoutParams(namelayoutparms);
                        MobileLayout.setGravity(Gravity.CLIP_VERTICAL);
                        MobileLayout.setPadding(2, 2, 2, 2);
                        TextView mobile = new TextView(getContext());
                        mobile.setText("Mobile: ");

                        mobile.setTypeface(null, Typeface.BOLD_ITALIC);
                        mobile.setTextSize(20);
                        TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        LinearLayout CountyLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams Countyparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        CountyLayout.setOrientation(LinearLayout.HORIZONTAL);
                        CountyLayout.setLayoutParams(namelayoutparms);
                        CountyLayout.setGravity(Gravity.CLIP_VERTICAL);
                        CountyLayout.setPadding(2, 2, 2, 2);
                        TextView county = new TextView(getContext());
                        county.setText("County: ");
                        county.setTypeface(null, Typeface.BOLD_ITALIC);
                        county.setTextSize(20);
                        TextView tv4 = new TextView(getContext());
                        tv4.setText(jo.getString("county"));
                        tv4.setTextSize(20);
                        CountyLayout.addView(county);
                        CountyLayout.addView(tv4);

                        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        tv1Params.bottomMargin = 5;
//            layout.addView(tv1,tv1Params);
                        //layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(namelayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(Emaillayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(MobileLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(CountyLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        details.setView(layout);
                        //details.setTitle("trilpok");
                        // details.setMessage("Input Student ID");
                        details.setCustomTitle(tv);


                        // details.setMessage(message);
                        details.setCancelable(false);

                        // Setting Negative "Cancel" Button


                        // Setting Positive "Yes" Button
                        details.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                        AlertDialog alertDialog = details.create();

                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            // WindowManager$BadTokenException will be caught and the app would
                            // not display the 'Force Close' message
                            e.printStackTrace();
                        }
                    } catch (JSONException je) {

                    }
                }
            });
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
                            UserID=mem.getString("userid");
                            GroupId=mem.getString("groupid");
                             membersArray=  incidentsArray.getJSONObject(i).getJSONArray("groups").getJSONObject(j).getJSONArray("members");
                            incidentName.append(incidentsArray.getJSONObject(i).getString("name"));
                            String date=incidentsArray.getJSONObject(i).getString("createDate");
                            String[] dateArray= date.split("T");
                            String[] formatedDateArray=dateArray[0].split("-");
                            String year=formatedDateArray[0];
                            String month=formatedDateArray[1];
                            String day = formatedDateArray[2];
                            teamNameBuilder.append(incidentsArray.getJSONObject(i).getJSONArray("groups").getJSONObject(j).getString("name"));
                            TeamBuilder.append(incidentsArray.getJSONObject(i).getString("type"));
                            dateBuilder.append(month + "/" + day + "/" + year);
                            latitudeBuilder.append(incidentsArray.getJSONObject(i).getJSONObject("location").getString("latitude"));
                            longitudeBuilder.append(incidentsArray.getJSONObject(i).getJSONObject("location").getString("longitude"));


                        }
                      //  System.out.println(incidentsArray.getJSONObject(i).getJSONArray("groups").getJSONObject(j).getJSONArray("members").getJSONObject(k).getString("fname"));


                    }

                }

            }
            System.out.println(membersArray.toString());
            System.out.println(teamDetails.toString()+"teamDetails");
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
