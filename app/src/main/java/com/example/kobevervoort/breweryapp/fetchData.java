package com.example.kobevervoort.breweryapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Helena on 26/12/17.
 */

public class fetchData extends AsyncTask<String, String , String> {

    String data ="";
    String normalUrl = "";
    String dataParsed = "";
    String singleParsed ="";

    protected void onPreExecute() {

    }

    protected String doInBackground(String... searchCityName) {

        try {

            // Setup url with input of searchfield
            normalUrl = "http://beermapping.com/webservice/loccity/f4ac5bafde4e3ca58d904d266fe956ca/" + searchCityName[0] + "&s=json";
            URL url = new URL( normalUrl);

            // Connect to url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {

                //Read data from url
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                //Put data in variable line
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuilder.append(line).append("\n");
                    data = data + line;
                }

                JSONArray JA = new JSONArray(data);
                for (int i = 0; i<JA.length(); i++) {

                    JSONObject JO = (JSONObject) JA.get(i);
                    singleParsed = "Name: " + JO.get("name") + "\n" +
                            "City: " + JO.get("city") + "\n" +
                            "Status: " + JO.get("status") + "\n" ;

                    dataParsed = dataParsed + singleParsed;

                }

                bufferedReader.close();
                return stringBuilder.toString();
            }

            finally{

                urlConnection.disconnect();
            }
        }

        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {

        MainActivity.data.setText(this.dataParsed);


        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);
    }




}
