package com.dirtoffapp.dirtoff.mycalendar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dirtoffapp.dirtoff.Availability;
import com.dirtoffapp.dirtoff.R;

import java.util.ArrayList;

public class MyAvailabilitySchedulesMonthlyAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyAvailabilityMonthly> list;
    LayoutInflater inflater;

    public MyAvailabilitySchedulesMonthlyAdapter(Context context, ArrayList<MyAvailabilityMonthly> list) {
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
            view = inflater.inflate(R.layout.activity_myavailability_schedules_monthly_adapter, null);
            handler = new ItemHandler();

            handler.txtStartMonth = view.findViewById(R.id.txtMonthStart);
            handler.txtDate = view.findViewById(R.id.textView);
            handler.txtStartTime = view.findViewById(R.id.textView1);
            handler.txtEndTime = view.findViewById(R.id.textView2);

            view.setTag(handler);
        } else {
            handler = (ItemHandler) view.getTag();
        }

        handler.txtStartMonth.setText("Month: " +list.get(i).getAvailability_startMonth());
        handler.txtDate.setText("Date: "+list.get(i).getAvailability_date());
        handler.txtStartTime.setText("Start Time: " +list.get(i).getAvailability_startTime());
        handler.txtEndTime.setText("End Time:   " +list.get(i).getAvailability_endTime());

        return view;
    }

    static class ItemHandler {
        TextView txtStartMonth, txtDate, txtStartTime, txtEndTime;
    }
}