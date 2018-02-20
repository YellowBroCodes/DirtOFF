package com.dirtoffapp.dirtoff;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    CallbackManager callbackManager;
    LoginButton loginButton;
    UserDatabase userdb;
    ArrayList<User> list = new ArrayList<>();
    DatabaseReference mDatabase;
    String profileId="", firstName="", middleName="", lastName="", email="", address="", status="Unverified", cnum="", bdate="", dateApplied="";
    Uri profilePictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers");
        userdb = new UserDatabase(this);
        list = userdb.getAllUser();

        loginButton = (LoginButton) findViewById(R.id.fb_login_bn);
        if(AccessToken.getCurrentAccessToken() == null) {
            loginButton.setReadPermissions("email", "public_profile");
            callbackManager = CallbackManager.Factory.create();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Profile profile = Profile.getCurrentProfile();

                            if(profile != null) {
                                try {
                                    profileId = object.getString("id");
                                    firstName = object.getString("first_name");
                                    lastName = object.getString("last_name");
                                    email = object.getString("email");
                                    dateApplied = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                profilePictureUri = profile.getProfilePictureUri(100, 100);
                                middleName = profile.getMiddleName();

                                /*// get user availability from firebase
                                DatabaseReference mSchedDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(profileId);
                                mSchedDatabase.orderByKey().addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            userdb.deleteAllAvailability();

                                            for(DataSnapshot child : dataSnapshot.getChildren()) {
                                                userdb.addAvailability(child.child("availability_date").getValue(String.class), child.child("availability_endTime").getValue(String.class), child.child("availability_id").getValue(String.class), child.child("availability_startTime").getValue(String.class));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });*/

                                // check if user is old user or new
                                Query id = mDatabase.orderByKey().equalTo(profileId);
                                id.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        // if old user => get user information from firebase
                                        if(dataSnapshot.exists()) {
                                            try {
                                                User user = null;

                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    user = child.getValue(User.class);
                                                }

                                                if(user != null) {
                                                    userdb.deleteAllUser();
                                                    userdb.addUser(user.getLaundWorker_address(), user.getLaundWorker_bdate(), user.getLaundWorker_cnum(), user.getLaundWorker_dateApplied(), user.getLaundWorker_email(), user.getLaundWorker_fbid(), user.getLaundWorker_fn(), user.getLaundWorker_ln(), user.getLaundWorker_mn(), user.getLaundWorker_pic(), user.getLaundWorker_status());
                                                    Toast.makeText(getApplicationContext(), "Welcome Back! " + user.getLaundWorker_fn() + " " + user.getLaundWorker_mn() + " " + user.getLaundWorker_ln(), Toast.LENGTH_LONG).show();
                                                }

                                                Intent navIntent2 = new Intent(getApplicationContext(), NavigationDrawer.class);
                                                startActivity(navIntent2);
                                            } catch(Exception e) {
                                                Toast.makeText(getApplicationContext(), "Error" +e.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                            // if new user => push user information to firebase
                                        } else {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("laundWorker_address", address);
                                            hashMap.put("laundWorker_bdate", bdate);
                                            hashMap.put("laundWorker_cnum", cnum);
                                            hashMap.put("laundWorker_dateApplied", dateApplied);
                                            hashMap.put("laundWorker_email", email);
                                            hashMap.put("laundWorker_fbid", profileId);
                                            hashMap.put("laundWorker_fn", firstName);
                                            hashMap.put("laundWorker_ln", lastName);
                                            hashMap.put("laundWorker_mn", middleName);
                                            hashMap.put("laundWorker_pic", profilePictureUri.toString());
                                            hashMap.put("laundWorker_status", status);

                                            mDatabase.child(profileId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Error in storing data to the cloud", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                            userdb.deleteAllUser();
                                            userdb.deleteAllUserServices();
                                            userdb.deleteAllAvailability();
                                            list.add(new User(address, bdate, cnum, dateApplied, email, profileId, firstName, lastName, middleName, profilePictureUri.toString(), status));
                                            userdb.addUser(address, bdate, cnum, dateApplied, email, profileId, firstName, lastName, middleName, profilePictureUri.toString(), status);
                                            Toast.makeText(getApplicationContext(), "Hello " +firstName+ " " +middleName+ " " +lastName, Toast.LENGTH_LONG).show();

                                            Intent navIntent = new Intent(getApplicationContext(), NavigationDrawer.class);
                                            startActivity(navIntent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(), "Error! Please check your internet connection", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "first_name, last_name, email, id, link, gender");
                    graphRequest.setParameters(parameters);
                    graphRequest.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Login Canceled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), "Login Error! Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Welcome Back! "+ list.get(0).getLaundWorker_fn() +" "+ list.get(0).getLaundWorker_mn() +" "+ list.get(0).getLaundWorker_ln(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            Query id = mDatabase.orderByKey().equalTo(list.get(0).getLaundWorker_fbid());
            id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        User user = null;

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            user = child.getValue(User.class);
                        }

                        if (user != null) {
                            userdb.deleteAllUser();
                            userdb.addUser(user.getLaundWorker_address(), user.getLaundWorker_bdate(), user.getLaundWorker_cnum(), user.getLaundWorker_dateApplied(), user.getLaundWorker_email(), user.getLaundWorker_fbid(), user.getLaundWorker_fn(), user.getLaundWorker_ln(), user.getLaundWorker_mn(), user.getLaundWorker_pic(), user.getLaundWorker_status());
                            Toast.makeText(getApplicationContext(), "Welcome Back! " + user.getLaundWorker_fn() + " " + user.getLaundWorker_mn() + " " + user.getLaundWorker_ln(), Toast.LENGTH_LONG).show();
                        }

                        Intent navIntent2 = new Intent(getApplicationContext(), NavigationDrawer.class);
                        startActivity(navIntent2);
                    } else {
                        Toast.makeText(getApplicationContext(), "Not Exist! Something went wrong! Please restart the application", Toast.LENGTH_LONG).show();
                        userdb.deleteAllUser();
                        userdb.deleteAllAvailability();
                        userdb.deleteAllUserServices();
                        MainActivity.this.finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("No", this);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch(i) {
            case DialogInterface.BUTTON_POSITIVE:
                this.finish();
                System.exit(0);

                break;
        }
    }
}