package com.whitewalkers.medoc;

import android.app.Activity;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class VisitDoctorActivity extends Activity {
    String reqestUrl = "http://jashanpreet.com/medoc/visitdetails.php";
    String  patientNameData ,patientIdData ,doctorIdData ,visitNumberData ,diseaseData ,medicinesData ,testsData ,precautionsData ,nextVisitData ,notesData, TimeStampData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_doctor);

        final EditText patientName = findViewById(R.id.patientName_visit);
        final EditText patientId = findViewById(R.id.patientName_visit);
        final EditText doctorId = findViewById(R.id.doctorId_visit);
        final EditText visitNumber = findViewById(R.id.visitNumber_visit);
        final EditText disease = findViewById(R.id.disease_visit);
        final EditText medicines = findViewById(R.id.medicines_visit);
        final EditText tests = findViewById(R.id.tests_visit);
        final EditText precautions = findViewById(R.id.precautions_visit);
        final EditText nextVisit = findViewById(R.id.nextVisit_visit);
        final EditText notes = findViewById(R.id.notes_visit);
        Button SubmitButton = findViewById(R.id.submitButton_visit);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientNameData = patientName.getText().toString();
                patientIdData = patientId.getText().toString();
                doctorIdData =doctorId.getText().toString();
                visitNumberData = visitNumber.getText().toString();
                diseaseData = disease.getText().toString();
                medicinesData = medicines.getText().toString();
                testsData = tests.getText().toString();
                precautionsData =precautions.getText().toString();
                nextVisitData = nextVisit.getText().toString();
                notesData = notes.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+5.30"));
                TimeStampData = sdf.format(new Date());

                new ServerTask().execute();
            }
        });
    }

    String serverRequest() throws UnsupportedEncodingException {

        //String data = URLEncoder.encode("Key","UTF-8")+ "=" + URLEncoder.encode("Value","UTF-8");
        String text = "";
        String urlData = reqestUrl +
                "?pid=" + patientIdData+
                "&docid=" + doctorIdData +
                "&visitnumber=" + visitNumberData +
                "&timestamp=" + TimeStampData +
                "&disease=" + diseaseData +
                "&medicine1=" + medicinesData +
                "&medicine2=" + medicinesData +
                "&medicine3=" + medicinesData +
                "&medicine4=" + medicinesData +
                "&test1=" + testsData +
                "&test2=" + testsData +
                "&test3=" + testsData +
                "&nextvisit=" + nextVisitData +
                "&precautions=" + precautionsData +
                "&notes=" + notesData;
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
