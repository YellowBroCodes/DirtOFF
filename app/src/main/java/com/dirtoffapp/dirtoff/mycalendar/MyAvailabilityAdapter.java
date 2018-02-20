package com.dirtoffapp.dirtoff.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dirtoffapp.dirtoff.Availability;
import com.dirtoffapp.dirtoff.R;

import java.util.ArrayList;

public class MyAvailabilityAdapter extends BaseAdapter {

    Context context;
    ArrayList<Availability> list;
    LayoutInflater inflater;

    public MyAvailabilityAdapter(Context context, ArrayList<Availability> list) {
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
            view = inflater.inflate(R.layout.activity_myavailability_adapter, null);
            handler = new ItemHandler();

            handler.txtDate = view.findViewById(R.id.textView);
            handler.txtStartTime = view.findViewById(R.id.textView2);
            handler.txtEndTime = view.findViewById(R.id.textView3);

            view.setTag(handler);
        } else {
            handler = (ItemHandler) view.getTag();
        }

        handler.txtDate.setText("Date: " +list.get(i).getDate());
        handler.txtStartTime.setText("Start Time: " +list.get(i).getStartTime());
        handler.txtEndTime.setText("End Time:   " +list.get(i).getEndTime());

        return view;
    }

    static class ItemHandler {
        TextView txtDate, txtStartTime, txtEndTime;
    }
}
