package com.dirtoffapp.dirtoff.mycalendar;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAvailabilityEditActivity extends AppCompatActivity {} /*implements View.OnClickListener {

    Toolbar toolbar;
    TimePicker startTime, endTime;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myavailability_edit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.startTime = (TimePicker) this.findViewById(R.id.timePicker);
        this.endTime = (TimePicker) this.findViewById(R.id.timePicker2);
        this.btnSave = (Button) this.findViewById(R.id.button);
        this.btnCancel = (Button) this.findViewById(R.id.button2);

        Intent intent = getIntent();
        String sTime = intent.getStringExtra("sTime");
        String eTime = intent.getStringExtra("eTime");

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        Date sT, eT;

        try {
            Calendar c = Calendar.getInstance();
            sT = sdf.parse(toMilitaryTime(sTime));
            c.setTime(sT);
            startTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            startTime.setCurrentMinute(c.get(Calendar.MINUTE));

            eT = sdf.parse(toMilitaryTime(eTime));
            c.setTime(eT);
            endTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            endTime.setCurrentMinute(c.get(Calendar.MINUTE));
        } catch(ParseException e) {
            e.printStackTrace();
        }

        this.btnSave.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button:
                int startHour = this.startTime.getCurrentHour();
                int startMin = this.startTime.getCurrentMinute();
                int endHour = this.endTime.getCurrentHour();
                int endMin = this.endTime.getCurrentMinute();

                if(startHour!=22 && startHour!=23) {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

                    try {
                        Date inTime = sdf.parse(startHour + ":" + startMin);
                        Date outTime = sdf.parse((endHour - 2) + ":" + endMin);

                        if(outTime.after(inTime) || (inTime.compareTo(outTime)==0) || (startHour < (endHour-2))) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("startTime", toRegularTime(startHour, startMin));
                            returnIntent.putExtra("endTime", toRegularTime(endHour, endMin));
                            setResult(Activity.RESULT_OK, returnIntent);
                            this.finish();
                        } else if(outTime.before(inTime)) {
                            Toast.makeText(this, "Start Hour should be 2hrs earlier than End Hour", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Minimum working hours is 2hrs.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "10:00pm-11:59pm Start Hour is not allowed", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.button2:
                this.finish();
                break;
        }
    }

    private String toRegularTime(int hour, int min) {
        String format;

        if(hour == 0) {
            hour += 12;
            format = "AM";
        } else if(hour == 12) {
            format = "PM";
        } else if(hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        return ((hour<10)?"0"+hour:""+hour) +":"+ ((min<10)?"0"+min:""+min) +" "+ format;
    }

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
    }*/
//}