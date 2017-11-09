package com.coder.hiredriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class DriverDashboard extends AppCompatActivity {

    bookingAdap dadap;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(DriverDashboard.this, LoginDriver.class));
                }

            }
        };


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        final ListView listView = (ListView) findViewById(R.id.bookkingList);

        final ArrayList<booking> Users=new ArrayList<booking>();
        DatabaseReference df=FirebaseDatabase.getInstance().getReference().child("booking");
        df.addValueEventListener(new ValueEventListener() {
            booking b;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot booking:dataSnapshot.getChildren()){
                    b=new booking();
                    b.bookId=booking.getKey();
                    b.driverId=booking.child("driverId").getValue(String.class);
                    if(mAuth!=null && mAuth.getUid().equals(b.driverId)){
                        b.fDate=booking.child("fDate").getValue(Date.class);
                        b.userId=booking.child("userId").getValue(String.class);
                        b.cost=booking.child("cost").getValue(double.class);
                        b.place=booking.child("place").getValue(String.class);
                        Users.add(b);
                    }
                }
                dadap=new bookingAdap(DriverDashboard.this,Users);
                listView.setAdapter(dadap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void logout(View v)
    {
        mAuth.signOut();

    }
}
