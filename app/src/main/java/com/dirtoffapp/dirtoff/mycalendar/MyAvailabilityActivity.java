package com.dirtoffapp.dirtoff.mycalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.Availability;
import com.dirtoffapp.dirtoff.R;
import com.dirtoffapp.dirtoff.User;
import com.dirtoffapp.dirtoff.UserDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyAvailabilityActivity extends AppCompatActivity{}// implements DialogInterface.OnClickListener, CalendarView.OnDateChangeListener {

    /*static final int TIME_ADD_REQUEST_CODE = 10;
    static final int TIME_EDIT_REQUEST_CODE = 20;
    static final int TIME_ADD_WEEKLY_REQUEST_CODE = 30;
    static final int TIME_ADD_MONTHLY_REQUEST_CODE = 40;

    Toolbar toolbar;
    ListView lv;
    ArrayList<MyAvailabilityMonthly> listMonthly = new ArrayList<>();
    ArrayList<MyAvailabilityWeekly> listWeekly = new ArrayList<>();
    ArrayList<MyAvailabilityMonthly> selectedDayListMonthly = new ArrayList<>();
    ArrayList<MyAvailabilityWeekly> selectedDayListWeekly = new ArrayList<>();
    MyAvailabilityAdapter adapter;
    AdapterView.AdapterContextMenuInfo info;
    UserDatabase userdb;
    String dateStr, dayOfTheWeek, availabilityMode;
    CalendarView calendarView;
    String dateSelected;
    String eDate, eKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myavailability);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userdb = new UserDatabase(this);
        //////////////////////////
        list = userdb.getAllAvailabilityMonthly();
        selectedDayList = userdb.getAllAvailabilityMonthly();

        this.lv = (ListView) this.findViewById(R.id.listView);
        this.calendarView = (CalendarView) this.findViewById(R.id.calendarView);

        this.adapter = new MyAvailabilityAdapter(this, selectedDayList);
        this.lv.setAdapter(adapter);
        this.registerForContextMenu(lv);
        //////////////////////////

        /////////////////////
        dateSelected = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date(calendarView.getDate()));
        ////////////////////
        this.calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.action_availability_contextmenu, menu);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //////////////////////////
        switch(item.getItemId()) {
            case R.id.edit:
                Intent timeEditIntent = new Intent(MyAvailabilityActivity.this, MyAvailabilityEditActivity.class);
                eDate = selectedDayList.get(info.position).getDate();
                eKey = selectedDayList.get(info.position).getKey();
                timeEditIntent.putExtra("sTime", selectedDayList.get(info.position).getStartTime());
                timeEditIntent.putExtra("eTime", selectedDayList.get(info.position).getEndTime());
                startActivityForResult(timeEditIntent, TIME_EDIT_REQUEST_CODE);

                break;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete schedule?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", this);
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
        //////////////////////////

        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        //////////////////////////
        switch(i) {
            case DialogInterface.BUTTON_POSITIVE:
                int index = getListIndex(selectedDayList.get(info.position).getDate(), selectedDayList.get(info.position).getStartTime());

                ArrayList<User> fireList = userdb.getAllUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireList.get(0).getLaundWorker_fbid());
                mDatabase.child(list.get(index).getKey()).removeValue();

                userdb.deleteAvailability(list.get(index).getKey(), selectedDayList.get(info.position).getDate());
                list.remove(index);
                selectedDayList.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Delete Success", Toast.LENGTH_LONG).show();
                break;
        }
        //////////////////////////
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_availability_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_availability_choice);

                TextView txtWeekly = dialog.findViewById(R.id.textView1);
                TextView txtMonthly = dialog.findViewById(R.id.textView2);

                txtWeekly.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent weeklyIntent = new Intent(MyAvailabilityActivity.this, MyAvailabilityWeeklyActivity.class);
                        startActivityForResult(weeklyIntent, TIME_ADD_WEEKLY_REQUEST_CODE);
                        dialog.dismiss();
                    }
                });

                txtMonthly.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent monthlyIntent = new Intent(MyAvailabilityActivity.this, MyAvailabilityMonthlyActivity.class);
                        startActivityForResult(monthlyIntent, TIME_ADD_MONTHLY_REQUEST_CODE);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == TIME_ADD_WEEKLY_REQUEST_CODE) {
                availabilityMode = data.getStringExtra("availabilityMode");
                dayOfTheWeek = data.getStringExtra("dayOfTheWeek");
                String startTime = data.getStringExtra("startTime");
                String endTime = data.getStringExtra("endTime");

                ArrayList<User> fireArrList = userdb.getAllUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireArrList.get(0).getLaundWorker_fbid()).child("modeWeekly");
                HashMap<String, String> hashMap = new HashMap<>();

                String key = mDatabase.push().getKey();
                hashMap.put("availability_dayOfTheWeek", dayOfTheWeek);
                hashMap.put("availability_endTime", endTime);
                hashMap.put("availability_id", key);
                hashMap.put("availability_startTime", startTime);
                mDatabase.child(key).setValue(hashMap);

                this.listWeekly.add(new MyAvailabilityWeekly(dayOfTheWeek, endTime, key, "", startTime));
                this.userdb.addAvailabilityWeekly(dayOfTheWeek, endTime, key, "", startTime);
                Toast.makeText(this, "Weekly Schedule Added", Toast.LENGTH_LONG).show();
            } else if(requestCode == TIME_ADD_MONTHLY_REQUEST_CODE) {
                String startMonth = data.getStringExtra("startMonth");
                String startTime = data.getStringExtra("startTime");
                String endTime = data.getStringExtra("endTime");

                ArrayList<User> fireArrList = userdb.getAllUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireArrList.get(0).getLaundWorker_fbid()).child("modeMonthly");
                HashMap<String, String> hashMap = new HashMap<>();

                String key = mDatabase.push().getKey();
                hashMap.put("availability_endTime", endTime);
                hashMap.put("availability_id", key);
                hashMap.put("availability_startMonth", startMonth);
                hashMap.put("availability_startTime", startTime);
                mDatabase.child(key).setValue(hashMap);

                this.listMonthly.add(new MyAvailabilityMonthly("", endTime, key, startMonth, startTime));
                this.userdb.addAvailabilityMonthly("", endTime, key, startMonth, startTime);
                Toast.makeText(this, "Monthly Schedule Added", Toast.LENGTH_LONG).show();
            }
        }
    }

    //////////////////////////
    private boolean isConflictSchedule(String startTime, String endTime) {
        boolean bool = false;

        for(int i = 0; i < list.size(); i++) {
            if(dateStr.equals(list.get(i).getDate())) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

                try {
                    if(sdf.parse(toMilitaryTime(startTime)).before(sdf.parse(toMilitaryTime(list.get(i).getStartTime())))) {
                        bool = !(sdf.parse(toMilitaryTime(endTime)).before(sdf.parse(toMilitaryTime(list.get(i).getStartTime()))));
                        if(bool) {break;}
                    } else {
                        bool = !(sdf.parse(toMilitaryTime(startTime)).after(sdf.parse(toMilitaryTime(list.get(i).getEndTime()))));
                        if(bool) {break;}
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return bool;
    }

    private boolean isConflictScheduleEdit(String startTime, String endTime) {
        boolean bool = false;

        for(int i = 0; i < list.size(); i++) {
            if(!(eKey.equals(list.get(i).getKey())) && (dateStr.equals(list.get(i).getDate()))) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

                try {
                    if(sdf.parse(toMilitaryTime(startTime)).before(sdf.parse(toMilitaryTime(list.get(i).getStartTime())))) {
                        bool = !(sdf.parse(toMilitaryTime(endTime)).before(sdf.parse(toMilitaryTime(list.get(i).getStartTime()))));
                        if(bool) {break;}
                    } else {
                        bool = !(sdf.parse(toMilitaryTime(startTime)).after(sdf.parse(toMilitaryTime(list.get(i).getEndTime()))));
                        if(bool) {break;}
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return bool;
    }
    //////////////////////////

    //////////////////////////
    private String toMilitaryTime(String currentStartTime) {
        String strHour = currentStartTime.substring(0, 2);
        String strMin = currentStartTime.substring(3, 5);
        String format = currentStartTime.substring(6, 8);
        int intHour = Integer.parseInt(strHour);
        int intMin = Integer.parseInt(strMin);

        if(format.equals("AM")) {
            if(intHour == 12) {
                intHour = 0;
            }
        } else {
            intHour = (intHour==12)?12:intHour+12;
        }

        return intHour +":"+ intMin;
    }
    //////////////////////////

    ///////////////////////////
    private int getListIndex(String d, String t) {
        int i;

        for(i=0; i<list.size(); i++) {
            if(d.equals(list.get(i).getDate()) && t.equals(list.get(i).getStartTime())) {
                break;
            }
        }

        return i;
    }
    //////////////////////////

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        ////////////////////////
        String d = (i2 < 10)?"0"+i2:""+i2;
        dateSelected = (i1+1)+ "/" +d+ "/" +i;

        if(!list.isEmpty()) {
            selectedDayList.clear();

            for (int a = 0; a < list.size(); a++) {
                if(dateSelected.equals(list.get(a).getDate())) {
                    selectedDayList.add(new Availability(list.get(a).getDate(), list.get(a).getEndTime(), list.get(a).getKey(), list.get(a).getStartTime()));
                }
            }

            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MyAvailabilityActivity.this, "You have not added a schedule yet.", Toast.LENGTH_SHORT).show();
        }*/
        ///////////////////////////////
    //}