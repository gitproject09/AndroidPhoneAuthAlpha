package com.sopan.auth.alpha;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText editTextMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMobile = findViewById(R.id.et_mobile);
        Button btnMobile = findViewById(R.id.btn_mobile);
        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sentSMSViaAlphaNet(editTextMobile.getText().toString().trim());
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(editTextMobile.getText().toString().trim());
            }
        });
    }


    private String sentSMSViaAlphaNet(String phoneNumber) {

        String respose = "";

        String username = "moniruzzaman";
        String hash_token = "d5e8c834c1de3bcd7140af989b8b6656";

        Log.e(MainActivity.class.getSimpleName(), "PhoneNumber: " + phoneNumber);
        //Single or Multiple mobiles numbers separated by comma
        //String to = "017xxxxxxxxx,016xxxxxxxx";
        String to = phoneNumber;

        //Your message to send, Add URL encoding here.
        //String textmessage = "my message is here";


        //generate random number
        Random rnd = new Random();
        int number = 987654; // default code if failed
        try {
            number = rnd.nextInt(999999);
        } catch (Exception e) {

        }

        String verificationCode = String.format("%06d", number);
        Log.e(MainActivity.class.getSimpleName(), "Random Code: " + verificationCode);

        //String verificationMessage = getString(R.string.app_name) + " " + getString(R.string.verification_code_message) + ": " + verificationCode ;

        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        //encode the message content
        //String encoded_message= URLEncoder.encode(textmessage);
        String encoded_message = URLEncoder.encode(verificationCode);
        String apiUrl = "http://alphasms.biz/index.php?app=ws";

        StringBuilder sgcPostContent = new StringBuilder(apiUrl);
        sgcPostContent.append("&u=" + username);
        sgcPostContent.append("&h=" + hash_token);
        sgcPostContent.append("&op=" + "pv");
        sgcPostContent.append("&to=" + to);
        sgcPostContent.append("&msg=" + encoded_message);

        apiUrl = sgcPostContent.toString();
        Log.e(MainActivity.class.getSimpleName(), "url: " + apiUrl);
        try {
            //prepare connection
            myURL = new URL(apiUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //read the output
            String output;
            while ((output = reader.readLine()) != null)
                Log.e(MainActivity.class.getSimpleName(), "OUTPUT: " + output);
            //print output
            //Log.d("OUTPUT", ""+output);

            respose = output;

            //Close connection
            reader.close();
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName(), "catch: " + e.getMessage());
            e.printStackTrace();
            respose = e.getMessage();
        }

        return respose;

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Please wait");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }

        @Override
        protected String doInBackground(String... params) {

            String mobileNumber = params[0];

            return sentSMSViaAlphaNet(mobileNumber);
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            Log.e(MainActivity.class.getSimpleName(), "response: " + result);
            //finalResult.setText(result);
        }


    }

    private void sentSMS() {
        String username = "YourUserNameHere";
        String hash_token = "YourTokenCodeHere";

        //Single or Multiple mobiles numbers separated by comma
        String to = "017xxxxxxxxx,016xxxxxxxx";

        //Your message to send, Add URL encoding here.
        String textmessage = "my message is here";

        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        //encode the message content
        String encoded_message = URLEncoder.encode(textmessage);
        String apiUrl = "http://alphasms.biz/index.php?app=ws&op=pv";

        StringBuilder sgcPostContent = new StringBuilder(apiUrl);
        sgcPostContent.append("u=" + username);
        sgcPostContent.append("h=" + hash_token);
        sgcPostContent.append("&to=" + to);
        sgcPostContent.append("&msg=" + encoded_message);

        apiUrl = sgcPostContent.toString();
        try {
            //prepare connection
            myURL = new URL(apiUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //read the output
            String output;
            while ((output = reader.readLine()) != null)
                //print output
                Log.d("OUTPUT", "" + output);

            //Close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
