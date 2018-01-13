package com.example.kobevervoort.breweryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Helena on 13/01/18.
 */

class Pub {

    String name;
    String city;
    String status;

    Pub(String name, String city, String status) {

        this.name = name;
        this.city = city;
        this.status = status;
    }
}

class Adapter extends BaseAdapter {

    public static ArrayList<Pub> pubs = new ArrayList<Pub>();
    Context c;

    Adapter(Context context){
        c = context;
        pubs = new ArrayList<Pub>();
    }

    @Override
    public int getCount() {
        return pubs.size();
    }

    @Override
    public Object getItem(int i) {
        return pubs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View detail = layoutInflater.inflate(R.layout.activity_detail, null);

        TextView detailName = (TextView) detail.findViewById(R.id.detailName);
        TextView detailStatus = (TextView) detail.findViewById(R.id.detailStatus);
        TextView detailCity = (TextView) detail.findViewById(R.id.detailCity);

        Pub tmp = pubs.get(position);

        detailName.setText(tmp.name);
        detailStatus.setText(tmp.status);
        detailCity.setText(tmp.city);


        return detail;

    }
}
