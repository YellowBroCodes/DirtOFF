package com.dirtoffapp.dirtoff.jobrequest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class JobRequestFragment extends Fragment {

    ImageView iv;
    TextView txtBookingId, txtBookingDate, txtBookingTime, txtName, txtEmail, txtGender, txtContact, txtLink;
    Button btnConfirm, btnDecline;
    DatabaseReference bookingReference, seekerReference, historyReference;
    UserDatabase userDatabase;
    ArrayList<Booking> bookingList = new ArrayList<>();
    //ArrayList<Seeker> seekerList = new ArrayList<>();
    ArrayList<User> workerList = new ArrayList<>();

    public JobRequestFragment() {
        // Required empty public constructor
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_request, container, false);

        getActivity().setTitle("Job Request Confirmation");

        userDatabase = new UserDatabase(getContext());
        workerList = userDatabase.getAllUser();

        iv = view.findViewById(R.id.imageView);
        txtBookingId = view.findViewById(R.id.textView);
        txtBookingDate = view.findViewById(R.id.textView1);
        txtBookingTime = view.findViewById(R.id.textView2);
        txtName = view.findViewById(R.id.textView3);
        txtEmail = view.findViewById(R.id.textView4);
        txtGender = view.findViewById(R.id.textView5);
        txtContact = view.findViewById(R.id.textView6);
        txtLink = view.findViewById(R.id.textView7);
        btnConfirm = view.findViewById(R.id.button);
        btnDecline = view.findViewById(R.id.button1);

        bookingReference = FirebaseDatabase.getInstance().getReference().child("bookingList");
        bookingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        Booking booking;
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
                                            booking.getLaundSeeker_fbid()
                                    );
                                }
                            }
                        }
                    } catch(Exception e) {
                        Toast.makeText(getActivity(), "Error " +e.getMessage(), Toast.LENGTH_LONG).show();
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
                                bookingList.get(0).getLaundSeeker_fbid()
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
                        historyReference.child(key).setValue(hash);

                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Already Accepted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Booked Schedule", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                bookingList.get(0).getLaundSeeker_fbid()
                        );

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
                        historyReference.child(key1).setValue(hashMap1);

                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Already Decline", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Booked Schedule", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }*/
}