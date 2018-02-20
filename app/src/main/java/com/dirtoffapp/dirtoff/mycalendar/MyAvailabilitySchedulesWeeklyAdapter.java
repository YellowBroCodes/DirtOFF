package com.dirtoffapp.dirtoff.mycalendar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dirtoffapp.dirtoff.R;

import java.util.ArrayList;

public class MyAvailabilitySchedulesWeeklyAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyAvailabilityWeekly> list;
    LayoutInflater inflater;

    public MyAvailabilitySchedulesWeeklyAdapter(Context context, ArrayList<MyAvailabilityWeekly> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemHandler handler = null;

        if(view == null) {
            view = inflater.inflate(R.layout.activity_myavailability_schedules_weekly_adapter, null);
            handler = new ItemHandler();

            handler.txtMonthOf = view.findViewById(R.id.txtMonthOf);
            handler.txtDayOfTheWeek = view.findViewById(R.id.textView);
            handler.txtStartTime = view.findViewById(R.id.textView1);
            handler.txtEndTime = view.findViewById(R.id.textView2);

            view.setTag(handler);
        } else {
            handler = (ItemHandler) view.getTag();
        }

        handler.txtMonthOf.setText("Month: " +list.get(i).getAvailability_monthOf());
        handler.txtDayOfTheWeek.setText("Day: " +list.get(i).getAvailability_dayOfTheWeek());
        handler.txtStartTime.setText("Start Time: " +list.get(i).getAvailability_startTime());
        handler.txtEndTime.setText("End Time:   " +list.get(i).getAvailability_endTime());

        return view;
    }

    static class ItemHandler {
        TextView txtMonthOf, txtDayOfTheWeek, txtStartTime, txtEndTime;
    }
}