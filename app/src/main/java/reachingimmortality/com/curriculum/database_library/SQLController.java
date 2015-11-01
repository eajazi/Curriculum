package reachingimmortality.com.curriculum.database_library;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.HashMap;

import reachingimmortality.com.curriculum.R;


public class SQLController {
    //The Android's default system path of your application database.

    public SQLiteDatabase database;
    public ExternalDbOpenHelper dbHelper;
    public Context context;

    //SQLController
    public SQLController(Context con) {
        context = con;
    }

    public SQLController open() throws SQLException {
        dbHelper = new ExternalDbOpenHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    //delete temp user
    public void resetTables(){
        database = dbHelper.getReadableDatabase();
        // Delete All Rows
        database.delete(MyContentProvider.USER_TABLE, null, null);
        database.delete(MyContentProvider.PROFILE_TABLE,null,null);

        database.delete(MyContentProvider.APPLICATION_TABLE,null,null);
        database.delete(MyContentProvider.USER_APPLICATION_TABLE,null,null);

        database.delete(MyContentProvider.WORK_EXPERIENCE_TABLE,null,null);
        database.delete(MyContentProvider.USER_WORK_EXPERIENCE_TABLE,null,null);

        database.delete(MyContentProvider.EDUCATION_TABLE,null,null);
        database.delete(MyContentProvider.USER_EDUCATION_TABLE,null,null);

        database.delete(MyContentProvider.SKILL_TABLE, null, null);
        database.delete(MyContentProvider.LANGUAGE_TABLE, null, null);
        database.delete(MyContentProvider.LANGUAGE_EVALUATION_TABLE, null, null);

        database.close();
    }

    //get User details
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_USER,null,null,null,null);
        // Move to first row
        if(cursor.moveToFirst()){
            user.put(MyContentProvider.USER_EMAIL, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_EMAIL)));
            user.put(MyContentProvider.USER_UNAME, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_UNAME)));
            user.put(MyContentProvider.USER_ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_ID)));
            user.put(MyContentProvider.USER_UID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_UID)));
        }
        cursor.close();
        database.close();
        // return user
        return user;
    }

    //GET START DATE SECTION
    public HashMap<String, String> getExperienceDateSection(){
        HashMap<String,String> date = new HashMap<>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_DATE_EXPERIENCE,null,null,null,null);
        // Move to first row
        if(cursor.moveToFirst()){
            date.put(MyContentProvider.DATE_EXPERIENCE_START_DAY, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EXPERIENCE_START_DAY)));
            date.put(MyContentProvider.DATE_EXPERIENCE_START_MONTH, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EXPERIENCE_START_MONTH)));
            date.put(MyContentProvider.DATE_EXPERIENCE_START_YEAR, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EXPERIENCE_START_YEAR)));
            date.put(MyContentProvider.DATE_EXPERIENCE_END_DAY, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EXPERIENCE_END_DAY)));
            date.put(MyContentProvider.DATE_EXPERIENCE_END_MONTH, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EXPERIENCE_END_MONTH)));
            date.put(MyContentProvider.DATE_EXPERIENCE_END_YEAR, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EXPERIENCE_END_YEAR)));

        }
        cursor.close();
        database.close();
        // return user
        return date;
    }

    //GET START DATE SECTION
    public HashMap<String, String> getEducationDateSection(){
        HashMap<String,String> date = new HashMap<>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_DATE_EDUCATION,null,null,null,null);
        // Move to first row
        if(cursor.moveToFirst()){
            date.put(MyContentProvider.DATE_EDUCATION_START_DAY, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EDUCATION_START_DAY)));
            date.put(MyContentProvider.DATE_EDUCATION_START_MONTH, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EDUCATION_START_MONTH)));
            date.put(MyContentProvider.DATE_EDUCATION_START_YEAR, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EDUCATION_START_YEAR)));
            date.put(MyContentProvider.DATE_EDUCATION_END_DAY, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EDUCATION_END_DAY)));
            date.put(MyContentProvider.DATE_EDUCATION_END_MONTH, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EDUCATION_END_MONTH)));
            date.put(MyContentProvider.DATE_EDUCATION_END_YEAR, cursor.getString(cursor.getColumnIndex(MyContentProvider.DATE_EDUCATION_END_YEAR)));

        }
        cursor.close();
        database.close();
        // return user
        return date;
    }

    public HashMap<String,String> getProfileDetails() {
        HashMap<String,String> profile = new HashMap<String,String>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_PROFILE,null,null,null,null);
        // Move to first row
        if(cursor.moveToFirst()){
            profile.put(MyContentProvider.PROFILE_NAME, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_NAME)));
            profile.put(MyContentProvider.PROFILE_SURNAME, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_SURNAME)));
        }
        cursor.close();
        database.close();
        // return user
        return profile;
    }
}