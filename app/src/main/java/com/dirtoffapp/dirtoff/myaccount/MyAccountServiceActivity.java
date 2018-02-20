package com.dirtoffapp.dirtoff.myaccount;

import android.graphics.Color;
import android.provider.Telephony;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class MyAccountServiceActivity  extends Fragment {


    ExpandableListView exlist;
    TextView txtBulky;

    HashMap<String, List<String>> map = new HashMap<>();
    MyAccountExpandableAdapter expandAdapter;

    List<String> expandList = new ArrayList<>();
    List<String> weeklyServiceFee = new ArrayList<>();
    List<String> monthlyServiceFee = new ArrayList<>();

    @Keep
    public MyAccountServiceActivity() {
        // Empty Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myaccountservice, container, false);
        UserDatabase userdb = new UserDatabase(getActivity());
        ArrayList<User> userList = userdb.getAllUser();

        exlist = view.findViewById(R.id.expandList);
        txtBulky = view.findViewById(R.id.txtBulky);

        expandList.add("WEEKLY SERVICE FEE");
        expandList.add("MONTHLY SERVICE FEE");

        map.put("WEEKLY SERVICE FEE", weeklyServiceFee);
        map.put("MONTHLY SERVICE FEE", monthlyServiceFee);

        expandAdapter = new MyAccountExpandableAdapter(this.getActivity(), expandList, map);
        exlist.setAdapter(expandAdapter);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryServices").child(userList.get(0).getLaundWorker_fbid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bulky = dataSnapshot.child("bulky").getValue(String.class);
                String monthlyServiceRate = dataSnapshot.child("monthlyServicesFee").getValue(String.class);
                String weeklyServiceRate = dataSnapshot.child("weeklyServicesFee").getValue(String.class);

                if(bulky != null && !bulky.equals("")) {
                    if(isAdded()) {
                        txtBulky.setBackgroundColor(getResources().getColor(R.color.green));
                        txtBulky.setText("BULKY");
                        txtBulky.setTextColor(getResources().getColor(R.color.white));
                    }
                } else {
                    if(isAdded()) {
                        txtBulky.setBackgroundColor(getResources().getColor(R.color.bulky));
                        txtBulky.setText("");
                    }
                }

                if(weeklyServiceRate == null || weeklyServiceRate.equals("")) {
                    weeklyServiceFee.clear();
                    weeklyServiceFee.add("Click edit menu to choose services its corresponding fee");
                } if(monthlyServiceRate == null || monthlyServiceRate.equals("")) {
                    monthlyServiceFee.clear();
                    monthlyServiceFee.add("Click edit menu to choose services its corresponding fee");
                } if((bulky != null && !bulky.equals("")) || monthlyServiceRate != null || weeklyServiceRate != null) {
                    exlist.collapseGroup(0);
                    exlist.collapseGroup(1);


                    StringTokenizer weeklyTokens = new StringTokenizer(weeklyServiceRate, ",");
                    if(weeklyTokens.hasMoreTokens()) {
                        weeklyServiceFee.clear();
                    }
                    while(weeklyTokens.hasMoreTokens()) {
                        String str1 = weeklyTokens.nextToken() + ": " + weeklyTokens.nextToken();
                        weeklyServiceFee.add(str1);
                    }

                    StringTokenizer monthlyTokens = new StringTokenizer(monthlyServiceRate, ",");
                    if(monthlyTokens.hasMoreTokens()) {
                        monthlyServiceFee.clear();
                    }
                    while(monthlyTokens.hasMoreTokens()) {
                        String str2 = monthlyTokens.nextToken() + ": " + monthlyTokens.nextToken();
                        monthlyServiceFee.add(str2);
                    }
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