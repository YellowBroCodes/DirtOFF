package com.dirtoffapp.dirtoff.mycalendar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAvailabilitySchedulesMonthlyEditActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TimePicker startTime, endTime;
    Button btnSave, btnCancel;
    TextView txtStartMonth;
    Spinner cboDate;
    String date = "", currentMonth, availability_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myavailability_monthly);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.startTime = (TimePicker) this.findViewById(R.id.timePicker);
        this.endTime = (TimePicker) this.findViewById(R.id.timePicker2);
        this.btnSave = (Button) this.findViewById(R.id.button);
        this.btnCancel = (Button) this.findViewById(R.id.button2);
        this.txtStartMonth = (TextView) this.findViewById(R.id.txtStartMonth);
        this.cboDate = (Spinner) this.findViewById(R.id.spinner);

        Intent intent = getIntent();
        String availability_date = intent.getStringExtra("availability_date");
        String availability_endTime = intent.getStringExtra("availability_endTime");
        availability_key = intent.getStringExtra("availability_key");
        String availability_startMonth = intent.getStringExtra("availability_startMonth");
        String availability_startTime = intent.getStringExtra("availability_startTime");

        currentMonth = availability_startMonth;
        this.txtStartMonth.setText(new StringBuilder().append("Starting Month: ").append(currentMonth));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        Date sT, eT;

        try {
            Calendar c = Calendar.getInstance();
            sT = sdf.parse(toMilitaryTime(availability_startTime));
            c.setTime(sT);
            startTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            startTime.setCurrentMinute(c.get(Calendar.MINUTE));

            eT = sdf.parse(toMilitaryTime(availability_endTime));
            c.setTime(eT);
            endTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            endTime.setCurrentMinute(c.get(Calendar.MINUTE));
        } catch(ParseException e) {
            e.printStackTrace();
        }

        this.cboDate.setSelection((Integer.parseInt(availability_date))-1);
        this.cboDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                date = cboDate.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("availability_date", date);
                            returnIntent.putExtra("availability_endTime", toRegularTime(endHour, endMin));
                            returnIntent.putExtra("availability_key", availability_key);
                            returnIntent.putExtra("availability_startMonth", currentMonth);
                            returnIntent.putExtra("availability_startTime", toRegularTime(startHour, startMin));
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

    private String toMilitaryTime(String time) {
        String strHour = time.substring(0, 2);
        String strMin = time.substring(3, 5);
        String format = time.substring(6, 8);
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
}