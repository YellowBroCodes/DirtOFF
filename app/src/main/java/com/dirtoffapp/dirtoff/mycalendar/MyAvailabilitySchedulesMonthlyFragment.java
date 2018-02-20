package com.dirtoffapp.dirtoff.mycalendar;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class MyAvailabilitySchedulesMonthlyFragment extends Fragment implements AdapterView.OnItemClickListener {

    static final int TIME_EDIT_REQUEST_CODE = 10;

    ListView lv;
    MyAvailabilitySchedulesMonthlyAdapter adapter;
    ArrayList<User> list = new ArrayList<>();
    ArrayList<MyAvailabilityMonthly> monthList = new ArrayList<>();
    UserDatabase userDatabase;
    DatabaseReference mDatabase;
    int index;

    public MyAvailabilitySchedulesMonthlyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myavailability_schedules_monthly, container, false);

        userDatabase = new UserDatabase(getActivity());
        list = userDatabase.getAllUser();

        lv = view.findViewById(R.id.listView);
        adapter = new MyAvailabilitySchedulesMonthlyAdapter(getActivity(), monthList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(list.get(0).getLaundWorker_fbid()).child("modeMonthly");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        MyAvailabilityMonthly monthly;
                        monthList.clear();
                        userDatabase.deleteAllAvailabilityMonthly();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            monthly = child.getValue(MyAvailabilityMonthly.class);
                            monthList.add(monthly);
                            if(monthly != null) {
                                userDatabase.addAvailabilityMonthly(monthly.getAvailability_endTime(), monthly.getAvailability_key(), monthly.getAvailability_date(), monthly.getAvailability_startMonth(), monthly.getAvailability_startTime());
                            }
                        }

                        adapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        Toast.makeText(getActivity(), "Error" +e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_availability_edit_or_delete);
        index = i;

        TextView txtEdit = dialog.findViewById(R.id.textView1);
        TextView txtDelete = dialog.findViewById(R.id.textView2);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timeEditIntent = new Intent(getActivity(), MyAvailabilitySchedulesMonthlyEditActivity.class);

                timeEditIntent.putExtra("availability_date", monthList.get(index).getAvailability_date());
                timeEditIntent.putExtra("availability_key", monthList.get(index).getAvailability_key());
                timeEditIntent.putExtra("availability_endTime", monthList.get(index).getAvailability_endTime());
                timeEditIntent.putExtra("availability_startMonth", monthList.get(index).getAvailability_startMonth());
                timeEditIntent.putExtra("availability_startTime", monthList.get(index).getAvailability_startTime());
                startActivityForResult(timeEditIntent, TIME_EDIT_REQUEST_CODE);

                dialog.dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog confirmationDialog = new Dialog(getActivity());
                confirmationDialog.setContentView(R.layout.dialog_availability_edit_or_delete);

                TextView txtYes = confirmationDialog.findViewById(R.id.textView1);
                txtYes.setText(new StringBuilder().append("Yes"));
                TextView txtNo = confirmationDialog.findViewById(R.id.textView2);
                txtNo.setText(new StringBuilder().append("No"));

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<User> fireList = userDatabase.getAllUser();
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireList.get(0).getLaundWorker_fbid()).child("modeMonthly");
                        mDatabase.child(monthList.get(index).getAvailability_key()).removeValue();

                        userDatabase.deleteAvailabilityMonthly(monthList.get(index).getAvailability_key());
                        monthList.remove(index);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getActivity(), "Delete Success", Toast.LENGTH_LONG).show();
                        confirmationDialog.dismiss();
                    }
                });

                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmationDialog.dismiss();
                    }
                });

                dialog.dismiss();
                confirmationDialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == TIME_EDIT_REQUEST_CODE) {
                String availability_date = data.getStringExtra("availability_date");
                String availability_endTime = data.getStringExtra("availability_endTime");
                String availability_key = data.getStringExtra("availability_key");
                String availability_startMonth = data.getStringExtra("availability_startMonth");
                String availability_startTime = data.getStringExtra("availability_startTime");

                ArrayList<User> fireList = userDatabase.getAllUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireList.get(0).getLaundWorker_fbid()).child("modeMonthly");
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("availability_date", availability_date);
                hashMap.put("availability_endTime", availability_endTime);
                hashMap.put("availability_key", availability_key);
                hashMap.put("availability_startMonth", availability_startMonth);
                hashMap.put("availability_startTime", availability_startTime);
                mDatabase.child(availability_key).setValue(hashMap);

                userDatabase.deleteAvailabilityMonthly(availability_key);
                monthList.remove(index);
                userDatabase.addAvailabilityMonthly(availability_date, availability_endTime, availability_key, availability_startMonth, availability_startTime);
                monthList.add(new MyAvailabilityMonthly(availability_date, availability_endTime, availability_key, availability_startMonth, availability_startTime));

                Toast.makeText(getActivity(), "Monthly Schedule Edit Success", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Edit Cancelled", Toast.LENGTH_LONG).show();
        }
    }
}
