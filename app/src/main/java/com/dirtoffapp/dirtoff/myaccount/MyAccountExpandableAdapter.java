package com.dirtoffapp.dirtoff.myaccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dirtoffapp.dirtoff.R;

import java.util.HashMap;
import java.util.List;

public class MyAccountExpandableAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> group;
    HashMap<String,List<String>> map;
    LayoutInflater groupInflater,itemInflater;

    public MyAccountExpandableAdapter(Context context, List<String> group, HashMap<String, List<String>> map) {
        this.context = context;
        this.group = group;
        this.map = map;
        groupInflater=LayoutInflater.from(context);
        itemInflater=LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return map.get(group.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return map.get(group.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupName = getGroup(groupPosition).toString();

        if(convertView == null) {
            convertView = groupInflater.inflate(R.layout.adapter_group_layout, null);
        }

        TextView tv = convertView.findViewById(R.id.textView1);
        tv.setText(groupName);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String itemName = getChild(groupPosition,childPosition).toString();

        if(convertView == null) {
            convertView = itemInflater.inflate(R.layout.adapter_item_layout, null);
        }

        TextView tv = convertView.findViewById(R.id.textView1);
        tv.setText(itemName);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
