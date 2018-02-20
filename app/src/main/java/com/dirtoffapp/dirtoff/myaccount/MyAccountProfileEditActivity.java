package com.dirtoffapp.dirtoff.myaccount;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.R;
import com.dirtoffapp.dirtoff.User;
import com.dirtoffapp.dirtoff.UserDatabase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAccountProfileEditActivity extends AppCompatActivity {

    final static int ADDRESS_PICK_REQUEST_CODE = 30;

    EditText txtFName, txtMName, txtLName, txtEmail, txtCnum, txtBdate;
    TextView txtAddress;
    Button btnAddress;
    UserDatabase userdb;
    ArrayList<User> list = new ArrayList<>();
    Toolbar toolbar;
    String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccountprofile_edit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.userdb = new UserDatabase(this);
        this.list = userdb.getAllUser();

        this.txtFName = (EditText) this.findViewById(R.id.editText);
        this.txtMName = (EditText) this.findViewById(R.id.editText2);
        this.txtLName = (EditText) this.findViewById(R.id.editText3);
        this.txtEmail = (EditText) this.findViewById(R.id.editText4);
        this.txtCnum = (EditText) this.findViewById(R.id.editText5);
        this.txtBdate = (EditText) this.findViewById(R.id.editText6);
        this.txtAddress = (TextView) this.findViewById(R.id.textView8);
        this.btnAddress = (Button) this.findViewById(R.id.button);

        this.txtFName.setText(list.get(0).getLaundWorker_fn());
        this.txtMName.setText(list.get(0).getLaundWorker_mn());
        this.txtMName.requestFocus();
        this.txtLName.setText(list.get(0).getLaundWorker_ln());
        this.txtEmail.setText(list.get(0).getLaundWorker_email());
        this.txtCnum.setText(list.get(0).getLaundWorker_cnum());
        this.txtBdate.setText(list.get(0).getLaundWorker_bdate());
        this.txtAddress.setText(list.get(0).getLaundWorker_address());
        address = this.txtAddress.getText().toString();

        this.btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent placeIntent;

                try {
                    placeIntent = builder.build(MyAccountProfileEditActivity.this);
                    startActivityForResult(placeIntent, ADDRESS_PICK_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
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
                if(txtFName.getText().toString().trim().equals("") || txtMName.getText().toString().trim().equals("") || txtLName.getText().toString().trim().equals("") || txtEmail.getText().toString().trim().equals("") || address.equals("") || txtCnum.getText().toString().trim().equals("") || txtBdate.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please pick an address and fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers");
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("laundWorker_address", address);
                    hashMap.put("laundWorker_bdate", txtBdate.getText().toString());
                    hashMap.put("laundWorker_cnum", txtCnum.getText().toString());
                    hashMap.put("laundWorker_dateApplied", this.list.get(0).getLaundWorker_dateApplied());
                    hashMap.put("laundWorker_email", txtEmail.getText().toString());
                    hashMap.put("laundWorker_fbid", this.list.get(0).getLaundWorker_fbid());
                    hashMap.put("laundWorker_fn", txtFName.getText().toString());
                    hashMap.put("laundWorker_ln", txtLName.getText().toString());
                    hashMap.put("laundWorker_mn", txtMName.getText().toString());
                    hashMap.put("laundWorker_pic", this.list.get(0).getLaundWorker_pic());
                    hashMap.put("laundWorker_status", this.list.get(0).getLaundWorker_status());
                    mDatabase.child(this.list.get(0).getLaundWorker_fbid()).setValue(hashMap);

                    this.userdb.deleteAllUser();
                    this.userdb.addUser(address, txtBdate.getText().toString(), txtCnum.getText().toString(), this.list.get(0).getLaundWorker_dateApplied(), txtEmail.getText().toString(), this.list.get(0).getLaundWorker_fbid(), txtFName.getText().toString(), txtLName.getText().toString(), txtMName.getText().toString(), this.list.get(0).getLaundWorker_pic(), this.list.get(0).getLaundWorker_status());
                    Toast.makeText(this, "Profile Edited", Toast.LENGTH_SHORT).show();
                    this.finish();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADDRESS_PICK_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                address = place.getAddress().toString();
                txtAddress.setText(address);
            } else if(resultCode == Activity.RESULT_CANCELED) {
                if(address.equals("")) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setTitle("Message");
                    alertBuilder.setMessage("You must select your location.");
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            Intent intent;

                            try {
                                intent = builder.build(MyAccountProfileEditActivity.this);
                                startActivityForResult(intent, ADDRESS_PICK_REQUEST_CODE);
                            } catch (GooglePlayServicesRepairableException e) {
                                e.printStackTrace();
                            } catch (GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    AlertDialog dialog = alertBuilder.create();
                    dialog.show();
                }
            }
        }
    }
}