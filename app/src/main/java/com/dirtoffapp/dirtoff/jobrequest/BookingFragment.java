package com.dirtoffapp.dirtoff.jobrequest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class BookingFragment extends Fragment {

    ListView lv;
    //BookingListHistoryAdapter adapter;
    BookingListAdapter adapter;
    ArrayList<Booking> bookingList = new ArrayList<>();
    //ArrayList<BookingHistory> historyList = new ArrayList<>();
    ArrayList<User> workerList = new ArrayList<>();
    UserDatabase userDatabase;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        getActivity().setTitle("Booking List");
        userDatabase = new UserDatabase(getActivity());
        bookingList = userDatabase.getAllBooking();
        //historyList = userDatabase.getAllHistoryBooking();

        lv = view.findViewById(R.id.listView);
        adapter = new BookingListAdapter(getContext(), bookingList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent bookingIntent = new Intent(getActivity(), JobRequestActivity.class);
                startActivity(bookingIntent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    workerList = userDatabase.getAllUser();

                    try {
                        Booking booking;
                        bookingList.clear();
                        //BookingHistory historyBooking;
                        //historyList.clear();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);
                            //historyBooking = child.getValue(BookingHistory.class);

                            if(booking != null) {
                            //if(historyBooking != null) {
                                //if(seekerList.get(0).getLaundSeeker_fbid().equals(booking.getLaundSeeker_fbid())) {
                                //if(workerList.get(0).getLaundWorker_fbid().equals(historyBooking.getLaundWorker_fbid())) {
                                if(workerList.get(0).getLaundWorker_fbid().equals(booking.getLaundWorker_fbid())) {
                                    bookingList.add(booking);
                                    //historyList.add(historyBooking);
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        //Toast.makeText(getActivity(), "Error " +e.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Error ", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}