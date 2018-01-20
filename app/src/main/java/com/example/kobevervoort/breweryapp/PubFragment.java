package com.example.kobevervoort.breweryapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PubFragment extends Fragment {

    View pubView;

    TextView pubName;
    TextView pubStatus;
    TextView pubCity;
    TextView pubStreet;
    TextView pubPhone;
    TextView pubFavorite;

    Button buttonFavorite;
    Button buttonUnfavorite;

    //Reference is called mRootRef because it will be a reference to the root of the database JSON tree
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mFavoriteRef = mRootRef.child("favorite");

    public PubFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pubView = inflater.inflate(R.layout.fragment_pub, container, false);

        Intent intent = getActivity().getIntent();
        Pub pub = (Pub) intent.getParcelableExtra("pub_data");

        pubName = (TextView) pubView.findViewById(R.id.detailName);
        pubStatus = (TextView) pubView.findViewById(R.id.detailStatus);
        pubCity = (TextView) pubView.findViewById(R.id.detailCity);
        pubStreet = (TextView) pubView.findViewById(R.id.detailStreet);
        pubPhone = (TextView) pubView.findViewById(R.id.detailPhone);
        pubFavorite = (TextView) pubView.findViewById(R.id.detailFavorite);

        buttonFavorite = (Button) pubView.findViewById(R.id.buttonFavorite);
        buttonUnfavorite = (Button) pubView.findViewById(R.id.buttonUnfavorite);

        pubName.setText(pub.name);
        pubStatus.setText(pub.status);
        pubStreet.setText(pub.street);
        pubCity.setText(pub.city);
        pubPhone.setText(pub.phone);

        return pubView;

    }

    @Override
    public void onStart(){
        super.onStart();

        mFavoriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                pubFavorite.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                mFavoriteRef.setValue("I like the beer in here!");

            }
        });

        buttonUnfavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                mFavoriteRef.setValue("I wasn't a fan...");
            }
        });

    }
}
