package com.example.s521950.tabs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import java.util.HashMap;

/**
 * Created by S521950.
 */
public class Tab2 extends Fragment {
    TextView tv, TeamName, memName;
    TextView name2, name3, name4;
    StringBuilder noIncidents = new StringBuilder();
    public static String UserID;
    StringBuilder address = new StringBuilder();
    ArrayList<String> noincidents = new ArrayList<>();
    public static String GroupId;
    public static String IncidentID;
    Switch addView;
    JSONObject object;
    StringBuilder teamNameBuilder = new StringBuilder();
    JSONArray membersArray = new JSONArray();
    ArrayList<Integer> index = new ArrayList<>();
    CardView cv;
    View v;
    ArrayList<String> teamDetails = new ArrayList<>();
    StringBuilder latitudeBuilder = new StringBuilder();
    StringBuilder longitudeBuilder = new StringBuilder();
    StringBuilder TeamBuilder = new StringBuilder();
    String key = "AIzaSyBa5i8jJnXcDB2khD0KwYoJZG0xGAqyBJY";
    TextView name1;
    ArrayList<TextView> namesView = new ArrayList<>();
    HttpURLConnection urlConnection, urlConnection2;
    StringBuilder loc = new StringBuilder();
    TextView dateCreated, location;
    int HttpResult;
    public String input;
    RelativeLayout insertPoint;
    StringBuilder sb = new StringBuilder();
    StringBuilder incidentName = new StringBuilder();
    StringBuilder dateBuilder = new StringBuilder();
static HashMap<String,String> memNames=new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("width", "" + width);
        Log.i("Height", "" + height);
        if (width == 800 && height > 1200) {
            v = inflater.inflate(R.layout.tab2, container, false);
        } else {
            v = inflater.inflate(R.layout.tab2_mobile, container, false);
        }

        TeamName = (TextView) v.findViewById(R.id.TeamName);
        name1 = (TextView) v.findViewById(R.id.Name1);
        name2 = (TextView) v.findViewById(R.id.Name2);
        name3 = (TextView) v.findViewById(R.id.Name3);
        name4 = (TextView) v.findViewById(R.id.Name4);
        namesView.add(name1);
        namesView.add(name2);
        namesView.add(name3);
        namesView.add(name4);
        memName = (TextView) v.findViewById(R.id.memName1);
        tv = (TextView) v.findViewById(R.id.textView);
        addView = (Switch) v.findViewById(R.id.addView);
        dateCreated = (TextView) v.findViewById(R.id.DateCreated);
        location = (TextView) v.findViewById(R.id.Location);
        cv = (CardView) v.findViewById(R.id.cv2);
        insertPoint = (RelativeLayout) v.findViewById(R.id.addlayout);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View newview = layoutInflater.inflate(R.layout.team_details_layout, null);
        insertPoint.removeView(cv);
        new MyAsyncTask().execute();
        addView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (TeamBuilder.toString().length() > 0)
                        insertPoint.addView(cv);
                    else
                        insertPoint.addView(newview);
                } else if (TeamBuilder.toString().length() > 0)
                    insertPoint.removeView(cv);
                else
//            insertPoint.removeView(cv);
                    insertPoint.removeView(newview);

            }
        });
        return v;
    }

    public JSONObject parseJsonGroup(JSONArray groupArray, String name) throws JSONException {
        String[] arr = name.split(" ");
        for (int i = 0; i < groupArray.length(); i++) {
            if (groupArray.getJSONObject(i).getString("fname").equals(arr[0]) && groupArray.getJSONObject(i).getString("lname").equals(arr[1])) {
                return groupArray.getJSONObject(i);
            }
        }


        return null;
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            try {
                postData();

            } catch (JSONException je) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            if (incidentName.toString().length() > 0) {
                tv.setText(incidentName.toString());

                StringBuilder address = new StringBuilder();
                dateCreated.setText(dateBuilder.toString());
                new MapsAsyncTask().execute();

                TeamName.setText(TeamBuilder.toString());
            } else {
                tv.setText(noIncidents.toString());
            }
            try {
                for (int i = 0; i < membersArray.length(); i++) {
                    namesView.get(i).setText(membersArray.getJSONObject(i).getString("fname") + " " + membersArray.getJSONObject(i).getString("lname"));
                }

            } catch (JSONException je1) {

            }
//            animatorSet.play(dateAnim).after(fadeIn);
//            animatorSet.start();
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
                        final String emailSting = tv2.getText().toString();
                        ImageView ib = new ImageView(getContext());
                        ib.setImageResource(R.drawable.emailicon3);
                        ib.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/html");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailSting});

                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                                startActivity(Intent.createChooser(intent, "Send Email"));
                            }
                        });
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        Emaillayout.addView(ib);
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
                        final TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        ImageView ib2 = new ImageView(getContext());
                        ib2.setImageResource(R.drawable.phone2);
                        ib2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                //callIntent.setData(Uri.parse("tel:xxxxxxx")); //This work
                                String number = tv3.getText().toString().trim();
                                number = "tel:" + number;//There work call
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(number)));
                            }
                        });
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        MobileLayout.addView(ib2);
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
                        final String emailSting = tv2.getText().toString();
                        ImageView ib = new ImageView(getContext());
                        ib.setImageResource(R.drawable.emailicon3);
                        ib.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/html");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailSting});

                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                                startActivity(Intent.createChooser(intent, "Send Email"));
                            }
                        });
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        Emaillayout.addView(ib);
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
                        final TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        ImageView ib2 = new ImageView(getContext());
                        ib2.setImageResource(R.drawable.phone2);
                        ib2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                //callIntent.setData(Uri.parse("tel:xxxxxxx")); //This work
                                String number = tv3.getText().toString().trim();
                                number = "tel:" + number;//There work call
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(number)));
                            }
                        });
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        MobileLayout.addView(ib2);
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
                        final String emailSting = tv2.getText().toString();
                        ImageView ib = new ImageView(getContext());
                        ib.setImageResource(R.drawable.emailicon3);
                        ib.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/html");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailSting});

                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                                startActivity(Intent.createChooser(intent, "Send Email"));
                            }
                        });
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        Emaillayout.addView(ib);
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
                        final TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        ImageView ib2 = new ImageView(getContext());
                        ib2.setImageResource(R.drawable.phone2);
                        ib2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                //callIntent.setData(Uri.parse("tel:xxxxxxx")); //This work
                                String number = tv3.getText().toString().trim();
                                number = "tel:" + number;//There work call
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(number)));
                            }
                        });
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        MobileLayout.addView(ib2);
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
                        final String emailSting = tv2.getText().toString();
                        ImageView ib = new ImageView(getContext());
                        ib.setImageResource(R.drawable.emailicon3);
                        ib.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/html");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailSting});

                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                                startActivity(Intent.createChooser(intent, "Send Email"));
                            }
                        });
                        Emaillayout.addView(email);
                        Emaillayout.addView(tv2);
                        Emaillayout.addView(ib);
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
                        final TextView tv3 = new TextView(getContext());
                        tv3.setText(jo.getString("mobile"));
                        tv3.setTextSize(20);
                        ImageView ib2 = new ImageView(getContext());
                        ib2.setImageResource(R.drawable.phone2);
                        ib2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                //callIntent.setData(Uri.parse("tel:xxxxxxx")); //This work
                                String number = tv3.getText().toString().trim();
                                number = "tel:" + number;//There work call
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(number)));
                            }
                        });
                        MobileLayout.addView(mobile);
                        MobileLayout.addView(tv3);
                        MobileLayout.addView(ib2);
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

        }

        public void postData() throws JSONException {
            try {
                URL url = new URL("http://csgrad07.nwmissouri.edu:3000/getIncedents");
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

                while ((input = Reader.readLine()) != null) {
                    //System.out.println(input);
                    sb.append(input);

                }

                Reader.close();
                JSONObject jsonObject1 = new JSONObject(sb.toString());
                String incidents = jsonObject1.getString("incedents");
                JSONArray incidentsArray = new JSONArray(incidents);
                for (int i = 0; i < incidentsArray.length(); i++) {
                    JSONArray array = incidentsArray.getJSONObject(i).getJSONArray("groups");
                    for (int j = 0; j < array.length(); j++) {
                        JSONArray array2 = array.getJSONObject(j).getJSONArray("members");
                        for (int k = 0; k < array2.length(); k++) {

                            JSONObject mem = array2.getJSONObject(k);
                            memNames.put(mem.getString("userid"),mem.getString("fname")+" "+mem.getString("lname"));
                            if (mem.getString("emailid").equals(LoginActivity.EmailId)) {
                                UserID = mem.getString("userid");
                                GroupId = mem.getString("groupid");
                                membersArray = incidentsArray.getJSONObject(i).getJSONArray("groups").getJSONObject(j).getJSONArray("members");
                                incidentName.append(incidentsArray.getJSONObject(i).getString("name"));
                                String date = incidentsArray.getJSONObject(i).getString("createDate");
                                String[] dateArray = date.split("T");
                                String[] formatedDateArray = dateArray[0].split("-");
                                String year = formatedDateArray[0];
                                String month = formatedDateArray[1];
                                String day = formatedDateArray[2];

                                teamNameBuilder.append(incidentsArray.getJSONObject(i).getJSONArray("groups").getJSONObject(j).getString("name"));
                                TeamBuilder.append(incidentsArray.getJSONObject(i).getString("type"));
                                IncidentID = incidentsArray.getJSONObject(i).getString("incedentid");
                                dateBuilder.append(month + "/" + day + "/" + year);
                                latitudeBuilder.append(incidentsArray.getJSONObject(i).getJSONObject("location").getString("latitude"));
                                longitudeBuilder.append(incidentsArray.getJSONObject(i).getJSONObject("location").getString("longitude"));


                            } else {
                                noincidents.add("You are currently not assigned to any incident");
                            }
                            //  System.out.println(incidentsArray.getJSONObject(i).getJSONArray("groups").getJSONObject(j).getJSONArray("members").getJSONObject(k).getString("fname"));
                        }

                    }

                }
                noIncidents.append("you are not currently assigned to any team");
                System.out.println(membersArray.toString());
                System.out.println(teamDetails.toString() + "teamDetails");
                HttpResult = urlConnection.getResponseCode();
                System.out.println(HttpResult);

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }

        public void mapsData() throws JSONException {
            try {
                URL url2 = new URL("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitudeBuilder.toString() + "," + longitudeBuilder.toString() + "&radius=10&sensor=true&key=" + key);
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
                    System.out.println("location:"+loc.toString()+"tabs2");

                }
                int statuscode = urlConnection2.getResponseCode();
                System.out.println(statuscode + "code is");
                JSONArray jsonArray;
                System.out.println(loc.toString());
                try {
                    object = new JSONObject(loc.toString());
                    jsonArray = object.getJSONArray("results");
                    address.append(jsonArray.getJSONObject(0).optString("name") + "," + jsonArray.getJSONObject(0).optString("vicinity"));
                } catch (JSONException e) {
                    System.out.println("catch block executed");

                } finally {
//                    object = new JSONObject(loc.toString());
//                    jsonArray = object.getJSONArray("results");
//                    address.append(jsonArray.getJSONObject(0).optString("name") + "," + jsonArray.getJSONObject(0).optString("vicinity"));
                }

                Log.i("gmapsAdress", address.toString());
                System.out.println("trilok" + address.toString());

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

        private class MapsAsyncTask extends AsyncTask<String, Integer, Double> {
            @Override
            protected Double doInBackground(String... params) {
                try {
                    mapsData();
                } catch (JSONException js) {
                    System.out.println(js.toString());
                }


                return null;
            }

            @Override
            protected void onPostExecute(Double aDouble) {
                super.onPostExecute(aDouble);
                location.setText(address.toString());



            }
        }

        public String parseMaps(String location) {

            return address.toString();
        }
    }
}
