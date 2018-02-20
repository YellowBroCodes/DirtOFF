package com.dirtoffapp.dirtoff.mycalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Keep;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.R;
import com.dirtoffapp.dirtoff.User;
import com.dirtoffapp.dirtoff.UserDatabase;
import com.dirtoffapp.dirtoff.myaccount.MyAccountActivity;
import com.dirtoffapp.dirtoff.myaccount.MyAccountProfileActivity;
import com.dirtoffapp.dirtoff.myaccount.MyAccountProfileEditActivity;
import com.dirtoffapp.dirtoff.myaccount.MyAccountServiceActivity;
import com.dirtoffapp.dirtoff.myaccount.MyAccountServiceEditActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAvailabilitySchedulesActivity extends AppCompatActivity {

    static final int TIME_ADD_WEEKLY_REQUEST_CODE = 30;
    static final int TIME_ADD_MONTHLY_REQUEST_CODE = 40;

    ArrayList<MyAvailabilityMonthly> listMonthly = new ArrayList<>();
    ArrayList<MyAvailabilityWeekly> listWeekly = new ArrayList<>();

    UserDatabase userdb;
    String dayOfTheWeek;

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myavailability_schedules);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userdb = new UserDatabase(this);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
                        Intent weeklyIntent = new Intent(MyAvailabilitySchedulesActivity.this, MyAvailabilityWeeklyActivity.class);
                        startActivityForResult(weeklyIntent, TIME_ADD_WEEKLY_REQUEST_CODE);
                        dialog.dismiss();
                    }
                });

                txtMonthly.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent monthlyIntent = new Intent(MyAvailabilitySchedulesActivity.this, MyAvailabilityMonthlyActivity.class);
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
                dayOfTheWeek = data.getStringExtra("dayOfTheWeek");
                String endTime = data.getStringExtra("endTime");
                String monthOf = data.getStringExtra("monthOf");
                String startTime = data.getStringExtra("startTime");

                ArrayList<User> fireArrList = userdb.getAllUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireArrList.get(0).getLaundWorker_fbid()).child("modeWeekly");
                HashMap<String, String> hashMap = new HashMap<>();

                String key = mDatabase.push().getKey();
                hashMap.put("availability_dayOfTheWeek", dayOfTheWeek);
                hashMap.put("availability_endTime", endTime);
                hashMap.put("availability_key", key);
                hashMap.put("availability_monthOf", monthOf);
                hashMap.put("availability_startTime", startTime);
                mDatabase.child(key).setValue(hashMap);

                this.listWeekly.add(new MyAvailabilityWeekly(dayOfTheWeek, endTime, key, monthOf, startTime));
                this.userdb.addAvailabilityWeekly(dayOfTheWeek, endTime, key, monthOf, startTime);
                Toast.makeText(this, "Weekly Schedule Added", Toast.LENGTH_LONG).show();
            } else if(requestCode == TIME_ADD_MONTHLY_REQUEST_CODE) {
                String date = data.getStringExtra("date");
                String endTime = data.getStringExtra("endTime");
                String startMonth = data.getStringExtra("startMonth");
                String startTime = data.getStringExtra("startTime");

                ArrayList<User> fireArrList = userdb.getAllUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(fireArrList.get(0).getLaundWorker_fbid()).child("modeMonthly");
                HashMap<String, String> hashMap = new HashMap<>();

                String key = mDatabase.push().getKey();
                hashMap.put("availability_date", date);
                hashMap.put("availability_endTime", endTime);
                hashMap.put("availability_key", key);
                hashMap.put("availability_startMonth", startMonth);
                hashMap.put("availability_startTime", startTime);
                mDatabase.child(key).setValue(hashMap);

                this.listMonthly.add(new MyAvailabilityMonthly(date, endTime, key, startMonth, startTime));
                this.userdb.addAvailabilityMonthly(date, endTime, key, startMonth, startTime);
                Toast.makeText(this, "Monthly Schedule Added", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new MyAvailabilitySchedulesMonthlyFragment(), "Monthly");
        adapter.addFragment(new MyAvailabilitySchedulesWeeklyFragment(), "Weekly");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}