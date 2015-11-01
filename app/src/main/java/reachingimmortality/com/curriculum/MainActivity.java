package reachingimmortality.com.curriculum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.software.shell.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.MainCallback;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;
import reachingimmortality.com.curriculum.ui_login.LoginActivity;

public class MainActivity extends MaterialNavigationDrawer implements MainCallback {

    //DATE PICKER CONSTANTS
    public static final String DIALOG_CASE = "datePickerCase";
    public static final String DATE_DAY = "dateDay";
    public static final String DATE_MONTH = "dateMonth";
    public static final String DATE_YEAR = "dateYear";
    public static final String DATE_FROM = "fromDate";
    public static final String DATE_TO = "enDate";
    public static final String DATE_EDU_FROM = "eduFrom";
    public static final String DATE_EDU_TO = "eduTo";

    //SKILLS CONSTANTS
    public static final int CASE_MOTHER_TONG = 1;
    public static final int CASE_OTHER_LANG = 2;
    public static final int CASE_OTHER_SKILLS= 3;

    //FAB DELAY
    public static final int ACTION_BUTTON_POST_DELAY_MS = 800;

    //UI ELEMENTS - CALL TO SERVER
    private MaterialDialog pDialog;
    private ActionButton fabMain;

    //TOAST
    private Toast mToast;
    //CONTROLLERS
    private SQLController sqlController;
    //HASHMAPS
    private HashMap<String, String> userDetails;

    @Override
    public void init(Bundle savedInstanceState) {
        // set the header image
        this.setDrawerHeaderImage(R.mipmap.bground_drawer_header);

        // create sections
        this.addSection(newSection(getResources().getString(R.string.title_profile), new ProfileMainFragment()));
        this.addSection(newSection(getResources().getString(R.string.title_application), new ApplicationMainFragment()));
        this.addSection(newSection(getResources().getString(R.string.title_experience),new ExperienceMainFragment()));
        this.addSection(newSection(getResources().getString(R.string.title_education),new EducationMainFragment()));
        this.addSection(newSection(getResources().getString(R.string.title_languages),new LanguageMainFragment()));
        this.addSection(newSection(getResources().getString(R.string.title_skills),new SkillsMainFragment()));
        this.addSection(newSection(getResources().getString(R.string.title_export),new ExportMainFragment()));
        //this.addSection(newSection("Section 3", R.drawable.ic_mic_white_24dp, new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
        //this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));

        // create bottom section
        //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            //Saving to shared prefrences
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.setLogin(false);

            //Starting activity Login
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("isLoggedOut",true);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sqlController = new SQLController(this);
        fabMain = (ActionButton) findViewById(R.id.fabMain);
        this.setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_24dp);
    }

    @Override
    public void onHomeAsUpSelected() {
        // when the back arrow is selected this method is called
    }


    //MYSQL INSERT
    //******************************************************************************************************************
    //******************************************************************************************************************
    @Override
    public void saveProfileData(String jsonProfileList) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (jsonProfileList != null) {
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            params.put(JSONFunctions.TAG, JSONFunctions.profile_tag);
            params.put(JSONFunctions.JSON_PROFILE, jsonProfileList);
            params.put(JSONFunctions.PROFILE_USERS_UID, userDetails.get(MyContentProvider.USER_ID));

            pDialog = new MaterialDialog.Builder(this)
                    .title(R.string.p_dialog_wiz_title)
                    .content(R.string.p_dialog_content_wait)
                    .widgetColorRes(R.color.material_dialog_buttons)
                    .progress(true, 0)
                    .show();

            client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pDialog.hide();
                    pDialog.dismiss();
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    System.out.println(response);
                    pDialog.hide();
                    pDialog.dismiss();

                    try {

                        JSONArray arr = new JSONArray(response);
                        System.out.println(arr.length());

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jsonProfile = (JSONObject) arr.get(i);
                            String success = jsonProfile.getString(JSONFunctions.JSON_SUCCESS);
                            String error = jsonProfile.getString(JSONFunctions.JSON_ERROR);

                            if (Integer.parseInt(success) == 1) {
                                ContentValues values = new ContentValues();
                                values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                values.put(MyContentProvider.PROFILE_USERS_PK_FK, jsonProfile.getString(JSONFunctions.PROFILE_USERS_UID));
                                values.put(MyContentProvider.PROFILE_NAME, jsonProfile.getString(JSONFunctions.PROFILE_NAME));
                                values.put(MyContentProvider.PROFILE_SURNAME, jsonProfile.getString(JSONFunctions.PROFILE_SURNAME));
                                values.put(MyContentProvider.PROFILE_EMAIL, jsonProfile.getString(JSONFunctions.PROFILE_EMAIL));
                                values.put(MyContentProvider.PROFILE_TEL_NUMBER, jsonProfile.getString(JSONFunctions.PROFILE_TEL_NUMBER));
                                values.put(MyContentProvider.PROFILE_STREET_ADRESS, jsonProfile.getString(JSONFunctions.PROFILE_STREET_ADRESS));
                                values.put(MyContentProvider.PROFILE_COUNTRY, jsonProfile.getString(JSONFunctions.PROFILE_COUNTRY));
                                values.put(MyContentProvider.PROFILE_CITY, jsonProfile.getString(JSONFunctions.PROFILE_CITY));
                                values.put(MyContentProvider.PROFILE_POSTAL_CODE, jsonProfile.getString(JSONFunctions.PROFILE_POSTAL_CODE));
                                values.put(MyContentProvider.PROFILE_WEB_BLOG, jsonProfile.getString(JSONFunctions.PROFILE_WEB_BLOG));

                                //INSERT INTO PROFILE TABLE
                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_PROFILE, values);

                                //GET BACK TO PROFILE FRAGMENT
                                onBackPressed();
                            } else {
                                showToast(getResources().getString(R.string.message_unexpected_message));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }
    }

    @Override
    public void saveApplicationData(String applicationCase, String applicationJSONList) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        if (applicationJSONList != null) {
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();
            Log.d("", userDetails.get(MyContentProvider.USER_ID));
            Log.d("", applicationCase);
            Log.d("", applicationJSONList);
            pDialog = new MaterialDialog.Builder(this)
                    .title(R.string.p_dialog_wiz_title)
                    .content(R.string.p_dialog_content_wait)
                    .widgetColorRes(R.color.material_dialog_buttons)
                    .progress(true, 0)
                    .show();

            params.put(JSONFunctions.TAG, JSONFunctions.application_tag);
            params.put(JSONFunctions.JSON_CASE_TAG, applicationCase);
            params.put(JSONFunctions.USER_APPLICATION_USERS__ID, userDetails.get(MyContentProvider.USER_ID));
            params.put(JSONFunctions.JSON_APPLICATION, applicationJSONList);



            client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pDialog.hide();
                    pDialog.dismiss();
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    System.out.println(response);
                    pDialog.hide();
                    pDialog.dismiss();
                    try {
                        //INSERT INTO SQLITE
                        JSONArray arr = new JSONArray(response);
                        System.out.println(arr.length());
                        //Insert into sqlite databse
                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject jsonApplication = (JSONObject) arr.get(i);
                            String success = jsonApplication.getString(JSONFunctions.JSON_SUCCESS);
                            String error = jsonApplication.getString(JSONFunctions.JSON_ERROR);
                            if(Integer.parseInt(success)==1) {

                                String applicationCase = jsonApplication.getString(JSONFunctions.JSON_CASE_TAG);

                                ContentValues values = new ContentValues();

                                values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                values.put(MyContentProvider.APPLICATION_ID, jsonApplication.getString(JSONFunctions.APPLICATION_ID));
                                values.put(MyContentProvider.APPLICATION_DESCRIPTION, jsonApplication.getString(JSONFunctions.APPLICATION_DESCRIPTION));
                                values.put(MyContentProvider.APPLICATION_TYPE_ID_FK, jsonApplication.getString(JSONFunctions.APPLICATION_TYPE__ID));

                                //INSERT INTO APPLICATION TABLE
                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_APPLICATION, values);

                                if(Integer.parseInt(applicationCase) == JSONFunctions.JSON_RESPONSE_ADD){
                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                    contentValues.put(MyContentProvider.USER_APPLICATION_ID, jsonApplication.getString(JSONFunctions.USER_APPLICATION_ID));
                                    contentValues.put(MyContentProvider.USER_APPLICATION_USER_FK, jsonApplication.getString(JSONFunctions.USER_APPLICATION_USERS__ID));
                                    contentValues.put(MyContentProvider.USER_APPLICATION_APPLICATION_FK, jsonApplication.getString(JSONFunctions.USER_APPLICATION_APPLICATION__ID));


                                    //INSERT INTO USER APPLICATION TABLE
                                    getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_APPLICATION, contentValues);
                                }
                                //GO BACK
                                onBackPressed();
                            }else if(Integer.parseInt(error)==1){
                                showToast("Error in inserting data");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }
    }
    //SAVE EXPERIENCE
    //********************************************************************************************************************
    //********************************************************************************************************************
    @Override
    public void saveExperience(String experienceCase, String jsonExperienceList) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (jsonExperienceList != null) {
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            params.put(JSONFunctions.TAG, JSONFunctions.experience_tag);
            params.put(JSONFunctions.JSON_CASE_TAG, experienceCase);
            params.put(JSONFunctions.JSON_EXPERIENCE, jsonExperienceList);
            params.put(JSONFunctions.USER_EXPERIENCE_USERS__ID, userDetails.get(MyContentProvider.USER_ID));

            pDialog = new MaterialDialog.Builder(this)
                    .title(R.string.p_dialog_wiz_title)
                    .content(R.string.p_dialog_content_wait)
                    .widgetColorRes(R.color.material_dialog_buttons)
                    .progress(true, 0)
                    .show();

            client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pDialog.hide();
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    System.out.println(response);
                    pDialog.hide();
                    pDialog.dismiss();

                    try {
                        //INSERT INTO SQLITE
                        JSONArray arr = new JSONArray(response);
                        System.out.println(arr.length());
                        //Insert into sqlite databse
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jsonExperience = (JSONObject) arr.get(i);
                            String success = jsonExperience.getString(JSONFunctions.JSON_SUCCESS);
                            String error = jsonExperience.getString(JSONFunctions.JSON_ERROR);
                            if(Integer.parseInt(success)==1) {
                                String jsonCase = jsonExperience.getString(JSONFunctions.JSON_CASE_TAG);

                                ContentValues values = new ContentValues();
                                values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                values.put(MyContentProvider.WORK_EXPERIENCE_ID, jsonExperience.getString(JSONFunctions.EXPERIENCE__ID));
                                values.put(MyContentProvider.WORK_EXPERIENCE_START_DATE, jsonExperience.getString(JSONFunctions.EXPERIENCE_START_DATE));
                                values.put(MyContentProvider.WORK_EXPERIENCE_END_DATE, jsonExperience.getString(JSONFunctions.EDUCATION_END_DATE));
                                values.put(MyContentProvider.WORK_EXPERIENCE_POSITION, jsonExperience.getString(JSONFunctions.EXPERIENCE_POSITION));
                                values.put(MyContentProvider.WORK_EXPERIENCE_EMPLOYER, jsonExperience.getString(JSONFunctions.EXPERIENCE_EMPLOYER));
                                values.put(MyContentProvider.WORK_EXPERIENCE_COUNTRY, jsonExperience.getString(JSONFunctions.EDUCATION_COUNTRY));
                                values.put(MyContentProvider.WORK_EXPERIENCE_CITY, jsonExperience.getString(JSONFunctions.EXPERIENCE_CITY));
                                values.put(MyContentProvider.WORK_EXPERIENCE_MAIN_ACTIVITIES, jsonExperience.getString(JSONFunctions.EXPERIENCE_MAIN_ACTIVITIES));

                                //INSERT INTO EXPERIENCE TABLE
                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE, values);

                                if(Integer.parseInt(jsonCase) == JSONFunctions.JSON_RESPONSE_ADD){
                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                    contentValues.put(MyContentProvider.USER_WORK_EXPERIENCE_ID, jsonExperience.getString(JSONFunctions.USER_EXPERIENCE_ID));
                                    contentValues.put(MyContentProvider.USER_WORK_EXPERIENCE_USER_FK, jsonExperience.getString(JSONFunctions.USER_EXPERIENCE_USERS__ID));
                                    contentValues.put(MyContentProvider.USER_WORK_EXPERIENCE_WORK_EXPERIENCE_FK, jsonExperience.getString(JSONFunctions.USER_EXPERIENCE_EXPERIENCE__ID));

                                    //INSERT INTO USER EXPERIENCE TABLE
                                    getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_WORK_EXPERIENCE, contentValues);

                                }
                                //SHOW EXPERIENCE FRAG
                                onBackPressed();
                            }else if(Integer.parseInt(error)==1){
                                showToast(getResources().getString(R.string.message_unexpected_message));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }
    }

    //SAVE EDUCATION
    //********************************************************************************************************************
    //********************************************************************************************************************
    @Override
    public void saveEducation(String educationCase, String jsonEducationList) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (jsonEducationList != null) {
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            pDialog = new MaterialDialog.Builder(this)
                    .title(R.string.p_dialog_wiz_title)
                    .content(R.string.p_dialog_content_wait)
                    .widgetColorRes(R.color.material_dialog_buttons)
                    .progress(true, 0)
                    .show();

            params.put(JSONFunctions.TAG, JSONFunctions.education_tag);
            params.put(JSONFunctions.JSON_CASE_TAG, educationCase);
            params.put(JSONFunctions.JSON_EDUCATION, jsonEducationList);
            params.put(JSONFunctions.USER_EDUCATION_USERS__ID, userDetails.get(MyContentProvider.USER_ID));

            client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pDialog.hide();
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    System.out.println(response);
                    pDialog.hide();
                    pDialog.dismiss();

                    try {
                        //INSERT INTO SQLITE
                        JSONArray arr = new JSONArray(response);
                        System.out.println(arr.length());
                        //Insert into sqlite databse
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jsonExperience = (JSONObject) arr.get(i);

                            String success = jsonExperience.getString(JSONFunctions.JSON_SUCCESS);
                            String error = jsonExperience.getString(JSONFunctions.JSON_ERROR);

                            if(Integer.parseInt(success)==1) {

                                String jsonCase = jsonExperience.getString(JSONFunctions.JSON_CASE_TAG);

                                ContentValues values = new ContentValues();
                                values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                values.put(MyContentProvider.EDUCATION_ID, jsonExperience.getString(JSONFunctions.EDUCATION__ID));
                                values.put(MyContentProvider.EDUCATION_START_DATE, jsonExperience.getString(JSONFunctions.EDUCATION_START_DATE));
                                values.put(MyContentProvider.EDUCATION_END_DATE, jsonExperience.getString(JSONFunctions.EDUCATION_END_DATE));
                                values.put(MyContentProvider.EDUCATION_QUALIFICATION_NAME, jsonExperience.getString(JSONFunctions.EDUCATION_QUALIFICATION));
                                values.put(MyContentProvider.EDUCATION_PROVIDER, jsonExperience.getString(JSONFunctions.EDUCATION_PROVIDER));
                                values.put(MyContentProvider.EDUCATION_COUNTRY, jsonExperience.getString(JSONFunctions.EDUCATION_COUNTRY));
                                values.put(MyContentProvider.EDUCATION_CITY, jsonExperience.getString(JSONFunctions.EDUCATION_CITY));
                                values.put(MyContentProvider.EDUCATION_MAIN_SUBJECTS, jsonExperience.getString(JSONFunctions.EDUCATION_MAIN_SUBJECTS));

                                //INSERT INTO EXPERIENCE TABLE
                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_EDUCATION, values);

                                if(Integer.parseInt(jsonCase) == JSONFunctions.JSON_RESPONSE_ADD){
                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                    contentValues.put(MyContentProvider.USER_EDUCATION_ID, jsonExperience.getString(JSONFunctions.USER_EDUCATION_ID));
                                    contentValues.put(MyContentProvider.USER_EDUCATION_USERS_FK, jsonExperience.getString(JSONFunctions.USER_EDUCATION_USERS__ID));
                                    contentValues.put(MyContentProvider.USER_EDUCATION_EDUCATION_FK, jsonExperience.getString(JSONFunctions.USER_EDUCATION_EDUCATION__ID));

                                    //INSERT INTO USER EXPERIENCE TABLE
                                    getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_EDUCATION, contentValues);

                                }
                                //SHOW EXPERIENCE FRAG
                                onBackPressed();
                                //fabMain.setLineMorphingState((fabMain.getLineMorphingState() + 1) % 2, true);
                            }else if(Integer.parseInt(error)==1){
                                showToast(getResources().getString(R.string.message_unexpected_message));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }
    }

    //SAVE LANGUAGE
    //********************************************************************************************************************
    //********************************************************************************************************************
    @Override
    public void insertLanguage(String languageCase, String languageJSONList, final String evaluationJSONList, final ActionButton fabSkillMain) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (languageJSONList != null){
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            params.put(JSONFunctions.TAG, JSONFunctions.language_tag);
            params.put(JSONFunctions.JSON_LANGUAGE, languageJSONList);
            params.put(JSONFunctions.JSON_CASE_TAG, languageCase);
            params.put(JSONFunctions.USER_LANGUAGE_USERS__ID, userDetails.get(MyContentProvider.USER_ID));

            pDialog = new MaterialDialog.Builder(this)
                    .title(R.string.p_dialog_wiz_title)
                    .content(R.string.p_dialog_content_wait)
                    .widgetColorRes(R.color.material_dialog_buttons)
                    .progress(true, 0)
                    .show();

            client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pDialog.hide();
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    System.out.println(response);
                    pDialog.hide();
                    pDialog.dismiss();

                    try {
                        JSONArray arr = new JSONArray(response);
                        System.out.println(arr.length());

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jsonLanguage = (JSONObject) arr.get(i);
                            String success = jsonLanguage.getString(JSONFunctions.JSON_SUCCESS);
                            String error = jsonLanguage.getString(JSONFunctions.JSON_ERROR);
                            if (Integer.parseInt(success) == 1) {
                                String jsonCase = jsonLanguage.getString(JSONFunctions.JSON_CASE_TAG);

                                ContentValues values = new ContentValues();

                                values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                values.put(MyContentProvider.LANGUAGE_ID, jsonLanguage.getString(JSONFunctions.LANGUAGE__ID));
                                values.put(MyContentProvider.LANGUAGE_NAME, jsonLanguage.getString(JSONFunctions.LANGUAGE_NAME));
                                values.put(MyContentProvider.LANGUAGE_TYPE_FK, jsonLanguage.getString(JSONFunctions.LANGUAGE_TYPE_FK));
                                //INSERT INTO LANGUAGE TABLE
                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_LANGUAGE, values);

                                String evaluationCase = null;

                                if(Integer.parseInt(jsonCase) == JSONFunctions.JSON_RESPONSE_ADD) {

                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                    contentValues.put(MyContentProvider.USER_LANG_ID, jsonLanguage.getString(JSONFunctions.USER_LANGUAGE_ID));
                                    contentValues.put(MyContentProvider.USER_LANG_USERS_ID_FK, jsonLanguage.getString(JSONFunctions.USER_LANGUAGE_USERS__ID));
                                    contentValues.put(MyContentProvider.USER_LANG_LANG_FK, jsonLanguage.getString(JSONFunctions.LANGUAGE__ID));
                                    //INSERT INTO LANGUAGE TABLE
                                    getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_LANGUAGE, contentValues);
                                }else if((Integer.parseInt(jsonCase) == JSONFunctions.JSON_RESPONSE_UPDATE)){
                                    //UPDATE METHODS
                                }
                                //CHECK IF IS IT MOTHER TONGUE
                                if(fabSkillMain != null){
                                    fabSkillMain.playShowAnimation();
                                    fabSkillMain.show();
                                }
                                if (evaluationJSONList != null) {
                                    insertEvaluationIntoMYSQL(
                                            jsonLanguage.getString(JSONFunctions.LANGUAGE__ID),
                                            evaluationJSONList);
                                }
                            }else{
                                showToast(getResources().getString(R.string.message_unexpected_message));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }
    }

    //SAVE EVALUATION
    //********************************************************************************************************************
    //********************************************************************************************************************
    private void insertEvaluationIntoMYSQL(String languageID, String evaluationJSONList) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(JSONFunctions.TAG, JSONFunctions.evaluation_tag);
        params.put(JSONFunctions.JSON_EVALUATION, evaluationJSONList);
        params.put(JSONFunctions.USER_LANGUAGE_LANGUAGE__ID, languageID);

        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.p_dialog_wiz_title)
                .content(R.string.p_dialog_content_wait)
                .widgetColorRes(R.color.material_dialog_buttons)
                .progress(true, 0)
                .show();

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                System.out.println(response);
                pDialog.hide();
                pDialog.dismiss();

                try {
                    JSONArray arr = new JSONArray(response);

                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonEvaluation = (JSONObject) arr.get(i);
                        String success = jsonEvaluation.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonEvaluation.getString(JSONFunctions.JSON_ERROR);
                        if (Integer.parseInt(success) == 1) {
                            //INSERT INTO EVALUATION
                            ContentValues values = new ContentValues();
                            values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                            values.put(MyContentProvider.LANGUAGE_EVALUATION_ID, jsonEvaluation.getString(JSONFunctions.LANGUAGE_EVALUATION_ID));
                            values.put(MyContentProvider.LANGUAGE_EVALUATION_LANG_FK, jsonEvaluation.getString(JSONFunctions.LANGUAGE_EVALUATION_LANGUAGE__ID));
                            values.put(MyContentProvider.LANGUAGE_EVALUATION_ASSESSMENT_FK, jsonEvaluation.getString(JSONFunctions.LANGUAGE_EVALUATION_ASSESSMENT__ID));

                            getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_EVALUATION, values);

                            //GO BACK
                            onBackPressed();

                        } else {
                            showToast(getResources().getString(R.string.message_unexpected_message));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    //SAVE SKILLS
    //********************************************************************************************************************
    //********************************************************************************************************************
    @Override
    public void saveSkills(String skillsCase, String skillType, String skillJSONList) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        sqlController.open();
        userDetails = sqlController.getUserDetails();

        params.put(JSONFunctions.TAG, JSONFunctions.skills_tag);
        params.put(JSONFunctions.SKILLS_TYPE, skillType);
        params.put(JSONFunctions.JSON_CASE_TAG, skillsCase);
        params.put(JSONFunctions.JSON_SKILLS, skillJSONList);


        params.put(JSONFunctions.SKILL_USERS_UID, userDetails.get(MyContentProvider.USER_ID));

        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.p_dialog_wiz_title)
                .content(R.string.p_dialog_content_wait)
                .widgetColorRes(R.color.material_dialog_buttons)
                .progress(true, 0)
                .show();

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                System.out.println(response);
                pDialog.hide();
                pDialog.dismiss();

                try {
                    JSONArray arr = new JSONArray(response);

                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonSkills = (JSONObject) arr.get(i);

                        String success = jsonSkills.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonSkills.getString(JSONFunctions.JSON_ERROR);

                        if (Integer.parseInt(success) == 1) {

                            String skillType = jsonSkills.getString(JSONFunctions.SKILLS_TYPE);

                            ContentValues values = new ContentValues();
                            //values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                            values.put(MyContentProvider.SKILL_ID, jsonSkills.getString(JSONFunctions.SKILL__ID));

                            switch (skillType) {
                                case JSONFunctions.COMMUNICATION_TYPE:
                                    values.put(MyContentProvider.SKILL_COMMUNICATION, jsonSkills.getString(JSONFunctions.SKILL_COMMUNICATION));
                                    break;

                                case JSONFunctions.MANAGAMENT_TYPE:
                                    values.put(MyContentProvider.SKILL_MANAGMENT, jsonSkills.getString(JSONFunctions.SKILL_MANAGMENT));
                                    break;

                                case JSONFunctions.JOB_RELATED_TYPE:
                                    values.put(MyContentProvider.SKILL_JOB_RELATED, jsonSkills.getString(JSONFunctions.SKILL_JOB_RELATED));
                                    break;

                                case JSONFunctions.DIG_COMPETENCE_TYPE:
                                    values.put(MyContentProvider.SKILL_IT, jsonSkills.getString(JSONFunctions.SKILL_IT));
                                    break;
                            }

                            values.put(MyContentProvider.SKILL_USERS_FK, jsonSkills.getString(JSONFunctions.SKILL_USERS_UID));
                            //INSERT INTO SKILLS TABLE
                            Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI_SKILLS, new String[]{MyContentProvider.SKILL_ID},
                                    null, null, null);
                            if (cursor.moveToFirst()) {
                                getApplicationContext().getContentResolver().update(MyContentProvider.CONTENT_URI_SKILLS, values,
                                        MyContentProvider.SKILL_ID + " =?", new String[]{cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_ID))});
                            } else {
                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_SKILLS, values);
                            }
                            cursor.close();


                            //GO BACK
                            onBackPressed();
                        } else {
                            showToast(getResources().getString(R.string.message_unexpected_message));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //DELETE DIALOG
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void showDeleteDialog(final long itemId, final String type, final String skillsType) {
        new MaterialDialog.Builder(this)
                .title(R.string.dialogTitleConfirmationDelete)
                .content(R.string.dialogContentConfirmationDelete)
                .theme(Theme.LIGHT)
                .positiveText(R.string.button_agree)
                .negativeText(R.string.button_disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (type) {
                            //EXPERIENCE
                            case JSONFunctions.experience_delete_tag:
                                deleteFromMySQL(JSONFunctions.experience_delete_tag, null, itemId);
                                break;

                            //EDUCATION
                            case JSONFunctions.education_delete_tag:
                                deleteFromMySQL(JSONFunctions.education_delete_tag, null, itemId);
                                break;

                            //LANGUAGE DELETE
                            case JSONFunctions.language_delete_tag:
                                deleteFromMySQL(JSONFunctions.language_delete_tag, null, itemId);
                                break;

                            //SKILLS DELETE
                            case JSONFunctions.skills_delete_tag:
                                deleteFromMySQL(JSONFunctions.skills_delete_tag, skillsType, itemId);
                                break;
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        fabMain.playShowAnimation();
                        fabMain.show();
                    }
                })
                .show();
    }

    //EXPORT CV
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void exportCV() {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        sqlController.open();
        userDetails = sqlController.getUserDetails();
        HashMap<String, String> profileDetails = sqlController.getProfileDetails();

        params.put(JSONFunctions.TAG, JSONFunctions.export_cv_tag);

        params.put(JSONFunctions.USER_UID, userDetails.get(MyContentProvider.USER_ID));
        params.put(JSONFunctions.USER_EMAIL,  userDetails.get(MyContentProvider.USER_EMAIL));
        params.put(JSONFunctions.PROFILE_NAME, profileDetails.get(MyContentProvider.PROFILE_NAME));
        params.put(JSONFunctions.PROFILE_SURNAME, profileDetails.get(MyContentProvider.PROFILE_SURNAME));


        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.p_dialog_wiz_title)
                .content(R.string.p_dialog_content_wait)
                .widgetColorRes(R.color.material_dialog_buttons)
                .progress(true, 0)
                .show();

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                System.out.println(response);
                pDialog.hide();
                pDialog.dismiss();

                try {
                    JSONArray arr = new JSONArray(response);

                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonEvaluation = (JSONObject) arr.get(i);

                        String success = jsonEvaluation.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonEvaluation.getString(JSONFunctions.JSON_ERROR);


                        if (Integer.parseInt(success) == 1) {
                            showSuccessDialogEport();
                        } else {
                            showToast("Error");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void showSuccessDialogEport() {
        new MaterialDialog.Builder(this)
                .content(R.string.dialog_message_successfully_export)
                .widgetColorRes(R.color.material_dialog_buttons)
                .positiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    //DELETE FROM MYSQL
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    public void deleteFromMySQL(String caseTag, String skillsType, long itemID) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.p_dialog_wiz_title)
                .content(R.string.p_dialog_content_wait)
                .widgetColorRes(R.color.material_dialog_buttons)
                .progress(true, 0)
                .show();

        params.put(JSONFunctions.TAG, JSONFunctions.JSON_DELETE_TAG);
        params.put(JSONFunctions.JSON_CASE_TAG, caseTag);
        params.put(JSONFunctions.SKILLS_TYPE, skillsType);
        params.put(JSONFunctions.JSON_DELETE_ITEM_ID, itemID);

        Log.d("delete", "case tag: " + caseTag + " skills_type: " + skillsType);

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                System.out.println(response);
                pDialog.hide();
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonDelete = (JSONObject) arr.get(i);

                        String success = jsonDelete.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonDelete.getString(JSONFunctions.JSON_ERROR);

                        if (Integer.parseInt(success) == 1) {
                            String deleteCase = jsonDelete.getString(JSONFunctions.JSON_CASE_TAG);
                            String itemID = jsonDelete.getString(JSONFunctions.JSON_DELETE_ITEM_ID);
                            switch (deleteCase) {
                                //DELETE FROM EXPEREINCE
                                case JSONFunctions.experience_delete_tag:

                                    Uri experienceURI = Uri.parse(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE + "/" + itemID);
                                    getContentResolver().delete(experienceURI, null, null);

                                    break;

                                //DELETE FROM EDUCATION
                                case JSONFunctions.education_delete_tag:

                                    Uri educationURI = Uri.parse(MyContentProvider.CONTENT_URI_EDUCATION + "/" + itemID);
                                    getContentResolver().delete(educationURI, null, null);

                                    break;

                                //DELETE FROM LANGUAGE
                                case JSONFunctions.language_delete_tag:
                                    Uri languageURI = Uri.parse(MyContentProvider.CONTENT_URI_LANGUAGE + "/" + itemID);
                                    getContentResolver().delete(languageURI, null, null);
                                    break;

                                //DELETE FROM SKILLS
                                case JSONFunctions.skills_delete_tag:

                                    String skillsType = jsonDelete.getString(JSONFunctions.SKILLS_TYPE);
                                    Uri skillsUri = Uri.parse(MyContentProvider.CONTENT_URI_SKILLS + "/" + itemID);
                                    String nullDATA = null;
                                    ContentValues values = new ContentValues();
                                    switch (skillsType) {
                                        //COMMUNICATION TYPE
                                        case JSONFunctions.COMMUNICATION_TYPE:
                                            values.put(MyContentProvider.SKILL_COMMUNICATION, nullDATA);
                                            break;

                                        //MANAGAMENT TYPE
                                        case JSONFunctions.MANAGAMENT_TYPE:
                                            values.put(MyContentProvider.SKILL_MANAGMENT, nullDATA);
                                            break;

                                        //JOB RELATED TYPE
                                        case JSONFunctions.JOB_RELATED_TYPE:
                                            values.put(MyContentProvider.SKILL_JOB_RELATED, nullDATA);
                                            break;

                                        //DIGITAL COMPETENCE TYPE
                                        case JSONFunctions.DIG_COMPETENCE_TYPE:
                                            values.put(MyContentProvider.SKILL_IT, nullDATA);
                                            break;
                                    }
                                    getContentResolver().update(skillsUri, values, null, null);
                            }

                            ActionButton fabMain = (ActionButton) findViewById(R.id.fabMain);
                            fabMain.playShowAnimation();
                            fabMain.show();
                        } else if (Integer.parseInt(error) == 1) {
                            showToast(getResources().getString(R.string.message_unexpected_message));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
    //Show Toast message
    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

}
