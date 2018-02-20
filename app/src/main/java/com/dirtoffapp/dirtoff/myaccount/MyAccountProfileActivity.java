package com.dirtoffapp.dirtoff.myaccount;

import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.R;
import com.dirtoffapp.dirtoff.User;
import com.dirtoffapp.dirtoff.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MyAccountProfileActivity extends Fragment {

    TextView txtFName, txtMName, txtLName, txtEmail, txtCnum, txtAddress, txtBdate, txtDateApplied, txtStatus;
    UserDatabase userDatabase;
    DatabaseReference mDatabase;
    ArrayList<User> list = new ArrayList<>();

    @Keep
    public MyAccountProfileActivity() {
        // Empty Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myaccountprofile, container, false);

        userDatabase = new UserDatabase(getActivity());
        list = userDatabase.getAllUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers").child(this.list.get(0).getLaundWorker_fbid());

        txtFName = view.findViewById(R.id.textView2);
        txtMName = view.findViewById(R.id.textView4);
        txtLName = view.findViewById(R.id.textView6);
        txtEmail = view.findViewById(R.id.textView8);
        txtCnum = view.findViewById(R.id.textView10);
        txtAddress = view.findViewById(R.id.textView12);
        txtBdate = view.findViewById(R.id.textView14);
        txtDateApplied = view.findViewById(R.id.textView16);
        txtStatus = view.findViewById(R.id.textView18);

        /*
        txtFName.setText(list.get(0).getLaundWorker_fn());
        txtMName.setText(list.get(0).getLaundWorker_mn());
        txtLName.setText(list.get(0).getLaundWorker_ln());
        txtEmail.setText(list.get(0).getLaundWorker_email());
        txtCnum.setText(list.get(0).getLaundWorker_cnum());
        txtAddress.setText(list.get(0).getLaundWorker_address());
        txtBdate.setText(list.get(0).getLaundWorker_bdate());
        txtDateApplied.setText(list.get(0).getLaundWorker_dateApplied());
        txtStatus.setText(list.get(0).getLaundWorker_status());*/

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    txtFName.setText(dataSnapshot.child("laundWorker_fn").getValue(String.class));
                    txtMName.setText(dataSnapshot.child("laundWorker_mn").getValue(String.class));
                    txtLName.setText(dataSnapshot.child("laundWorker_ln").getValue(String.class));
                    txtEmail.setText(dataSnapshot.child("laundWorker_email").getValue(String.class));
                    txtCnum.setText(dataSnapshot.child("laundWorker_cnum").getValue(String.class));
                    txtAddress.setText(dataSnapshot.child("laundWorker_address").getValue(String.class));
                    txtBdate.setText(dataSnapshot.child("laundWorker_bdate").getValue(String.class));
                    txtDateApplied.setText(dataSnapshot.child("laundWorker_dateApplied").getValue(String.class));
                    txtStatus.setText(dataSnapshot.child("laundWorker_status").getValue(String.class));
                } else {
                    Toast.makeText(getActivity(), "Error! Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error! Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}