package com.coder.hiredriver;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by spinykiller on 10/24/2017.
 */

public class bookingAdap extends ArrayAdapter {
    user u = new user();
    driver d=new driver();
    View listItemViewi;
   // private static final String LOG_TAG = AndroidFlavorAdapter.class.getSimpleName();
    public bookingAdap(Activity context, ArrayList<booking> userlist) {
        super(context, 0, userlist);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.booking_list_template, parent, false);
        }
        listItemViewi=listItemView;
        final booking currentUser = (booking)getItem(position);
        //code  for fetching particular driver and customer and display info
        DatabaseReference df= FirebaseDatabase.getInstance().getReference().child("user");
        final DatabaseReference dfd= FirebaseDatabase.getInstance().getReference().child("driver").child(currentUser.getDriverId());
        df.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                u = dataSnapshot.child(currentUser.getUserId()).getValue(user.class);
                TextView nameTextView = (TextView) listItemViewi.findViewById(R.id.custName);
                nameTextView.setText(u.name);
                TextView numberTextView = (TextView) listItemViewi.findViewById(R.id.location);
                numberTextView.setText(currentUser.getPlace());
                TextView contact = (TextView) listItemViewi.findViewById(R.id.contact);
                contact.setText(u.getContact().toString());
                TextView ondate = (TextView) listItemViewi.findViewById(R.id.date);
                ondate.setText((new SimpleDateFormat("dd-MM-yyyy")).format(currentUser.getfDate()));

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dfd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                d=dataSnapshot.getValue(driver.class);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return listItemView;
    }

}
