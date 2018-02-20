package com.dirtoffapp.dirtoff.jobrequest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class JobRequestActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    AlertDialog dialog;
    ImageView iv;
    TextView txtBookingId, txtBookingDate, txtBookingTime, txtName, txtEmail, txtGender, txtContact, txtLink;
    Button btnConfirm, btnDecline;
    DatabaseReference bookingReference, seekerReference, historyReference;
    UserDatabase userDatabase;
    ArrayList<Booking> bookingList = new ArrayList<>();
    //ArrayList<Seeker> seekerList = new ArrayList<>();
    ArrayList<User> workerList = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request);

        setTitle("Job Request Confirmation");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userDatabase = new UserDatabase(this);
        workerList = userDatabase.getAllUser();

        this.iv = (ImageView) this.findViewById(R.id.imageView);
        this.txtBookingId = (TextView) this.findViewById(R.id.textView);
        this.txtBookingDate = (TextView) this.findViewById(R.id.textView1);
        this.txtBookingTime = (TextView) this.findViewById(R.id.textView2);
        this.txtName = (TextView) this.findViewById(R.id.textView3);
        this.txtEmail = (TextView) this.findViewById(R.id.textView4);
        this.txtGender = (TextView) this.findViewById(R.id.textView5);
        this.txtContact = (TextView) this.findViewById(R.id.textView6);
        this.txtLink = (TextView) this.findViewById(R.id.textView7);
        this.btnConfirm = (Button) this.findViewById(R.id.button);
        this.btnDecline = (Button) this.findViewById(R.id.button1);

        bookingReference = FirebaseDatabase.getInstance().getReference().child("bookingList");
        bookingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        Booking booking = null;
                        bookingList.clear();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);

                            if(booking != null) {
                                if(workerList.get(0).getLaundWorker_fbid().equals(booking.getLaundWorker_fbid())) {
                                    bookingList.add(booking);
                                    txtBookingId.setText(new StringBuilder().append("Booking Id: ").append(booking.getBooking_id()));
                                    txtBookingDate.setText(new StringBuilder().append("Booking Date: ").append(booking.getBooking_date()));
                                    txtBookingTime.setText(new StringBuilder().append("Booking Time: ").append(booking.getBooking_time()));

                                    userDatabase.addBooking(
                                            booking.getBooking_date(),
                                            booking.getBooking_id(),
                                            booking.getBooking_status(),
                                            booking.getBooking_time(),
                                            booking.getLaundWorker_fn(),
                                            booking.getLaundWorker_fbid(),
                                            booking.getLaundWorker_ln(),
                                            booking.getLaundWorker_mn(),
                                            booking.getLaundWorker_pic(),
                                            booking.getLaundSeeker_fbid(),
                                            booking.getBooking_service(),
                                            booking.getBooking_fee()
                                    );
                                }
                            }
                        }

                        if(booking != null && !booking.getBooking_status().equalsIgnoreCase("Pending")) {
                            btnConfirm.setVisibility(View.GONE);
                            btnDecline.setVisibility(View.GONE);
                        }
                    } catch(Exception e) {
                        Toast.makeText(JobRequestActivity.this, "Error " +e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bookingList = userDatabase.getAllBooking();
        if(!bookingList.isEmpty()) {
            seekerReference = FirebaseDatabase.getInstance().getReference().child("laundrySeekers").child(bookingList.get(0).getLaundSeeker_fbid());
            seekerReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String name = dataSnapshot.child("laundSeeker_fn").getValue(String.class) +" "+ dataSnapshot.child("laundSeeker_ln").getValue(String.class);
                        txtName.setText(name);
                        txtEmail.setText(dataSnapshot.child("laundSeeker_email").getValue(String.class));
                        txtGender.setText(dataSnapshot.child("laundSeeker_gender").getValue(String.class));
                        txtContact.setText(dataSnapshot.child("laundSeeker_cnum").getValue(String.class));
                        txtLink.setText(dataSnapshot.child("laundSeeker_link").getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JobRequestActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you really want to accept the booking?");
                builder.setPositiveButton("Yes", JobRequestActivity.this);
                builder.setNegativeButton("No", JobRequestActivity.this);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JobRequestActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you really want to decline the booking?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!bookingList.isEmpty()) {
                            if (!bookingList.get(0).getBooking_status().equalsIgnoreCase("Canceled")) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("booking_date", bookingList.get(0).getBooking_date());
                                hashMap.put("booking_id", bookingList.get(0).getBooking_id());
                                hashMap.put("booking_status", "Canceled");
                                hashMap.put("booking_time", bookingList.get(0).getBooking_time());
                                hashMap.put("laundWorker_fn", bookingList.get(0).getLaundWorker_fn());
                                hashMap.put("laundWorker_fbid", bookingList.get(0).getLaundWorker_fbid());
                                hashMap.put("laundWorker_ln", bookingList.get(0).getLaundWorker_ln());
                                hashMap.put("laundWorker_mn", bookingList.get(0).getLaundWorker_mn());
                                hashMap.put("laundWorker_pic", bookingList.get(0).getLaundWorker_pic());
                                hashMap.put("laundSeeker_fbid", bookingList.get(0).getLaundSeeker_fbid());
                                hashMap.put("booking_service", bookingList.get(0).getBooking_service());
                                hashMap.put("booking_fee", bookingList.get(0).getBooking_fee());
                                bookingReference.child(bookingList.get(0).getBooking_id()).setValue(hashMap);

                                userDatabase.addBooking(
                                        bookingList.get(0).getBooking_date(),
                                        bookingList.get(0).getBooking_id(),
                                        "Canceled",
                                        bookingList.get(0).getBooking_time(),
                                        bookingList.get(0).getLaundWorker_fn(),
                                        bookingList.get(0).getLaundWorker_fbid(),
                                        bookingList.get(0).getLaundWorker_ln(),
                                        bookingList.get(0).getLaundWorker_mn(),
                                        bookingList.get(0).getLaundWorker_pic(),
                                        bookingList.get(0).getLaundSeeker_fbid(),
                                        bookingList.get(0).getBooking_service(),
                                        bookingList.get(0).getBooking_fee()
                                );

                                historyReference = FirebaseDatabase.getInstance().getReference().child("bookingListHistory");
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                String key1 = historyReference.push().getKey();
                                hashMap1.put("booking_date", bookingList.get(0).getBooking_date());
                                hashMap1.put("booking_id", key1);
                                hashMap1.put("booking_status", "Canceled");
                                hashMap1.put("booking_time", bookingList.get(0).getBooking_time());
                                hashMap1.put("laundWorker_fn", bookingList.get(0).getLaundWorker_fn());
                                hashMap1.put("laundWorker_fbid", bookingList.get(0).getLaundWorker_fbid());
                                hashMap1.put("laundWorker_ln", bookingList.get(0).getLaundWorker_ln());
                                hashMap1.put("laundWorker_mn", bookingList.get(0).getLaundWorker_mn());
                                hashMap1.put("laundWorker_pic", bookingList.get(0).getLaundWorker_pic());
                                hashMap1.put("laundSeeker_fbid", bookingList.get(0).getLaundSeeker_fbid());
                                hashMap1.put("booking_service", bookingList.get(0).getBooking_service());
                                hashMap1.put("booking_fee", bookingList.get(0).getBooking_fee());
                                historyReference.child(key1).setValue(hashMap1);

                                Toast.makeText(JobRequestActivity.this, "Success", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                btnConfirm.setVisibility(View.GONE);
                                btnDecline.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(JobRequestActivity.this, "Already Decline", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(JobRequestActivity.this, "No Booked Schedule", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch(i) {
            case DialogInterface.BUTTON_POSITIVE:
                if(!bookingList.isEmpty()) {
                    if (!bookingList.get(0).getBooking_status().equalsIgnoreCase("Progress")) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("booking_date", bookingList.get(0).getBooking_date());
                        hashMap.put("booking_id", bookingList.get(0).getBooking_id());
                        hashMap.put("booking_status", "Progress");
                        hashMap.put("booking_time", bookingList.get(0).getBooking_time());
                        hashMap.put("laundWorker_fn", bookingList.get(0).getLaundWorker_fn());
                        hashMap.put("laundWorker_fbid", bookingList.get(0).getLaundWorker_fbid());
                        hashMap.put("laundWorker_ln", bookingList.get(0).getLaundWorker_ln());
                        hashMap.put("laundWorker_mn", bookingList.get(0).getLaundWorker_mn());
                        hashMap.put("laundWorker_pic", bookingList.get(0).getLaundWorker_pic());
                        hashMap.put("laundSeeker_fbid", bookingList.get(0).getLaundSeeker_fbid());
                        hashMap.put("booking_service", bookingList.get(0).getBooking_service());
                        hashMap.put("booking_fee", bookingList.get(0).getBooking_fee());
                        bookingReference.child(bookingList.get(0).getBooking_id()).setValue(hashMap);

                        userDatabase.addBooking(
                                bookingList.get(0).getBooking_date(),
                                bookingList.get(0).getBooking_id(),
                                "Progress",
                                bookingList.get(0).getBooking_time(),
                                bookingList.get(0).getLaundWorker_fn(),
                                bookingList.get(0).getLaundWorker_fbid(),
                                bookingList.get(0).getLaundWorker_ln(),
                                bookingList.get(0).getLaundWorker_mn(),
                                bookingList.get(0).getLaundWorker_pic(),
                                bookingList.get(0).getLaundSeeker_fbid(),
                                bookingList.get(0).getBooking_service(),
                                bookingList.get(0).getBooking_fee()
                        );

                        historyReference = FirebaseDatabase.getInstance().getReference().child("bookingListHistory");
                        HashMap<String, String> hash = new HashMap<>();
                        String key = historyReference.push().getKey();
                        hash.put("booking_date", bookingList.get(0).getBooking_date());
                        hash.put("booking_id", key);
                        hash.put("booking_status", "Progress");
                        hash.put("booking_time", bookingList.get(0).getBooking_time());
                        hash.put("laundWorker_fn", bookingList.get(0).getLaundWorker_fn());
                        hash.put("laundWorker_fbid", bookingList.get(0).getLaundWorker_fbid());
                        hash.put("laundWorker_ln", bookingList.get(0).getLaundWorker_ln());
                        hash.put("laundWorker_mn", bookingList.get(0).getLaundWorker_mn());
                        hash.put("laundWorker_pic", bookingList.get(0).getLaundWorker_pic());
                        hash.put("laundSeeker_fbid", bookingList.get(0).getLaundSeeker_fbid());
                        hash.put("booking_service", bookingList.get(0).getBooking_service());
                        hash.put("booking_fee", bookingList.get(0).getBooking_fee());
                        historyReference.child(key).setValue(hash);

                        Toast.makeText(JobRequestActivity.this, "Success", Toast.LENGTH_LONG).show();
                        btnConfirm.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(JobRequestActivity.this, "Already Accepted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(JobRequestActivity.this, "No Booked Schedule", Toast.LENGTH_LONG).show();
                }

            case DialogInterface.BUTTON_NEGATIVE:
                dialogInterface.dismiss();
                break;
        }
    }
}