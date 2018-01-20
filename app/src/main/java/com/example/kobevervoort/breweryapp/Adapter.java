package com.example.kobevervoort.breweryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Helena on 13/01/18.
 */

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
    // changed it from generic Java Object tot Pub object
    public Pub getItem(int i) {
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
        View detail = layoutInflater.inflate(R.layout.fragment_main, null);

        TextView detailName = (TextView) detail.findViewById(R.id.detailName);
        TextView detailStatus = (TextView) detail.findViewById(R.id.detailStatus);
        TextView detailCity = (TextView) detail.findViewById(R.id.detailCity);
        TextView detailStreet = (TextView) detail.findViewById(R.id.detailStreet);
        TextView detailPhone = (TextView) detail.findViewById(R.id.detailPhone);

        Pub tmp = pubs.get(position);

        detailName.setText(tmp.name);
        detailStatus.setText(tmp.status);
        detailStreet.setText(tmp.street);
        detailCity.setText(tmp.city);
        detailPhone.setText(tmp.phone);


        return detail;

    }
}
