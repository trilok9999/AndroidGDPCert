package com.example.s521950.tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Registration extends AppCompatActivity {
    Toolbar toolbar;
    static String testfname;
    String url2;
    StringBuilder fname=new StringBuilder();
    HttpURLConnection urlConnection=null;
    Button button;
    ImageView ivImage;
    Button btn;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    Button reset;
    String https = "http://192.168.0.14:1000/registerMobi";
    ArrayList<RegistrationPage> registrationPages=new ArrayList<>();
    public EditText firstName,lastName,email,mobile,county,bloodgrp;
    EditText password;
    String Json;
    StringBuilder sb = new StringBuilder();
    URL url;
    URLConnection urlConn;
    DataOutputStream printout;
    DataInputStream  input;
    File destination;
    String selectedImagePath;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    Bitmap bv;
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
            setContentView(R.layout.registration);
        }
        else{
            setContentView(R.layout.registration_mobile);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button=(Button)findViewById(R.id.SignupET);
        btn=(Button)findViewById(R.id.ImageButton);
        lastName=((EditText)findViewById(R.id.LastNameET));
        firstName=(EditText)findViewById(R.id.FirstNameET);
        email=(EditText)findViewById(R.id.EmailET);
        password=(EditText)findViewById(R.id.Password);
        mobile=(EditText)findViewById(R.id.Mobile);
        county=(EditText)findViewById(R.id.CountyName);
        bloodgrp=(EditText)findViewById(R.id.BloodGroup);
        reset=(Button)findViewById(R.id.ResetET);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(R.layout.registration_mobile);
            }
        });


    }

    public void selectImage2(View v) {
        ivImage=(ImageView)findViewById(R.id.UploadImage);


        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ivImage.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                ivImage.setImageBitmap(bm);
            }
        }
    }

    public void Signup(View v){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        //registrationPages.add(new RegistrationPage(lastName.getText().toString(),firstName.getText().toString(),email.getText().toString(),password.getText().toString()));
       // Gson gson = new Gson();
        //Json=gson.toJson(new RegistrationPage(lastName.getText().toString(),firstName.getText().toString(),email.getText().toString(),password.getText().toString()));


        builder2.setTitle("Are you sure, do you want to submit the form ?");
        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fname.append(firstName.getText().toString());
//            Toast.makeText(getApplicationContext(), Json, Toast.LENGTH_LONG).show();
               // Toast.makeText(getApplicationContext(), selectedImagePath, Toast.LENGTH_LONG).show();
                new MyAsyncTask().execute();
                finish();

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

    private class MyAsyncTask extends AsyncTask<String,Integer,Double>{
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();

            Log.i("fname",fname.toString());
//            url2="http://csgrad07.nwmissouri.edu:3000/registerMobi?fname="+firstName.getText().toString()+"&lname="+lastName.getText().toString()+"&emailid="+email.getText().toString()+"&password="+password.getText().toString()+"&mobile="+mobile.getText().toString()+"&county="+county.getText().toString()+"&bloodgrp="+bloodgrp.getText().toString();
            try {
                url2="http://csgrad07.nwmissouri.edu:3000/registerMobi?fname="+ URLEncoder.encode(fname.toString(),"UTF-8")+"&lname="+URLEncoder.encode(lastName.getText().toString(),"UTF-8")+"&emailid="+URLEncoder.encode(email.getText().toString(),"UTF-8")+"&password="+URLEncoder.encode(password.getText().toString(),"UTF-8")+"&mobile="+URLEncoder.encode(mobile.getText().toString(),"UTF-8")+"&county="+URLEncoder.encode(county.getText().toString(),"UTF-8")+"&bloodgrp="+URLEncoder.encode(bloodgrp.getText().toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Double doInBackground(String... params) {
//            postData();
//            return null;
            double result=0.0;
            try {
                URL url=new URL(url2);

                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                    urlConnection.setRequestProperty("Connection", "close");
                }
                int statusCode=urlConnection.getResponseCode();
                if (statusCode==200){
                    result=1.0;
                }
                else {
                    result=0.0;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            if(aDouble==1.0)
                 Toast.makeText(Registration.this,"Submitted",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(Registration.this,"Failed",Toast.LENGTH_LONG).show();
            //super.onPostExecute(aDouble);
        }
    }
//    public void postData(){
//
//        try {
//            URL url = new URL(url2);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setUseCaches(false);
//            urlConnection.setConnectTimeout(10000);
//            urlConnection.setReadTimeout(10000);
//            //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            // urlConnection.setRequestProperty("Content-Type","application/json");
//            urlConnection.connect();
//            //  File file = new File(selectedImagePath);
//            // File file2=destination;
////            FileInputStream fileInputStream2 = new FileInputStream(file);
////            byte[] bytes = new byte[(int) file.length()];
////            fileInputStream2.read(bytes);
//
//            OutputStream os = urlConnection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
////            String str = jsonParam.toString();
////            byte[] data=str.getBytes("UTF-8");
////            printout2.write(str);
////            MultipartUtility multipart = new MultipartUtility(https, "UTF-8");
////            multipart.addFilePart("file",file);
////            List<String> response = multipart.finish();
////
////            System.out.println("SERVER REPLIED:");
////
////            for (String line : response) {
////                System.out.println(line);
////            }
//
//            writer.flush();
//            writer.close();
////           // int HttpResult =urlConnection.getResponseCode();
////            if(HttpResult ==HttpURLConnection.HTTP_OK){
////                BufferedReader br = new BufferedReader(new InputStreamReader(
////                        urlConnection.getInputStream(),"utf-8"));
////                String line = null;
////                while ((line = br.readLine()) != null) {
////                    sb.append(line + "\n");
////                }
////                printout2.close();
////                br.close();
////                System.out.println(""+sb.toString());
////
////            }else{
////                System.out.println(urlConnection.getResponseMessage());
////                Log.i("status",urlConnection.getResponseMessage());
////            }
//        } catch (MalformedURLException e) {
//
//            e.printStackTrace();
//        }
//
//         catch (IOException e) {
//
//            e.printStackTrace();
//        }
//        //        catch (JSONException e) {
////            e.printStackTrace();
//        //}
//        finally{
//            if(urlConnection!=null)
//                urlConnection.disconnect();
//        }
//    }
//public void postData(){
//
//    try {
//        URL url = new URL("http://csgrad07.nwmissouri.edu:3000/register");
//        urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setDoInput(true);
//        urlConnection.setDoOutput(true);
//        urlConnection.setRequestMethod("POST");
//        urlConnection.setUseCaches(false);
//        urlConnection.setConnectTimeout(10000);
//        urlConnection.setReadTimeout(10000);
//        //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
//        urlConnection.setRequestProperty("Content-Type","application/json");
//        //Create JSONObject here
//
//        File file = new File(selectedImagePath);
//        //  File file2=destination;
//        FileInputStream fileInputStream2 = new FileInputStream(file);
//        byte[] bytes = new byte[(int) file.length()];
//        fileInputStream2.read(bytes);
//
//        printout = new DataOutputStream(urlConnection.getOutputStream ());
//        printout.writeChars("fname=trilok");
//        printout.writeBytes(twoHyphens + boundary + lineEnd);
//        printout.writeBytes("Content-Disposition: form-data; name=\"file\"" + lineEnd);
//        printout.writeBytes(lineEnd);
//        printout.writeBytes(lineEnd);
//        printout.writeBytes(twoHyphens + boundary + lineEnd);
//        printout.writeBytes("Content-Disposition: form-data; name=\"file\""+ lineEnd);
//        printout.writeBytes(lineEnd);
//        printout.writeBytes(lineEnd);
//        printout.writeBytes(twoHyphens + boundary + lineEnd);
//        printout.writeBytes("Content-Disposition: form-data; name=\"file\""+ lineEnd);
//        printout.writeBytes(lineEnd);
//        int bytesAvailable = fileInputStream2.available();
//
//        int maxBufferSize = 1024;
//        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
//        byte[ ] buffer = new byte[bufferSize];
//
//        // read file and write it into form...
//        int bytesRead = fileInputStream2.read(buffer, 0, bufferSize);
//
//        while (bytesRead > 0)
//        {
//            printout.write(buffer, 0, bufferSize);
//            bytesAvailable = fileInputStream2.available();
//            bufferSize = Math.min(bytesAvailable,maxBufferSize);
//            bytesRead = fileInputStream2.read(buffer, 0,bufferSize);
//        }
//        printout.writeBytes(lineEnd);
//        printout.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//        int serverResponseCode = urlConnection.getResponseCode();
//        String serverResponseMessage = urlConnection.getResponseMessage();
//        Log.i("LOGS", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
//        printout.flush();
//        printout.close();
//        fileInputStream2.close();
//        printout.flush();
//        fileInputStream2.close();
//        int HttpResult =urlConnection.getResponseCode();
//        if(HttpResult ==HttpURLConnection.HTTP_OK){
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    urlConnection.getInputStream(),"utf-8"));
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            br.close();
//
//            System.out.println(""+sb.toString());
//
//        }else{
//            System.out.println(urlConnection.getResponseMessage());
//        }
//    } catch (MalformedURLException e) {
//
//        e.printStackTrace();
//    }
//    catch (IOException e) {
//
//        e.printStackTrace();
//    } finally{
//        if(urlConnection!=null)
//            urlConnection.disconnect();
//    }
//}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
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
