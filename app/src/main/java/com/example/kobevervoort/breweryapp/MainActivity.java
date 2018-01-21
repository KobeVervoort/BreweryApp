package com.example.kobevervoort.breweryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button click;
    ListView beerList;
    Adapter BeerAdapter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private static final String TAG = "MainActivity";

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
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String data) {

            try {

                JSONArray jsonArray = new JSONArray(data);

                for (int i = 0; i < jsonArray.length(); i++) {

                    //Get JSONobject from apiurl
                    JSONObject beerPub = (JSONObject) jsonArray.getJSONObject(i);

                    //Get specific elements from apiurl and put the in a Stringvariabel
                    String name = (String) beerPub.getString("name");
                    String status = (String) beerPub.getString("status");
                    String street = (String) beerPub.getString("street");
                    String city = (String) beerPub.getString("city");
                    String phone = (String) beerPub.getString("phone");
                    String id = (String) beerPub.getString("id");

                    //Set these strings into the adapter
                    Adapter.pubs.add(new Pub(name, status, street, city, phone, id));
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

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null){
            //Not signed in, launch the Sign In Activity
            Log.d(TAG, "User is not logged in");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            Log.d(TAG, "User is logged in \nand here he is: " + mFirebaseUser.getUid());
            setContentView(R.layout.activity_main);

            beerList = (ListView) findViewById(R.id.beerList);
            BeerAdapter = new Adapter(this);
            beerList.setAdapter(BeerAdapter);
            beerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Pub pub = BeerAdapter.getItem(i);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class).putExtra("pub_data", pub);
                    startActivity(intent);
                }
            });
            BeerAdapter.notifyDataSetChanged();

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
        }


    }


}

