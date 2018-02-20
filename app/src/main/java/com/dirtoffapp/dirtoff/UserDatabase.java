package com.dirtoffapp.dirtoff;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.dirtoffapp.dirtoff.jobrequest.Booking;
import com.dirtoffapp.dirtoff.jobrequest.BookingHistory;
import com.dirtoffapp.dirtoff.myaccount.MyAccount;
import com.dirtoffapp.dirtoff.mycalendar.MyAvailabilityMonthly;
import com.dirtoffapp.dirtoff.mycalendar.MyAvailabilityWeekly;

public class UserDatabase extends SQLiteOpenHelper {


    static String DATABASE = "db_user";
    static String TBL_USER = "tbl_user";
    static String TBL_AVAILABILITY = "tbl_availability";
    static String TBL_AVAILABILITY_MONTHLY = "tbl_availability_monthly";
    static String TBL_AVAILABILITY_WEEKLY = "tbl_availability_weekly";
    static String TBL_USER_SERVICES = "tbl_user_services";
    static String TBL_BOOKING = "tbl_booking";
    static String TBL_HISTORY_BOOKING = "tbl_history_booking";

    public UserDatabase(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        String sql = "CREATE TABLE " +TBL_USER+ "(laundWorker_address varchar(100), laundWorker_bdate varchar(15), laundWorker_cnum varchar(11), laundWorker_dateApplied varchar(15), laundWorker_email varchar(50), laundWorker_fbid varchar(20) primary key, laundWorker_fn varchar(50), laundWorker_ln varchar(50), laundWorker_mn varchar(50), laundWorker_pic varchar(100), laundWorker_status varchar(50))";
        arg0.execSQL(sql);
        String sql2 = "CREATE TABLE " +TBL_AVAILABILITY+ "(date varchar(100), endtime varchar(50), id string primary key, starttime varchar(50))";
        arg0.execSQL(sql2);
        String sql01 = "CREATE TABLE " +TBL_AVAILABILITY_MONTHLY+ "(date varchar(20), endTime varchar(50), id string primary key, startMonth varchar(20), startTime varchar(50))";
        arg0.execSQL(sql01);
        String sql02 = "CREATE TABLE " +TBL_AVAILABILITY_WEEKLY+ "(dayOfTheWeek varchar(15), endTime varchar(50), id string primary key, monthOf varchar(20), startTime varchar(50))";
        arg0.execSQL(sql02);
        String sql3 = "CREATE TABLE " +TBL_USER_SERVICES+ "(id integer primary key autoincrement, bulky varchar(10), monthlyServicesFee varchar(100), weeklyServicesFee varchar(100))";
        arg0.execSQL(sql3);
        String sql4 = "CREATE TABLE " +TBL_BOOKING+ "(booking_date varchar(15), booking_id varchar(20) primary key, booking_status varchar(10), booking_time varchar(15), laundWorker_fn varchar(20), laundWorker_fbid varchar(20), laundWorker_ln varchar(20), laundWorker_mn varchar(20), laundWorker_pic varchar(100), laundSeeker_fbid varchar(20))";
        arg0.execSQL(sql4);
        String sql5 = "CREATE TABLE " +TBL_HISTORY_BOOKING+ "(booking_date varchar(15), booking_id varchar(20) primary key, booking_status varchar(10), booking_time varchar(15), laundWorker_fn varchar(20), laundWorker_fbid varchar(20), laundWorker_ln varchar(20), laundWorker_mn varchar(20), laundWorker_pic varchar(100), laundSeeker_fbid varchar(20), booking_service varchar(20), booking_fee varchar(20))";
        arg0.execSQL(sql5);
    }

    public long addUserServices(String bulky, String monthlyServicesFee, String weeklyServicesFee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("bulky", bulky);
        cv.put("monthlyServicesFee", monthlyServicesFee);
        cv.put("weeklyServicesFee", weeklyServicesFee);

        result = db.insert(TBL_USER_SERVICES, null, cv);

        db.close();
        return result;
    }

    public ArrayList<MyAccount> getAllUserServices() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_USER_SERVICES, null, null, null, null, null, "services");
        ArrayList<MyAccount> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String bulky = c.getString(c.getColumnIndex("bulky"));
            String monthlyServicesFee = c.getString(c.getColumnIndex("monthlyServicesFee"));
            String weeklyServicesFee = c.getString(c.getColumnIndex("weeklyServicesFee"));

            list.add(new MyAccount(bulky, monthlyServicesFee, weeklyServicesFee));
        }

        c.close();
        db.close();
        return list;
    }

    public void deleteAllUserServices() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_USER_SERVICES, null, null);

        db.close();
    }

    public long addUser(String address, String bdate, String cnum, String dateApplied, String email, String fbid, String fname, String lname, String mname, String image, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("laundWorker_address", address);
        cv.put("laundWorker_bdate", bdate);
        cv.put("laundWorker_cnum", cnum);
        cv.put("laundWorker_dateApplied", dateApplied);
        cv.put("laundWorker_email", email);
        cv.put("laundWorker_fbid", fbid);
        cv.put("laundWorker_fn", fname);
        cv.put("laundWorker_ln", lname);
        cv.put("laundWorker_mn", mname);
        cv.put("laundWorker_pic", image);
        cv.put("laundWorker_status", status);

        result = db.insert(TBL_USER, null, cv);

        db.close();
        return result;
    }

    public ArrayList<User> getAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<User> list = new ArrayList<>();
        Cursor c = db.query(TBL_USER, null, null, null, null, null, "laundWorker_fbid");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String laundWorker_address = c.getString(c.getColumnIndex("laundWorker_address"));
            String laundWorker_bdate = c.getString(c.getColumnIndex("laundWorker_bdate"));
            String laundWorker_cnum = c.getString(c.getColumnIndex("laundWorker_cnum"));
            String laundWorker_dateApplied = c.getString(c.getColumnIndex("laundWorker_dateApplied"));
            String laundWorker_email = c.getString(c.getColumnIndex("laundWorker_email"));
            String laundWorker_fbid = c.getString(c.getColumnIndex("laundWorker_fbid"));
            String laundWorker_fn = c.getString(c.getColumnIndex("laundWorker_fn"));
            String laundWorker_ln = c.getString(c.getColumnIndex("laundWorker_ln"));
            String laundWorker_mn = c.getString(c.getColumnIndex("laundWorker_mn"));
            String laundWorker_pic = c.getString(c.getColumnIndex("laundWorker_pic"));
            String laundWorker_status = c.getString(c.getColumnIndex("laundWorker_status"));

            list.add(new User(laundWorker_address, laundWorker_bdate, laundWorker_cnum, laundWorker_dateApplied, laundWorker_email, laundWorker_fbid, laundWorker_fn, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundWorker_status));
        }

        c.close();
        db.close();
        return list;
    }

    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_USER, null, null);

        db.close();
    }

    public ArrayList<Availability> getAllAvailability() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Availability> list = new ArrayList<>();
        Cursor c = db.query(TBL_AVAILABILITY, null, null, null, null, null, "id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String date = c.getString(c.getColumnIndex("date"));
            String endTime = c.getString(c.getColumnIndex("endtime"));
            String id = c.getString(c.getColumnIndex("id"));
            String startTime = c.getString(c.getColumnIndex("starttime"));

            list.add(new Availability(date, endTime, id, startTime));
        }

        c.close();
        db.close();
        return list;
    }

    public long addAvailability(String date, String endtime, String id, String starttime) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("date", date);
        cv.put("endtime", endtime);
        cv.put("id", id);
        cv.put("starttime", starttime);
        result = db.insert(TBL_AVAILABILITY, null, cv);

        db.close();
        return result;
    }

    public int deleteAvailability(String id, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_AVAILABILITY, null, null, null, null, null, "id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(date.equals(c.getString(c.getColumnIndex("date")))) {
                time = db.delete(TBL_AVAILABILITY, "id=?", new String[]{id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllAvailability() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_AVAILABILITY, null, null);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_USER);
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_USER_SERVICES);
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_AVAILABILITY);
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_AVAILABILITY_MONTHLY);
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_AVAILABILITY_WEEKLY);
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_BOOKING);
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_HISTORY_BOOKING);
        onCreate(arg0);
    }

    ///////////////////////////////////////////////////////////////////////////
    // MONTHLY
    public long addAvailabilityMonthly(String date, String endTime, String id, String startMonth, String startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("date", date);
        cv.put("endTime", endTime);
        cv.put("id", id);
        cv.put("startMonth", startMonth);
        cv.put("startTime", startTime);
        result = db.insert(TBL_AVAILABILITY_MONTHLY, null, cv);

        db.close();
        return result;
    }

    public ArrayList<MyAvailabilityMonthly> getAllAvailabilityMonthly() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MyAvailabilityMonthly> list = new ArrayList<>();
        Cursor c = db.query(TBL_AVAILABILITY_MONTHLY, null, null, null, null, null, "id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String date = c.getString(c.getColumnIndex("date"));
            String endTime = c.getString(c.getColumnIndex("endTime"));
            String id = c.getString(c.getColumnIndex("id"));
            String startMonth = c.getString(c.getColumnIndex("startMonth"));
            String startTime = c.getString(c.getColumnIndex("startTime"));

            list.add(new MyAvailabilityMonthly(date, endTime, id, startMonth, startTime));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteAvailabilityMonthly(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_AVAILABILITY_MONTHLY, null, null, null, null, null, "id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(id.equals(c.getString(c.getColumnIndex("id")))) {
                time = db.delete(TBL_AVAILABILITY_MONTHLY, "id=?", new String[]{id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllAvailabilityMonthly() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_AVAILABILITY_MONTHLY, null, null);

        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // WEEKLY
    public long addAvailabilityWeekly(String dayOfTheWeek, String endTime, String id, String monthOf, String startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("dayOfTheWeek", dayOfTheWeek);
        cv.put("endTime", endTime);
        cv.put("id", id);
        cv.put("monthOf", monthOf);
        cv.put("startTime", startTime);
        result = db.insert(TBL_AVAILABILITY_WEEKLY, null, cv);

        db.close();
        return result;
    }

    public ArrayList<MyAvailabilityWeekly> getAllAvailabilityWeekly() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MyAvailabilityWeekly> list = new ArrayList<>();
        Cursor c = db.query(TBL_AVAILABILITY_WEEKLY, null, null, null, null, null, "id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String dayOfTheWeek = c.getString(c.getColumnIndex("dayOfTheWeek"));
            String endTime = c.getString(c.getColumnIndex("endTime"));
            String id = c.getString(c.getColumnIndex("id"));
            String monthOf = c.getString(c.getColumnIndex("monthOf"));
            String startTime = c.getString(c.getColumnIndex("startTime"));

            list.add(new MyAvailabilityWeekly(dayOfTheWeek, endTime, id, monthOf, startTime));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteAvailabilityWeekly(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_AVAILABILITY_WEEKLY, null, null, null, null, null, "id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(id.equals(c.getString(c.getColumnIndex("id")))) {
                time = db.delete(TBL_AVAILABILITY_WEEKLY, "id=?", new String[]{id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllAvailabilityWeekly() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_AVAILABILITY_WEEKLY, null, null);

        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // BOOKING
    public long addBooking(String booking_date, String booking_id, String booking_status, String booking_time, String laundWorker_fn, String laundWorker_fbid, String laundWorker_ln, String laundWorker_mn, String laundWorker_pic, String laundSeeker_fbid, String booking_service, String booking_fee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("booking_date", booking_date);
        cv.put("booking_id", booking_id);
        cv.put("booking_status", booking_status);
        cv.put("booking_time", booking_time);
        cv.put("laundWorker_fn", laundWorker_fn);
        cv.put("laundWorker_fbid", laundWorker_fbid);
        cv.put("laundWorker_ln", laundWorker_ln);
        cv.put("laundWorker_mn", laundWorker_mn);
        cv.put("laundWorker_pic", laundWorker_pic);
        cv.put("laundSeeker_fbid", laundSeeker_fbid);
        cv.put("booking_service", booking_service);
        cv.put("booking_fee", booking_fee);
        result = db.insert(TBL_BOOKING, null, cv);

        db.close();
        return result;
    }

    public ArrayList<Booking> getAllBooking() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Booking> list = new ArrayList<>();
        Cursor c = db.query(TBL_BOOKING, null, null, null, null, null, "booking_id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String booking_date = c.getString(c.getColumnIndex("booking_date"));
            String booking_id = c.getString(c.getColumnIndex("booking_id"));
            String booking_status = c.getString(c.getColumnIndex("booking_status"));
            String booking_time = c.getString(c.getColumnIndex("booking_time"));
            String laundWorker_fn = c.getString(c.getColumnIndex("laundWorker_fn"));
            String laundWorker_fbid = c.getString(c.getColumnIndex("laundWorker_fbid"));
            String laundWorker_ln = c.getString(c.getColumnIndex("laundWorker_ln"));
            String laundWorker_mn = c.getString(c.getColumnIndex("laundWorker_mn"));
            String laundWorker_pic = c.getString(c.getColumnIndex("laundWorker_pic"));
            String laundSeeker_fbid = c.getString(c.getColumnIndex("laundSeeker_fbid"));
            String booking_service = c.getString(c.getColumnIndex("booking_service"));
            String booking_fee = c.getString(c.getColumnIndex("booking_fee"));

            list.add(new Booking(booking_date, booking_id, booking_status, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service, booking_fee));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteBooking(String booking_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_BOOKING, null, null, null, null, null, "booking_id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(booking_id.equals(c.getString(c.getColumnIndex("booking_id")))) {
                time = db.delete(TBL_BOOKING, "booking_id=?", new String[]{booking_id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllBooking() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_BOOKING, null, null);

        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // HISTORY BOOKING
    public long addHistoryBooking(String booking_date, String booking_id, String booking_status, String booking_time, String laundWorker_fn, String laundWorker_fbid, String laundWorker_ln, String laundWorker_mn, String laundWorker_pic, String laundSeeker_fbid, String booking_service, String booking_fee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("booking_date", booking_date);
        cv.put("booking_id", booking_id);
        cv.put("booking_status", booking_status);
        cv.put("booking_time", booking_time);
        cv.put("laundWorker_fn", laundWorker_fn);
        cv.put("laundWorker_fbid", laundWorker_fbid);
        cv.put("laundWorker_ln", laundWorker_ln);
        cv.put("laundWorker_mn", laundWorker_mn);
        cv.put("laundWorker_pic", laundWorker_pic);
        cv.put("laundSeeker_fbid", laundSeeker_fbid);
        cv.put("booking_service", booking_service);
        cv.put("booking_fee", booking_fee);

        result = db.insert(TBL_HISTORY_BOOKING, null, cv);

        db.close();
        return result;
    }

    public ArrayList<BookingHistory> getAllHistoryBooking() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<BookingHistory> list = new ArrayList<>();
        Cursor c = db.query(TBL_HISTORY_BOOKING, null, null, null, null, null, "booking_id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String booking_date = c.getString(c.getColumnIndex("booking_date"));
            String booking_id = c.getString(c.getColumnIndex("booking_id"));
            String booking_status = c.getString(c.getColumnIndex("booking_status"));
            String booking_time = c.getString(c.getColumnIndex("booking_time"));
            String laundWorker_fn = c.getString(c.getColumnIndex("laundWorker_fn"));
            String laundWorker_fbid = c.getString(c.getColumnIndex("laundWorker_fbid"));
            String laundWorker_ln = c.getString(c.getColumnIndex("laundWorker_ln"));
            String laundWorker_mn = c.getString(c.getColumnIndex("laundWorker_mn"));
            String laundWorker_pic = c.getString(c.getColumnIndex("laundWorker_pic"));
            String laundSeeker_fbid = c.getString(c.getColumnIndex("laundSeeker_fbid"));
            String booking_service = c.getString(c.getColumnIndex("booking_service"));
            String booking_fee = c.getString(c.getColumnIndex("booking_fee"));

            list.add(new BookingHistory(booking_date, booking_id, booking_status, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service, booking_fee));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteHistoryBooking(String booking_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_HISTORY_BOOKING, null, null, null, null, null, "booking_id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(booking_id.equals(c.getString(c.getColumnIndex("booking_id")))) {
                time = db.delete(TBL_HISTORY_BOOKING, "booking_id=?", new String[]{booking_id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllHistoryBooking() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_HISTORY_BOOKING, null, null);

        db.close();
    }
}