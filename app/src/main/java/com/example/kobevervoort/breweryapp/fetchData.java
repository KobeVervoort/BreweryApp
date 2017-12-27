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

public class fetchData extends AsyncTask<Void, Void, String> {

    String data ="";
    String dataParsed = "";
    String singleParsed ="";

    protected void onPreExecute() {

    }

    protected String doInBackground(Void... urls) {

        try {

            URL url = new URL("http://beermapping.com/webservice/locquery/f4ac5bafde4e3ca58d904d266fe956ca/belgian&s=json");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
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
