package com.dirtoffapp.dirtoff.mycalendar;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

public class MyAvailabilitySchedulesWeeklyFragment extends Fragment implements AdapterView.OnItemClickListener {

    static final int TIME_EDIT_REQUEST_CODE = 10;

    ListView lv;
    MyAvailabilitySchedulesWeeklyAdapter adapter;
    ArrayList<MyAvailabilityWeekly> weekList = new ArrayList<>();
    ArrayList<User> list = new ArrayList<>();
    UserDatabase userDatabase;
    DatabaseReference mDatabase;
    int index;

    public MyAvailabilitySchedulesWeeklyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myavailability_schedules_weekly, container, false);

        userDatabase = new UserDatabase(getActivity());
        list = userDatabase.getAllUser();

        lv = view.findViewById(R.id.listView);
        adapter = new MyAvailabilitySchedulesWeeklyAdapter(getActivity(), weekList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(list.get(0).getLaundWorker_fbid()).child("modeWeekly");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        MyAvailabilityWeekly weekly;
                        weekList.clear();
                        userDatabase.deleteAllAvailabilityWeekly();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            weekly = child.getValue(MyAvailabilityWeekly.class);
                            weekList.add(weekly);
                            if(weekly != null) {
                                userDatabase.addAvailabilityWeekly(weekly.availability_dayOfTheWeek, weekly.getAvailability_endTime(), weekly.getAvailability_key(), weekly.getAvailability_monthOf(), weekly.getAvailability_startTime());
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
                Intent timeEditIntent = new Intent(getActivity(), MyAvailabilitySchedulesWeeklyEditActivity.class);

                timeEditIntent.putExtra("availability_dayOfTheWeek", weekList.get(index).getAvailability_dayOfTheWeek());
                timeEditIntent.putExtra("availability_endTime", weekList.get(index).getAvailability_endTime());
                timeEditIntent.putExtra("availability_key", weekList.get(index).getAvailability_key());
                timeEditIntent.putExtra("availability_monthOf", weekList.get(index).getAvailability_monthOf());
                timeEditIntent.putExtra("availability_startTime", weekList.get(index).getAvailability_startTime());
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
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireList.get(0).getLaundWorker_fbid()).child("modeWeekly");
                        mDatabase.child(weekList.get(index).getAvailability_key()).removeValue();

                        userDatabase.deleteAvailabilityMonthly(weekList.get(index).getAvailability_key());
                        weekList.remove(index);
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
                String availability_dayOfTheWeek = data.getStringExtra("availability_dayOfTheWeek");
                String availability_endTime = data.getStringExtra("availability_endTime");
                String availability_key = data.getStringExtra("availability_key");
                String availability_monthOf = data.getStringExtra("availability_monthOf");
                String availability_startTime = data.getStringExtra("availability_startTime");

                ArrayList<User> fireList = userDatabase.getAllUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireList.get(0).getLaundWorker_fbid()).child("modeWeekly");
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("availability_dayOfTheWeek", availability_dayOfTheWeek);
                hashMap.put("availability_endTime", availability_endTime);
                hashMap.put("availability_key", availability_key);
                hashMap.put("availability_monthOf", availability_monthOf);
                hashMap.put("availability_startTime", availability_startTime);
                mDatabase.child(availability_key).setValue(hashMap);

                userDatabase.deleteAvailabilityWeekly(availability_key);
                weekList.remove(index);
                userDatabase.addAvailabilityWeekly(availability_dayOfTheWeek, availability_endTime, availability_key, availability_monthOf, availability_startTime);
                weekList.add(new MyAvailabilityWeekly(availability_dayOfTheWeek, availability_endTime, availability_key, availability_monthOf, availability_startTime));

                Toast.makeText(getActivity(), "Weekly Schedule Edit Success", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Edit Cancelled", Toast.LENGTH_LONG).show();
        }
    }
}
