package com.whitewalkers.medoc;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class UserLoginActivity extends Activity implements AdapterView.OnItemSelectedListener {
    int i=1654;
    String reqestUrl= "http://jashanpreet.com/medoc/preg.php";
    String userNameData,ageData ,demographicData ,allergicToData, sexData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        final EditText userName = findViewById(R.id.name_user);

        final EditText age = findViewById(R.id.age_user);

        final Spinner sex= findViewById(R.id.sex_user);
        sex.setOnItemSelectedListener(this);
        List<String> sexCategories = new ArrayList<String>();
        sexCategories.add("Male");
        sexCategories.add("Female");
        sexCategories.add("Other");

        ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sexCategories);
        sexAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sex.setAdapter(sexAdapter);

        final EditText demographic = findViewById(R.id.demographic_user);

        final EditText allergicTo = findViewById(R.id.allergicto_user);


        Button submitButton = findViewById(R.id.submit_button_user);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameData = userName.getText().toString();
                ageData = age.getText().toString();
                demographicData = demographic.getText().toString();
                allergicToData = allergicTo.getText().toString();

                new ServerTask().execute();

                Intent intent = new Intent(getApplicationContext(), UserDashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    String serverRequest() throws UnsupportedEncodingException {

        //String data = URLEncoder.encode("Key","UTF-8")+ "=" + URLEncoder.encode("Value","UTF-8");
        String text = "";
        String urlData = reqestUrl +
                "?pid=" + (i++) +
                "&name="+ userNameData+
                "&age="+ ageData+
                "&sex="+ sexData+
                "&demographic="+ demographicData+
                "&allergicto="+ allergicToData;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sexData = adapterView.getItemAtPosition(i).toString();
        switch(sexData){
            case "Male":
                sexData = "M";
                break;

            case "Female":
                sexData = "F";
                break;

            case "Other":
                sexData = "O";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(adapterView.getContext(), "Please select your sex.", Toast.LENGTH_SHORT).show();
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
