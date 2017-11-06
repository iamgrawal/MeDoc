package com.whitewalkers.medoc;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class DoctorDashboardActivity extends Activity {
    String patientId;
    String reqestUrl = "http://jashanpreet.com/medoc/returnpiddetails.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_dashboard);

        Button viewPatientsButton = findViewById(R.id.view_patients_doctor);
        viewPatientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final EditText patientIdText = findViewById(R.id.patientId_editText);
        Button submitPatientIdButton = findViewById(R.id.submitPatientId_button);
        submitPatientIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientId = patientIdText.getText().toString();
                new ServerTask().execute();
                Intent intent = new Intent(getApplicationContext(), VisitDoctorActivity.class);
                startActivity(intent);
            }
        });

    }

    String serverRequest() throws UnsupportedEncodingException {

        //String data = URLEncoder.encode("Key","UTF-8")+ "=" + URLEncoder.encode("Value","UTF-8");
        String text = "";
        String urlData = reqestUrl +
                "?pid=" + patientId;
        HttpURLConnection conn = null;
        InputStream stream = null;
        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL(urlData);

            // Send GET data request

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            if (conn.getResponseCode() == 200) {

                stream = conn.getInputStream();
                Log.d("Get Request", "inside try -if");
                if (stream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    Log.d("click", "inside tr4y");
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {

                        builder.append(line + "\n");
                        Log.d("line", line);

                    }

                    text = builder.toString();
                    Log.d("Response: ",text);
                }
            }
        }
        catch (Exception ex) {
            Log.d("ServerRequest Error", "error occurred" + ex);

        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return text;
    }

    class ServerTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String response= "";
            try{
                response = serverRequest();
            } catch(UnsupportedEncodingException e){
                e.printStackTrace();
                Log.e("ServerTask Error", "Exception Occurred");
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("click", "inside on pre execute");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("click", "inside on post execute" + "the resulted string is" + s);
        }
    }
}
