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

import com.dirtoffapp.dirtoff.Availability;
import com.dirtoffapp.dirtoff.R;
import com.dirtoffapp.dirtoff.UserDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyAvailabilityTimeActivity extends AppCompatActivity {} /*implements View.OnClickListener {

    Toolbar toolbar;
    TimePicker startTime, endTime;
    Button btnSave, btnCancel;

    // calendar
    Date currentDate;
    CalendarView calendarView;
    String dateStr;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myavailability_time);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.startTime = (TimePicker) this.findViewById(R.id.timePicker);
        this.endTime = (TimePicker) this.findViewById(R.id.timePicker2);
        this.btnSave = (Button) this.findViewById(R.id.button);
        this.btnCancel = (Button) this.findViewById(R.id.button2);
        this.calendarView = (CalendarView) this.findViewById(R.id.calendarView);

        dateStr = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date(calendarView.getDate()));
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        try {
            currentDate = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                dateStr = (i1+1)+ "/" +((i2 < 10)?"0"+i2:""+i2)+ "/" +i;
            }
        });

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
                            if(currentDate.before(simpleDateFormat.parse(dateStr))) {
                                Intent intent = new Intent();
                                intent.putExtra("dateStr", dateStr);
                                intent.putExtra("startTime", toRegularTime(startHour, startMin));
                                intent.putExtra("endTime", toRegularTime(endHour, endMin));
                                setResult(Activity.RESULT_OK, intent);
                                this.finish();
                            } else {
                                Toast.makeText(this, "Invalid Date! Date must be after today.", Toast.LENGTH_LONG).show();
                            }
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
    }*/
//}