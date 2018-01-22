package com.example.kobevervoort.breweryapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PubFragment extends Fragment {

    View pubView;

    TextView pubName;
    TextView pubStatus;
    TextView pubCity;
    TextView pubStreet;
    TextView pubPhone;
    TextView pubFavorite;


    public static final String TAG = "PubFragment";

    Button buttonFavorite;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Reference is called mRootRef because it will be a reference to the root of the database JSON tree
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mFavoriteRef = mRootRef.child("favorite");
    DatabaseReference mPubsRef = mRootRef.child("pubs");
    DatabaseReference mUsersRef = mRootRef.child("users");
    DatabaseReference mUserFavorites = mRootRef.child("userFavorites");

    public PubFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        final Pub pub = (Pub) intent.getParcelableExtra("pub_data");

        final String pubId = pub.id;
        final Query eventQuery = mPubsRef.orderByKey().equalTo(pubId);

        pubView = inflater.inflate(R.layout.fragment_pub, container, false);

        pubName = (TextView) pubView.findViewById(R.id.detailName);
        pubStatus = (TextView) pubView.findViewById(R.id.detailStatus);
        pubCity = (TextView) pubView.findViewById(R.id.detailCity);
        pubStreet = (TextView) pubView.findViewById(R.id.detailStreet);
        pubPhone = (TextView) pubView.findViewById(R.id.detailPhone);

        buttonFavorite = (Button) pubView.findViewById(R.id.buttonFavorite);

        pubName.setText(pub.name);
        pubStatus.setText(pub.status);
        pubStreet.setText(pub.street);
        pubCity.setText(pub.city);
        pubPhone.setText(pub.phone);

        //Logic for adding or deleting a pub as a favorite
        buttonFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "Got FirebaseAuth instance");
                Log.d(TAG, "Made the query");
                eventQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null){
                            Log.d(TAG, "datasnapshot was empty");
                            //make new pub
                            Pub newPub = new Pub(pub.name, pub.status, pub.street, pub.city, pub.phone, pubId);
                            Log.d(TAG, "newPub.id = " + newPub.id);
                            mPubsRef.child(pubId).setValue(newPub);

                            //make new user-favourite child for pub and auth user
                            addOrDeleteFavoritePub(mAuth.getUid(), newPub);
                        } else {
                            Log.d(TAG, "there was a datasnapshot" + dataSnapshot.getValue());
                            Pub newPub = dataSnapshot.child(pubId).getValue(Pub.class);

                            addOrDeleteFavoritePub(mAuth.getUid(), newPub);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "There was an error");
                    }
                });
                Log.d(TAG, "this is the UID: "+ mAuth.getUid());
            }
        });

        return pubView;

    }

    @Override
    public void onStart(){
        super.onStart();

        //Checking if this page's pub is already in database if so, if it's already associated with
        //the current user
        Intent intent = getActivity().getIntent();
        final Pub pub = (Pub) intent.getParcelableExtra("pub_data");
        final String pubId = pub.id;
        final Query eventQuery = mPubsRef.orderByKey().equalTo(pubId);

        mUserFavorites.child(mAuth.getUid()).orderByKey().equalTo(pubId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    buttonFavorite.setText("Add to favorites");
                } else {
                    UserFavorite userFavorite = dataSnapshot.child(pubId).getValue(UserFavorite.class);
                    if (userFavorite.isFavorite()){
                        buttonFavorite.setText("Already a favorite");
                    } else {
                        buttonFavorite.setText("Add to favorites");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*mFavoriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                pubFavorite.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    private void addOrDeleteFavoritePub(String userId, Pub newPub){
        final String uid = userId;
        final Pub pub = newPub;
        final UserFavorite userFavorite = new UserFavorite();
        Log.d(TAG, "Pub id = " + pub.id);

        mUserFavorites.child(uid).orderByKey().equalTo(pub.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    userFavorite.setFavorite(true);
                    mUserFavorites.child(uid).child(pub.id).setValue(userFavorite);
                } else {
                    Log.d(TAG, "datasnapshot 2 = " + dataSnapshot.getValue());
                    UserFavorite snapshot = dataSnapshot.child(pub.id).getValue(UserFavorite.class);
                    Log.d(TAG, "snapshot favorite or not = " + snapshot.isFavorite());

                    if (snapshot.isFavorite()){
                        snapshot.setFavorite(false);
                        mUserFavorites.child(uid).child(pub.id).setValue(snapshot);
                        buttonFavorite.setText("Add to favorites");
                    } else {
                        snapshot.setFavorite(true);
                        mUserFavorites.child(uid).child(pub.id).setValue(snapshot);
                        buttonFavorite.setText("Already a favorite");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
