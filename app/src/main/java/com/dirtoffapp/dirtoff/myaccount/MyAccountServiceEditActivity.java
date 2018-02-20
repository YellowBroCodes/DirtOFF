package com.dirtoffapp.dirtoff.myaccount;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.R;
import com.dirtoffapp.dirtoff.User;
import com.dirtoffapp.dirtoff.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MyAccountServiceEditActivity extends AppCompatActivity {

    EditText txtWeekWash, txtWeekFold, txtWeekIron, txtMonthWash, txtMonthFold, txtMonthIron;
    String bulky;
    CheckBox checkBox;
    Toolbar toolbar;
    UserDatabase userdb;
    ArrayList<User> fireList = new ArrayList<>();
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccountservice_edit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.txtWeekWash = (EditText) this.findViewById(R.id.txtWeekWash);
        this.txtWeekFold = (EditText) this.findViewById(R.id.txtWeekFold);
        this.txtWeekIron = (EditText) this.findViewById(R.id.txtWeekIron);
        this.txtMonthWash = (EditText) this.findViewById(R.id.txtMonthWash);
        this.txtMonthFold = (EditText) this.findViewById(R.id.txtMonthFold);
        this.txtMonthIron = (EditText) this.findViewById(R.id.txtMonthIron);
        this.checkBox = (CheckBox) this.findViewById(R.id.checkBox);

        userdb = new UserDatabase(this);
        fireList = userdb.getAllUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryServices").child(fireList.get(0).getLaundWorker_fbid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    bulky = dataSnapshot.child("bulky").getValue(String.class);
                    String mFee = dataSnapshot.child("monthlyServicesFee").getValue(String.class);
                    String wFee = dataSnapshot.child("weeklyServicesFee").getValue(String.class);

                    if(bulky != null && !bulky.equals("")) {
                        checkBox.setChecked(true);
                    }
                    if(mFee != null && !mFee.equals("")) {
                        StringTokenizer monthlyTokens = new StringTokenizer(mFee, ",");
                        if(monthlyTokens.hasMoreTokens()) {
                            while(monthlyTokens.hasMoreTokens()) {
                                String token = monthlyTokens.nextToken();
                                switch(token) {
                                    case "Wash":
                                        txtMonthWash.setText(monthlyTokens.nextToken());
                                        break;
                                    case "Fold":
                                        txtMonthFold.setText(monthlyTokens.nextToken());
                                        break;
                                    case "Iron":
                                        txtMonthIron.setText(monthlyTokens.nextToken());
                                        break;
                                }
                            }
                        }
                    }
                    if(wFee != null && !wFee.equals("")) {
                        StringTokenizer weeklyTokens = new StringTokenizer(wFee, ",");
                        if(weeklyTokens.hasMoreTokens()) {
                            while(weeklyTokens.hasMoreTokens()) {
                                String token = weeklyTokens.nextToken();
                                switch(token) {
                                    case "Wash":
                                        txtWeekWash.setText(weeklyTokens.nextToken());
                                        break;
                                    case "Fold":
                                        txtWeekFold.setText(weeklyTokens.nextToken());
                                        break;
                                    case "Iron":
                                        txtWeekIron.setText(weeklyTokens.nextToken());
                                        break;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    bulky = "BULKY";

                } else {
                    bulky = "";
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.saveServices:
                String weekWash = txtWeekWash.getText().toString();
                String weekFold = txtWeekFold.getText().toString();
                String weekIron = txtWeekIron.getText().toString();
                String monthWash = txtMonthWash.getText().toString();
                String monthFold = txtMonthFold.getText().toString();
                String monthIron = txtMonthIron.getText().toString();
                String weeklyServicesFee = "";
                String monthlyServicesFee = "";

                if(!weekWash.equals("")) {
                    if(!weeklyServicesFee.equals("")) {
                        weeklyServicesFee += ",Wash," + weekWash;
                    } else {
                        weeklyServicesFee += "Wash," + weekWash;
                    }
                }
                if(!weekFold.equals("")) {
                    if(!weeklyServicesFee.equals("")) {
                        weeklyServicesFee += ",Fold," + weekFold;
                    } else {
                        weeklyServicesFee += "Fold," + weekFold;
                    }
                }
                if(!weekIron.equals("")) {
                    if(!weeklyServicesFee.equals("")) {
                        weeklyServicesFee += ",Iron," + weekIron;
                    } else {
                        weeklyServicesFee += "Iron," + weekIron;
                    }
                }
                if(!monthWash.equals("")) {
                    if(!monthlyServicesFee.equals("")) {
                        monthlyServicesFee += ",Wash," + monthWash;
                    } else {
                        monthlyServicesFee += "Wash," + monthWash;
                    }
                }
                if(!monthFold.equals("")) {
                    if(!monthlyServicesFee.equals("")) {
                        monthlyServicesFee += ",Fold," + monthFold;
                    } else {
                        monthlyServicesFee += "Fold," + monthFold;
                    }
                }
                if(!monthIron.equals("")) {
                    if(!monthlyServicesFee.equals("")) {
                        monthlyServicesFee += ",Iron," + monthIron;
                    } else {
                        monthlyServicesFee += "Iron," + monthIron;
                    }
                }

                if(!weeklyServicesFee.equals("") || !monthlyServicesFee.equals("") || !bulky.equals("")) {
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("bulky", bulky);
                    hashMap.put("monthlyServicesFee", monthlyServicesFee);
                    hashMap.put("weeklyServicesFee", weeklyServicesFee);
                    mDatabase.setValue(hashMap);

                    userdb.deleteAllUserServices();
                    userdb.addUserServices(bulky, monthlyServicesFee, weeklyServicesFee);
                    Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
                    this.finish();
                } else {
                    Toast.makeText(this, "Please select at least one service", Toast.LENGTH_SHORT).show();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}