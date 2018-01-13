package com.example.kobevervoort.breweryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    Button click;
    ListView beerList;
    Adapter BeerAdapter;

    @SuppressLint("StaticFieldLeak")
    public class fetchData extends AsyncTask<String, String , String> {

        String data ="";
        String normalUrl = "";

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... searchCityName) {

            try {

                Adapter.pubs.clear();

                // Setup url with input of searchfield
                normalUrl = "http://beermapping.com/webservice/loccity/f4ac5bafde4e3ca58d904d266fe956ca/" + searchCityName[0] + "&s=json";
                URL url = new URL( normalUrl);

                // Connect to url
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                //Read data from url
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                //Put data in variable line
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuilder.append(line).append("\n");
                    data = data + line;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        protected void onPostExecute(String data) {

            super.onPostExecute(data);

            try {

                JSONArray jsonArray = new JSONArray(data);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                    JSONObject pub = jsonObject.getJSONObject("pub");

                    String name = (String) pub.getString("name");
                    String status = (String) pub.getString("status");
                    String city = (String) pub.getString("city");

                    Adapter.pubs.add(new Pub(name, status, city));
                }

                BeerAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BufferedReader reader = null;

        click = (Button)findViewById(R.id.button);
        click.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //get text en set to string
                EditText searchCity = (EditText) findViewById(R.id.searchCity);
                String searchCityName = searchCity.getText().toString();

                fetchData process = new fetchData();
                process.execute(searchCityName);

            }
        });

        beerList = (ListView) findViewById(R.id.beerList);
        BeerAdapter = new Adapter(this);
        beerList.setAdapter(BeerAdapter);
        BeerAdapter.notifyDataSetChanged();

    }


}

