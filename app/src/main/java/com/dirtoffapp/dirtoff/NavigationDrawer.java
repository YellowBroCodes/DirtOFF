package com.dirtoffapp.dirtoff;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dirtoffapp.dirtoff.jobrequest.BookingFragment;
import com.dirtoffapp.dirtoff.jobrequest.JobRequestFragment;
import com.dirtoffapp.dirtoff.mycalendar.MyAvailabilitySchedulesActivity;
import com.dirtoffapp.dirtoff.mywallet.WalletFragment;
import com.dirtoffapp.dirtoff.support.SupportFragment;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.dirtoffapp.dirtoff.myaccount.MyAccountActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final static int ADDRESS_PICK_REQUEST_CODE = 30;

    TextView navFullname;
    ArrayList<User> list = new ArrayList<>();
    UserDatabase userdb;
    ImageView iv = null;
    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navdrawer);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setTitle("Dirt Off");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        /////////
        this.userdb = new UserDatabase(this);
        this.list = this.userdb.getAllUser();

        if(!list.isEmpty()) {
            String image = this.list.get(0).getLaundWorker_pic();
            String str = this.list.get(0).getLaundWorker_fn()+ " " +this.list.get(0).getLaundWorker_mn()+ " " +this.list.get(0).getLaundWorker_ln();

            iv = headerView.findViewById(R.id.imageView);
            navFullname = headerView.findViewById(R.id.textView);
            navFullname.setText(str);

            if(list.get(0).getLaundWorker_address().trim().equals("")) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent placeIntent;

                try {
                    placeIntent = builder.build(this);
                    startActivityForResult(placeIntent, ADDRESS_PICK_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }

            if(!image.startsWith("file")) {
                new NavigationDrawer.DownloadImage(iv).execute(image);
            } else {
                try {
                    Uri uri = Uri.parse(image);
                    iv.setImageURI(uri);
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "Error image" +e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "Something went wrong! Please restart the application", Toast.LENGTH_LONG).show();
            LoginManager.getInstance().logOut();
            userdb.deleteAllUser();
            userdb.deleteAllAvailability();
            userdb.deleteAllUserServices();
            this.finish();
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Uri uri;
        UserDatabase db;
        ArrayList<User> list = new ArrayList<>();

        private DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
            db = new UserDatabase(NavigationDrawer.this);
            list = db.getAllUser();
            db.deleteAllUser();
        }

        protected Bitmap doInBackground(String...urls) {
            String urldisplay = urls[0];
            Bitmap micon = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                micon = BitmapFactory.decodeStream(in);

                uri = getImageUri(micon);
                list.get(0).setLaundWorker_pic(uri.toString());
                db.addUser(list.get(0).getLaundWorker_address(), list.get(0).getLaundWorker_bdate(), list.get(0).getLaundWorker_cnum(), list.get(0).getLaundWorker_dateApplied(), list.get(0).getLaundWorker_email(), list.get(0).getLaundWorker_fbid(), list.get(0).getLaundWorker_fn(), list.get(0).getLaundWorker_ln(), list.get(0).getLaundWorker_mn(), uri.toString(), list.get(0).getLaundWorker_status());

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers");
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("laundWorker_address", list.get(0).getLaundWorker_address());
                hashMap.put("laundWorker_bdate", list.get(0).getLaundWorker_bdate());
                hashMap.put("laundWorker_cnum", list.get(0).getLaundWorker_cnum());
                hashMap.put("laundWorker_dateApplied", list.get(0).getLaundWorker_dateApplied());
                hashMap.put("laundWorker_email", list.get(0).getLaundWorker_email());
                hashMap.put("laundWorker_fbid", list.get(0).getLaundWorker_fbid());
                hashMap.put("laundWorker_fn", list.get(0).getLaundWorker_fn());
                hashMap.put("laundWorker_ln", list.get(0).getLaundWorker_ln());
                hashMap.put("laundWorker_mn", list.get(0).getLaundWorker_mn());
                hashMap.put("laundWorker_pic", uri.toString());
                hashMap.put("laundWorker_status", list.get(0).getLaundWorker_status());
                mDatabase.child(this.list.get(0).getLaundWorker_fbid()).setValue(hashMap);
            } catch(Exception e) {
                Toast.makeText(getApplicationContext(), "Error! Please check your internet connection", Toast.LENGTH_LONG).show();
            }

            return micon;
        }

        protected void onPostExecute(Bitmap result) {
            try {
                Uri uri = Uri.parse(list.get(0).getLaundWorker_pic());
                bmImage.setImageURI(uri);
            } catch(Exception e) {
                //Log.d("TAG", e.getMessage());
            }
        }

        private Uri getImageUri(Bitmap icon) {
            Uri uri = null;

            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                // calculate insamplesize
                options.inSampleSize = calculateInSampleSize(options, 100, 100);

                // decode bitmap with insamplesize set
                options.inJustDecodeBounds = false;
                Bitmap newBitmap = Bitmap.createScaledBitmap(icon, 100,100, true);
                File file = new File(NavigationDrawer.this.getFilesDir(), "Image" +new Random().nextInt()+ ".jpeg");
                FileOutputStream out = NavigationDrawer.this.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                // get absolute path
                String realPath = file.getAbsolutePath();
                File f = new File(realPath);
                uri = Uri.fromFile(f);
            } catch (Exception e) {
                //Log.e("Error: ", e.getMessage());
                //e.printStackTrace();
            }

            return uri;
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height/ (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio; } final float totalPixels = width * height; final float totalReqPixelsCap = reqWidth * reqHeight * 2; while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            Intent calendar = new Intent(NavigationDrawer.this, MyAvailabilitySchedulesActivity.class);
            startActivity(calendar);
        } else if (id == R.id.nav_account) {
            Intent account = new Intent(NavigationDrawer.this, MyAccountActivity.class);
            startActivity(account);
        }
        else if (id == R.id.nav_mail) {

        }
        else if (id == R.id.nav_file) {
            BookingFragment bookingFragment = new BookingFragment();
            FragmentManager bookingManager = getSupportFragmentManager();
            bookingManager.beginTransaction().replace(
                    R.id.linearLayout,
                    bookingFragment,
                    bookingFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_wallet) {
            WalletFragment walletFragment = new WalletFragment();
            FragmentManager walletManager = getSupportFragmentManager();
            walletManager.beginTransaction().replace(
                    R.id.linearLayout,
                    walletFragment,
                    walletFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_support) {
            SupportFragment supportFragment = new SupportFragment();
            FragmentManager supportManager = getSupportFragmentManager();
            supportManager.beginTransaction().replace(
                    R.id.linearLayout,
                    supportFragment,
                    supportFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            this.userdb.deleteAllUser();
            this.userdb.deleteAllAvailability();
            this.userdb.deleteAllUserServices();
            this.userdb.deleteAllAvailabilityMonthly();
            this.userdb.deleteAllAvailabilityWeekly();
            this.userdb.deleteAllBooking();
            this.userdb.deleteAllHistoryBooking();
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADDRESS_PICK_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                address = place.getAddress().toString();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers");
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("laundWorker_address", address);
                hashMap.put("laundWorker_bdate", this.list.get(0).getLaundWorker_bdate());
                hashMap.put("laundWorker_cnum", this.list.get(0).getLaundWorker_cnum());
                hashMap.put("laundWorker_dateApplied", this.list.get(0).getLaundWorker_dateApplied());
                hashMap.put("laundWorker_email", this.list.get(0).getLaundWorker_email());
                hashMap.put("laundWorker_fbid", this.list.get(0).getLaundWorker_fbid());
                hashMap.put("laundWorker_fn", this.list.get(0).getLaundWorker_fn());
                hashMap.put("laundWorker_ln", this.list.get(0).getLaundWorker_ln());
                hashMap.put("laundWorker_mn", this.list.get(0).getLaundWorker_mn());
                hashMap.put("laundWorker_pic", this.list.get(0).getLaundWorker_pic());
                hashMap.put("laundWorker_status", this.list.get(0).getLaundWorker_status());
                mDatabase.child(this.list.get(0).getLaundWorker_fbid()).setValue(hashMap);

                userdb.deleteAllUser();
                userdb.addUser(address, list.get(0).getLaundWorker_bdate(), list.get(0).getLaundWorker_cnum(), list.get(0).getLaundWorker_dateApplied(), list.get(0).getLaundWorker_email(), list.get(0).getLaundWorker_fbid(), list.get(0).getLaundWorker_fn(), list.get(0).getLaundWorker_ln(), list.get(0).getLaundWorker_mn(), list.get(0).getLaundWorker_pic(), list.get(0).getLaundWorker_status());
                Toast.makeText(this, "Address was added to database", Toast.LENGTH_LONG).show();
            } else if(resultCode == Activity.RESULT_CANCELED) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("Message");
                alertBuilder.setMessage("You must select your location.");
                alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        Intent intent;

                        try {
                            intent = builder.build(NavigationDrawer.this);
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
