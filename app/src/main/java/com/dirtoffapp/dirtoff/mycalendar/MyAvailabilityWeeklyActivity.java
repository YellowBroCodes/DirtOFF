package com.dirtoffapp.dirtoff.mycalendar;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAvailabilityWeeklyActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TimePicker startTime, endTime;
    Button btnSave, btnCancel;
    Spinner cboDay;
    TextView txtOfMonth;
    String dayOfTheWeek = "", monthOf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myavailability_weekly);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.startTime = (TimePicker) this.findViewById(R.id.timePicker);
        this.endTime = (TimePicker) this.findViewById(R.id.timePicker2);
        this.btnSave = (Button) this.findViewById(R.id.button);
        this.btnCancel = (Button) this.findViewById(R.id.button2);
        this.cboDay = (Spinner) this.findViewById(R.id.spinner);
        this.txtOfMonth = (TextView) this.findViewById(R.id.txtOfMonth);

        this.cboDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dayOfTheWeek = cboDay.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // get current month
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.US);
        monthOf = sdf.format(calendar.getTime());
        this.txtOfMonth.setText(new StringBuilder().append("Of ").append(monthOf));

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
                            Intent intent = new Intent();
                            intent.putExtra("dayOfTheWeek", dayOfTheWeek);
                            intent.putExtra("startTime", toRegularTime(startHour, startMin));
                            intent.putExtra("monthOf", monthOf);
                            intent.putExtra("endTime", toRegularTime(endHour, endMin));
                            setResult(Activity.RESULT_OK, intent);
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
}