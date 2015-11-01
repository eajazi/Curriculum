package reachingimmortality.com.curriculum.http_calls;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;


public class JSONFunctions {

    //Controllers
    private JSONParser jsonParser;
    private SessionManager sessionManager;

    //URL of the PHP API
    public static final String indexURL = "http://virido.hr/ajki/beastmode/";

    public static final String create_pdfURL = "http://virido.hr/ajki/beastmode/include/create_pdf.php";
    //TAGS
    public static final String TAG = "tag";
    public static final String login_tag = "login";
    public static final String google_login_tag = "google_login";
    public static final String register_tag = "register";
    public static final String forpass_tag = "forpass";
    public static final String chgpass_tag = "chgpass";
    public static final String profile_tag = "profile";
    public static final String application_tag = "application";
    public static final String experience_tag = "experience";
    public static final String education_tag = "education";
    public static final String language_tag = "language";
    public static final String evaluation_tag = "evaluation";
    public static final String skills_tag = "skills";
    public static final String export_cv_tag = "export_cv";

    //GET DATA TAGS
    public static final String get_user_application_tag = "get_user_aplication";
    public static final String get_user_experience_tag = "get_user_experience";
    public static final String get_user_education_tag = "get_user_education";
    public static final String get_user_language_tag = "get_user_language";
    public static final String get_language_eval_tag = "get_lang_eval";

    public static final String application_add_tag = "application_add";
    public static final String application_update_tag = "application_update";

    public static final String experience_add_tag = "experience_add";
    public static final String experience_update_tag = "experience_update";
    public static final String experience_delete_tag = "experience_delete";

    public static final String education_add_tag = "education_add";
    public static final String education_update_tag = "education_update";
    public static final String education_delete_tag = "education_delete";

    public static final String language_add_tag = "language_add";
    public static final String language_update_tag = "language_update";
    public static final String language_delete_tag = "language_delete";

    public static final String evaluation_add_tag = "evaluation_add";
    public static final String evaluation_update_tag = "evaluation_update";
    public static final String evaluation_delete_tag = "evaluation_delete";

    public static final String skills_add_tag = "skills_add";
    public static final String skills_update_tag = "skills_update";
    public static final String skills_delete_tag = "skills_delete";

    //JSON RESPONSE KEYS - IS THERE DATA
    public static final String IS_USER_PRO = "is_user_pro";
    public static final String IS_USER_APP = "is_user_app";
    public static final String IS_USER_EXP = "is_user_exp";
    public static final String IS_USER_EDU = "is_user_edu";
    public static final String IS_USER_LANG = "is_user_lang";
    public static final String IS_LANG_EV = "is_lang_ev";

    //TYPE
    public static final String SKILLS_TYPE = "skillType";

    //SKILLS TYPES
    public static final String COMMUNICATION_TYPE = "communication_type";
    public static final String MANAGAMENT_TYPE = "managament_type";
    public static final String JOB_RELATED_TYPE = "job_related_type";
    public static final String DIG_COMPETENCE_TYPE = "digital_competence_type";

    //JSON KEYS
    public static final String JSON_ERROR = "error";
    public static final String JSON_SUCCESS = "success";
    public static final String JSON_ERROR_MESSAGE = "error_message";
    public static final String JSON_CASE_TAG = "case_tag";
    public static final int JSON_RESPONSE_ADD = 3;
    public static final int JSON_RESPONSE_UPDATE = 4;
    public static final String JSON_DELETE_TAG = "delete";
    public static final String JSON_DELETE_ITEM_ID = "itemDeletedID";

    //Login - Register JSON USER
    public static final String JSON_LOGIN = "loginJSON";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_UID = "users_uid";
    public static final String USER_UNIQUE_ID = "unique_id";
    public static final String USER_USERNAME = "uname";
    public static final String USER_CREATED_AT = "created_at";

    //PROFILE RECORDS
    public static final String JSON_PROFILE= "profileJSON";
    public static final String PROFILE_NAME = "name";
    public static final String PROFILE_SURNAME = "surname";
    public static final String PROFILE_EMAIL = "email_pro";
    public static final String PROFILE_TEL_NUMBER = "tel_number";
    public static final String PROFILE_STREET_ADRESS = "street_adress";
    public static final String PROFILE_POSTAL_CODE = "postal_code";
    public static final String PROFILE_CITY= "city";
    public static final String PROFILE_COUNTRY = "country";
    public static final String PROFILE_WEB_BLOG = "web_blog";
    public static final String PROFILE_USERS_UID = "users_uid";

    //APPLICATION KEYS
    public static final String JSON_APPLICATION = "applicationJSON";
    public static final String APPLICATION_ID = "application_id";
    public static final String APPLICATION_TYPE__ID = "type__id";
    public static final String APPLICATION_DESCRIPTION = "description";

    //APPLICATION KEYS
    public static final String USER_APPLICATION_ID = "user_application_id";
    public static final String USER_APPLICATION_USERS__ID = "users_uid";
    public static final String USER_APPLICATION_APPLICATION__ID = "application__id";

    //EXPERIENCE KEYS
    public static final String JSON_EXPERIENCE = "experienceJSON";
    public static final String EXPERIENCE__ID = "experience_id";
    public static final String EXPERIENCE_START_DATE = "start_date";
    public static final String EXPERIENCE_END_DATE = "end_date";
    public static final String EXPERIENCE_POSITION = "position";
    public static final String EXPERIENCE_EMPLOYER = "employer";
    public static final String EXPERIENCE_CITY= "city";
    public static final String EXPERIENCE_COUNTRY = "country";
    public static final String EXPERIENCE_MAIN_ACTIVITIES = "main_activities";

    //USER EXPERIENCE KEYS
    public static final String USER_EXPERIENCE_ID = "user_experience_id";
    public static final String USER_EXPERIENCE_USERS__ID = "users_uid";
    public static final String USER_EXPERIENCE_EXPERIENCE__ID = "experience__id";

    //EDUCATION KEYS
    public static final String JSON_EDUCATION = "educationJSON";
    public static final String EDUCATION__ID = "education_id";
    public static final String EDUCATION_START_DATE = "start_date";
    public static final String EDUCATION_END_DATE = "end_date";
    public static final String EDUCATION_QUALIFICATION = "qualification";
    public static final String EDUCATION_PROVIDER = "provider";
    public static final String EDUCATION_CITY= "city";
    public static final String EDUCATION_COUNTRY = "country";
    public static final String EDUCATION_MAIN_SUBJECTS = "main_subjects";

    //USER EDUCATION KEYS
    public static final String USER_EDUCATION_ID = "user_education_id";
    public static final String USER_EDUCATION_USERS__ID = "users_uid";
    public static final String USER_EDUCATION_EDUCATION__ID = "education__id";

    //SKILLS KEYS
    public static final String JSON_SKILLS = "skillsJSON";
    public static final String SKILL__ID = "skills_id";
    public static final String SKILL_COMMUNICATION = "communication";
    public static final String SKILL_MANAGMENT = "managament";
    public static final String SKILL_JOB_RELATED = "job_related";
    public static final String SKILL_IT = "digital_competence";
    public static final String SKILL_USERS_UID = "users_uid";

    //LANGUAGE KEYS
    public static final String JSON_LANGUAGE = "languageJSON";
    public static final String LANGUAGE__ID = "languages_id";
    public static final String LANGUAGE_NAME = "language_name";
    public static final String LANGUAGE_TYPE_FK = "type_language__id";

    //USER LANGUAGE KEYS
    public static final String USER_LANGUAGE_ID = "user_languages_id";
    public static final String USER_LANGUAGE_LANGUAGE__ID = "language__id";
    public static final String USER_LANGUAGE_USERS__ID = "users_uid";
    //LANGUAGE EVALUATION
    public static final String JSON_EVALUATION = "evaluationJSON";
    public static final String LANGUAGE_EVALUATION_ID ="language_eval_id";
    public static final String LANGUAGE_EVALUATION_LANGUAGE__ID ="language__id";
    public static final String LANGUAGE_EVALUATION_ASSESSMENT__ID ="evaluation__id";

    // constructor
    public JSONFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        JSONObject json = jsonParser.getJSONFromUrl(indexURL, params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(indexURL, params);
        return json;
    }





    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(indexURL, params);
        return json;
    }






     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String email, String uname, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));

        JSONObject json = jsonParser.getJSONFromUrl(indexURL,params);
        return json;
    }


    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean clearTempDataSQLite(Context context){

        SQLController db = new SQLController(context);
        db.open();
        db.resetTables();
        db.close();
        return true;
    }

    //PROFILE DATA
    public String composeProfileJSONfromSQLite(Context context) {
        ArrayList<HashMap<String, String>> profileList;
        profileList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_PROFILE, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(PROFILE_NAME, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_NAME)));
                map.put(PROFILE_SURNAME, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_SURNAME)));
                map.put(PROFILE_TEL_NUMBER, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_TEL_NUMBER)));
                map.put(PROFILE_STREET_ADRESS, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_STREET_ADRESS)));
                map.put(PROFILE_POSTAL_CODE, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_POSTAL_CODE)));
                map.put(PROFILE_CITY, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_CITY)));
                map.put(PROFILE_COUNTRY, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_COUNTRY)));
                map.put(PROFILE_WEB_BLOG, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_WEB_BLOG)));
                map.put(PROFILE_USERS_UID, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_USERS_PK_FK)));
                profileList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Gson gson = new GsonBuilder().create();
        Log.d("profileList", "" + profileList);
        //Use GSON to serialize Array List to JSON
        return gson.toJson(profileList);
    }

    //Profile list from SQLite
    public ArrayList<HashMap<String, String>> getProfileFromSQLite(Context context) {
        ArrayList<HashMap<String, String>> profileList;
        profileList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_PROFILE, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(PROFILE_NAME, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_NAME)));
                map.put(PROFILE_SURNAME, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_SURNAME)));
                map.put(PROFILE_TEL_NUMBER, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_TEL_NUMBER)));
                map.put(PROFILE_STREET_ADRESS, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_STREET_ADRESS)));
                map.put(PROFILE_STREET_ADRESS, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_POSTAL_CODE)));
                map.put(PROFILE_CITY, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_CITY)));
                map.put(PROFILE_COUNTRY, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_COUNTRY)));
                map.put(PROFILE_WEB_BLOG, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_WEB_BLOG)));
                map.put(PROFILE_USERS_UID, cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_USERS_PK_FK)));
                profileList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return profileList;
    }

    //EXPERIENCE DATA
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeExperienceJSONfromSQLite(Context context) {
        ArrayList<HashMap<String, String>> experienceList;
        experienceList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(EXPERIENCE__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_ID)));
                map.put(EXPERIENCE_START_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_START_DATE)));
                map.put(EXPERIENCE_END_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_END_DATE)));
                map.put(EXPERIENCE_POSITION, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_POSITION)));
                map.put(EXPERIENCE_EMPLOYER, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_EMPLOYER)));
                map.put(EXPERIENCE_CITY, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_CITY)));
                map.put(EXPERIENCE_COUNTRY, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_COUNTRY)));
                map.put(EXPERIENCE_MAIN_ACTIVITIES, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_MAIN_ACTIVITIES)));
                experienceList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(experienceList);
    }

    //get Experience data from SQLite
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public ArrayList<HashMap<String, String>> getExperienceFromSQLite(Context context) {
        ArrayList<HashMap<String, String>> experienceList;
        experienceList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(EXPERIENCE__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_ID)));
                map.put(EXPERIENCE_START_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_START_DATE)));
                map.put(EXPERIENCE_END_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_END_DATE)));
                map.put(EXPERIENCE_POSITION, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_POSITION)));
                map.put(EXPERIENCE_EMPLOYER, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_EMPLOYER)));
                map.put(EXPERIENCE_CITY, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_CITY)));
                map.put(EXPERIENCE_COUNTRY, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_COUNTRY)));
                map.put(EXPERIENCE_MAIN_ACTIVITIES, cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_MAIN_ACTIVITIES)));
                experienceList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return experienceList;
    }

    //Compose JSON Education from SQLite
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeEducationJSONfromSQLite(Context context) {
        ArrayList<HashMap<String, String>> educationList;
        educationList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_EDUCATION, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(EDUCATION__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_ID)));
                map.put(EDUCATION_START_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_START_DATE)));
                map.put(EDUCATION_END_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_END_DATE)));
                map.put(EDUCATION_QUALIFICATION, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_QUALIFICATION_NAME)));
                map.put(EDUCATION_PROVIDER, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_PROVIDER)));
                map.put(EDUCATION_CITY, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_CITY)));
                map.put(EDUCATION_COUNTRY, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_COUNTRY)));
                map.put(EDUCATION_MAIN_SUBJECTS, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_MAIN_SUBJECTS)));
                educationList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(educationList);
    }

    //Get education Arraylist from SQLite
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public ArrayList<HashMap<String, String>> getEducationFromSQLite(Context context) {
        ArrayList<HashMap<String, String>> educationList;
        educationList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_EDUCATION,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(EDUCATION__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_ID)));
                map.put(EDUCATION_START_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_START_DATE)));
                map.put(EDUCATION_END_DATE, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_END_DATE)));
                map.put(EDUCATION_QUALIFICATION, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_QUALIFICATION_NAME)));
                map.put(EDUCATION_PROVIDER, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_PROVIDER)));
                map.put(EDUCATION_CITY, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_CITY)));
                map.put(EDUCATION_COUNTRY, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_COUNTRY)));
                map.put(EDUCATION_MAIN_SUBJECTS, cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_MAIN_SUBJECTS)));educationList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return educationList;
    }

    //COMPOSE USER EXPERIENCE JSON FROM SQLite
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeUserExperienceJSONfromSQLite(Context context) {
        ArrayList<HashMap<String, String>> experienceUserList;
        experienceUserList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_USER_WORK_EXPERIENCE, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(USER_EXPERIENCE_USERS__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_WORK_EXPERIENCE_USER_FK)));
                map.put(USER_EXPERIENCE_EXPERIENCE__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_WORK_EXPERIENCE_WORK_EXPERIENCE_FK)));
                experienceUserList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(experienceUserList);
    }

    //GET USER EXPERIENCE ARRAYLIST FROM SQLite
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public ArrayList<HashMap<String, String>> getUserExperienceFromSQLite(Context context) {
        ArrayList<HashMap<String, String>> experienceUserList;
        experienceUserList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_USER_WORK_EXPERIENCE,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(USER_EXPERIENCE_USERS__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_WORK_EXPERIENCE_USER_FK)));
                map.put(USER_EXPERIENCE_EXPERIENCE__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_WORK_EXPERIENCE_WORK_EXPERIENCE_FK)));
                experienceUserList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return experienceUserList;
    }

    //COMPOSE USER EDUCATION JSON FROM SQLITE
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeUserEducationJSONfromSQLite(Context context) {
        ArrayList<HashMap<String, String>> educationUserList;
        educationUserList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_USER_EDUCATION, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(USER_EDUCATION_USERS__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_EDUCATION_USERS_FK)));
                map.put(USER_EDUCATION_EDUCATION__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_EDUCATION_EDUCATION_FK)));
                educationUserList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(educationUserList);
    }

    //GET USER EDUCATION ARRAYLIST FROM SQLite
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public ArrayList<HashMap<String, String>> getUserEducationFromSQLite(Context context) {
        ArrayList<HashMap<String, String>> experienceUserList;
        experienceUserList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_USER_EDUCATION,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(USER_EDUCATION_USERS__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_EDUCATION_USERS_FK)));
                map.put(USER_EDUCATION_EDUCATION__ID, cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_EDUCATION_EDUCATION_FK)));
                experienceUserList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return experienceUserList;
    }
    //Compose JSON USERS
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeUserList(String uname,String email,String password){

        //ArrayList profileList
        ArrayList<HashMap<String,String>> userList = new ArrayList<>();
        //HashMap profile
        HashMap<String,String> user = new HashMap<>();
        user.put(USER_USERNAME, uname);
        user.put(USER_EMAIL, email);
        user.put(USER_PASSWORD, password);

        userList.add(user);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(userList);
    }
    //Compose JSON PROFILE
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeProfileList(String name,String surname,String email,String streetAdress,String postalCode,
                                                          String city,String country,String telNumber,String webBlog){

        //ArrayList profileList
        ArrayList<HashMap<String,String>> profileList = new ArrayList<>();
        //HashMap profile
        HashMap<String,String> profile = new HashMap<>();
        profile.put(PROFILE_NAME, name);
        profile.put(PROFILE_SURNAME, surname);
        profile.put(PROFILE_EMAIL, email);
        profile.put(PROFILE_POSTAL_CODE, postalCode);
        profile.put(PROFILE_STREET_ADRESS, streetAdress);
        profile.put(PROFILE_CITY, city);
        profile.put(PROFILE_COUNTRY, country);
        profile.put(PROFILE_TEL_NUMBER, telNumber);
        profile.put(PROFILE_WEB_BLOG, webBlog);

        profileList.add(profile);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(profileList);
    }

    //Compose JSON APPLICATION
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeApplicationList(String applicationID,String description,String typeID, String userApplicationID){

        //ArrayList profileList
        ArrayList<HashMap<String,String>> applicationList = new ArrayList<>();
        //HashMap profile
        HashMap<String,String> application = new HashMap<>();

        application.put(APPLICATION_ID, applicationID);
        application.put(APPLICATION_DESCRIPTION, description);
        application.put(APPLICATION_TYPE__ID, typeID);
        application.put(USER_APPLICATION_ID, userApplicationID);

        applicationList.add(application);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(applicationList);
    }

    //Compose JSON EXPEREINCE
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeExperienceJSON(String experienceID,String startDate, String endDate, String position, String employer, String city,
                                        String country, String mainActivities) {

        //ArrayList experienceList
        ArrayList<HashMap<String,String>> experienceList = new ArrayList<>();
        //HashMap experience
        HashMap<String,String> experience = new HashMap<>();

        experience.put(EXPERIENCE__ID,experienceID);
        experience.put(EXPERIENCE_START_DATE,startDate);
        experience.put(EXPERIENCE_END_DATE,endDate);
        experience.put(EXPERIENCE_POSITION,position);
        experience.put(EXPERIENCE_EMPLOYER,employer);
        experience.put(EXPERIENCE_CITY,city);
        experience.put(EXPERIENCE_COUNTRY,country);
        experience.put(EXPERIENCE_MAIN_ACTIVITIES,mainActivities);

        experienceList.add(experience);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(experienceList);
    }

    //COMPOSE JSON EDUCATION
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeEducationJSON(String educationID,String startDate, String endDate, String qualification, String provider, String city,
                                        String country, String mainSubjects) {
        //ArrayList educationList
        ArrayList<HashMap<String,String>> educationList = new ArrayList<>();
        //HashMap education
        HashMap<String,String> education = new HashMap<>();
        education.put(EDUCATION__ID, educationID);
        education.put(EDUCATION_START_DATE, startDate);
        education.put(EDUCATION_END_DATE, endDate);
        education.put(EDUCATION_QUALIFICATION, qualification);
        education.put(EDUCATION_PROVIDER, provider);
        education.put(EDUCATION_CITY, city);
        education.put(EDUCATION_COUNTRY, country);
        education.put(EDUCATION_MAIN_SUBJECTS, mainSubjects);

        educationList.add(education);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(educationList);
    }

    //Compose JSON SKILLS
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeSkillsJSON(String communication, String managment,String it, String jobRelated) {

        //ArrayList skillsList
        ArrayList<HashMap<String,String>> skillsList = new ArrayList<>();
        //HashMap skills
        HashMap<String,String> skills = new HashMap<>();
        skills.put(SKILL_COMMUNICATION, communication);
        skills.put(SKILL_MANAGMENT, managment);
        skills.put(SKILL_IT, it);
        skills.put(SKILL_JOB_RELATED, jobRelated);

        skillsList.add(skills);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(skillsList);
    }

    //Compose JSON LANGUAGE
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeLanguageJSON(String languageID, String languageName, String languageType) {

        //ArrayList languageList
        ArrayList<HashMap<String,String>> languageList = new ArrayList<>();
        //HashMap language
        HashMap<String,String> language = new HashMap<>();
        language.put(LANGUAGE__ID, languageID);
        language.put(LANGUAGE_NAME, languageName);
        language.put(LANGUAGE_TYPE_FK, languageType);

        languageList.add(language);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(languageList);
    }

    //COMPOSE JSON EVALUATION
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeEvaluationJSON(ArrayList<HashMap<String, String>> evaluationList ) {

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(evaluationList);
    }

    //Compose JSON COMMUNICATION
    //************************************************************************************************************************************
    //************************************************************************************************************************************
    public String composeCommunicationJSON(String skillsID, String skillsType, String skillsValue) {

        ArrayList<HashMap<String,String>> skillsList = new ArrayList<>();
        HashMap<String,String> skill = new HashMap<>();

        skill.put(SKILL__ID, skillsID);

        switch (skillsType){
            //COMMUNICATION TYPE
            case COMMUNICATION_TYPE:
                skill.put(SKILL_COMMUNICATION, skillsValue);
                break;

            //MANAGAMENT TYPE
            case MANAGAMENT_TYPE:
                skill.put(SKILL_MANAGMENT, skillsValue);
                break;

            //JOB RELATED TYPE
            case JOB_RELATED_TYPE:
                skill.put(SKILL_JOB_RELATED, skillsValue);
                break;

            //DIGITAL COMPETENCE TYPE
            case DIG_COMPETENCE_TYPE:
                skill.put(SKILL_IT, skillsValue);
                break;
        }

        skillsList.add(skill);

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON
        return gson.toJson(skillsList);
    }


}

