package reachingimmortality.com.curriculum.ui_wizard;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import reachingimmortality.com.curriculum.MainActivity;
import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.dialogs.MotherTongueDialog;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;

public class WizardActivity extends AppCompatActivity implements WizardCallback {

    //UI ELEMENTS - CALL TO SERVER
    private MaterialDialog pDialog;

    //CONTROLLERS
    private SQLController sqlController;

    //HASHMAPS
    private HashMap<String, String> userDetails;

    //TOAST
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);

        //INIT CONTROLLERS
        sqlController = new SQLController(this);
        SessionManager sessionManager = new SessionManager(this);

        //REPLACE ROOT FRAME WITH SELECTED FRAGMENT
        if(sessionManager.isProfileEdited()){
            showApplicationFragment();
        }else if(sessionManager.isApplicationEdited()){
            showExperienceFragment();
        }else if(sessionManager.isExperienceEdited()){
            showEducation();
        }else if(sessionManager.isEducationEdited()){
            showMotherLanguage();
        }else if(sessionManager.isMotherLanguageEdited()){
            showOtherLanguage();
        }else if(sessionManager.isOtherLanguageEdited()) {
            showSkills();
        }else if(sessionManager.isSkillsEdited()) {
            showExport();
        }else{
            getFragmentManager().beginTransaction()
                    .replace(R.id.root_wizard, new ProfileFragment())
                    .commit();
        }

    }

    //GO TO APPLICATION / INSERT PROFILE
    //*********************************************************************************************************************
    //*********************************************************************************************************************
    @Override
    public void insertProfileIntoMySQL(String jsonProfileList) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (jsonProfileList != null) {
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            params.put(JSONFunctions.TAG, JSONFunctions.profile_tag);
            params.put(JSONFunctions.JSON_PROFILE, jsonProfileList);
            params.put(JSONFunctions.PROFILE_USERS_UID, userDetails.get(MyContentProvider.USER_ID));

            Log.d("profileList",jsonProfileList);

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

                                //SHOW APPLICATION FRAG
                                showApplicationFragment();
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

    //GO TO EXPERIENCE // INSERT APPLICATION
    //*********************************************************************************************************************
    //*********************************************************************************************************************
    @Override
    public void insertApplicationIntoMySQL(String applicationCase, String jsonApplicationList) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (jsonApplicationList != null) {
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            pDialog = new MaterialDialog.Builder(this)
                    .title(R.string.p_dialog_wiz_title)
                    .content(R.string.p_dialog_content_wait)
                    .widgetColorRes(R.color.material_dialog_buttons)
                    .progress(true, 0)
                    .show();

            params.put(JSONFunctions.TAG, JSONFunctions.application_tag);
            params.put(JSONFunctions.JSON_CASE_TAG, applicationCase);
            params.put(JSONFunctions.USER_APPLICATION_USERS__ID, userDetails.get(MyContentProvider.USER_ID));
            params.put(JSONFunctions.JSON_APPLICATION, jsonApplicationList);

            Log.d("JSON_CASE_TAG", applicationCase);
            Log.d("USERS__ID", userDetails.get(MyContentProvider.USER_ID));
            Log.d("JSON_APPLICATION",jsonApplicationList);

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
                            if (Integer.parseInt(success) == 1) {

                                String applicationCase = jsonApplication.getString(JSONFunctions.JSON_CASE_TAG);

                                ContentValues values = new ContentValues();

                                values.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                values.put(MyContentProvider.APPLICATION_ID, jsonApplication.getString(JSONFunctions.APPLICATION_ID));
                                values.put(MyContentProvider.APPLICATION_DESCRIPTION, jsonApplication.getString(JSONFunctions.APPLICATION_DESCRIPTION));
                                values.put(MyContentProvider.APPLICATION_TYPE_ID_FK, jsonApplication.getString(JSONFunctions.APPLICATION_TYPE__ID));

                                //INSERT INTO APPLICATION TABLE
                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_APPLICATION, values);

                                if (Integer.parseInt(applicationCase) == JSONFunctions.JSON_RESPONSE_ADD) {
                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put(MyContentProvider.SQL_INSERT_OR_REPLACE, true);
                                    contentValues.put(MyContentProvider.USER_APPLICATION_ID, jsonApplication.getString(JSONFunctions.USER_APPLICATION_ID));
                                    contentValues.put(MyContentProvider.USER_APPLICATION_USER_FK, jsonApplication.getString(JSONFunctions.USER_APPLICATION_USERS__ID));
                                    contentValues.put(MyContentProvider.USER_APPLICATION_APPLICATION_FK, jsonApplication.getString(JSONFunctions.USER_APPLICATION_APPLICATION__ID));


                                    //INSERT INTO USER APPLICATION TABLE
                                    getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_APPLICATION, contentValues);
                                }
                                //MANAGE SHARED PREFERENCES
                                showExperienceFragment();
                            } else if (Integer.parseInt(error) == 1) {
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

    //INSERT EXPERIENCE / MYSQL
    //*******************************************************************************************************
    //*******************************************************************************************************
    @Override
    public void insertExperienceIntoMySQL(final String expeirenceCase, String jsonExperienceList) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (jsonExperienceList != null) {
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            params.put(JSONFunctions.TAG, JSONFunctions.experience_tag);
            params.put(JSONFunctions.JSON_CASE_TAG, expeirenceCase);
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

    //INSERT EDUCATION / MYSQL
    //*******************************************************************************************************
    //*******************************************************************************************************
    @Override
    public void insertEducationIntoMySQL(String educationCase, String jsonEducationList) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        Log.d("", "jsonList " + jsonEducationList);
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

    //INSERTING LANGUAGE / MYSQL
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    @Override
    public void insertLanguageIntoMYSQL(String languageCase, String jsonLanguageList, final String jsonEvaluationList) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (jsonLanguageList != null){
            //GET CURRENT USER DATA
            sqlController.open();
            userDetails = sqlController.getUserDetails();

            params.put(JSONFunctions.TAG, JSONFunctions.language_tag);
            params.put(JSONFunctions.JSON_LANGUAGE, jsonLanguageList);
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
                                if (jsonEvaluationList != null) {
                                    insertEvaluationIntoMYSQL(
                                            jsonLanguage.getString(JSONFunctions.LANGUAGE__ID),
                                            jsonEvaluationList);
                                } else {
                                    showMotherLanguage();
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



    //INSERTING LANGUAGE EVALUATION / MYSQL
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    private void insertEvaluationIntoMYSQL(String languageID, String jsonEvaluationList) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(JSONFunctions.TAG, JSONFunctions.evaluation_tag);
        params.put(JSONFunctions.JSON_EVALUATION, jsonEvaluationList);
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

                            //SHOW LANGUAGE FRAGMENT
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

    //INSERTING SKILLS / MYSQL
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    @Override
    public void insertSkillsIntoMySQL(String skillsCase, String skillType, String jsonSkillsList) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        sqlController.open();
        userDetails = sqlController.getUserDetails();

        params.put(JSONFunctions.TAG, JSONFunctions.skills_tag);
        params.put(JSONFunctions.SKILLS_TYPE, skillType);
        params.put(JSONFunctions.JSON_CASE_TAG, skillsCase);
        params.put(JSONFunctions.JSON_SKILLS, jsonSkillsList);


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


                            //SHOW SKILLS FRAGMENT
                            showSkills();
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

    //REPLACE FRAGMENTS
    //*********************************************************************************************************************
    //*********************************************************************************************************************
    //PROFILE
    private void showProfile() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.root_wizard, new ProfileFragment());
        transaction.commit();
    }

    //APPLICATION
    private void showApplicationFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.root_wizard, new ApplicationFragment());
        transaction.commit();
    }

    //EXPERIENCE
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    private void showExperienceFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.root_wizard, new ExperienceFragment());
        transaction.commit();
    }

    //EXPERIENCE ADD / EDIT
    @Override
    public void showExperienceAddEditFragment(String type, Uri uri) {

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        switch (type) {
            case JSONFunctions.experience_add_tag:
                ExperienceEditFragment fragment = new ExperienceEditFragment();
                transaction.replace(R.id.root_wizard, fragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
                break;
            case JSONFunctions.experience_update_tag:
                ExperienceEditFragment fragmentUpdate = ExperienceEditFragment.newInstance(uri);
                transaction.replace(R.id.root_wizard, fragmentUpdate);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
                break;
        }
    }

    //EDUCATION
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    public void showEducation() {
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_wizard, new EducationFragment());
        transaction.commit();
    }

    //EDUCATION ADD / EDIT
    @Override
    public void showEducationAddEditFragment(String type, Uri uri) {

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        switch (type) {
            case JSONFunctions.education_add_tag:
                EducationEditFragment fragment = new EducationEditFragment();
                transaction.replace(R.id.root_wizard, fragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
                break;
            case JSONFunctions.education_update_tag:
                EducationEditFragment fragmentUpdate = EducationEditFragment.newInstance(uri);
                transaction.replace(R.id.root_wizard, fragmentUpdate);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
                break;
        }
    }

    //LANGUAGE
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    private void showMotherLanguage() {
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_wizard, new LanguageMotherFragment());
        transaction.commit();
    }

    private void showOtherLanguage() {
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_wizard, new LanguageOtherFragment());
        transaction.commit();
    }
    //SKILLS
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    private void showSkills() {
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_wizard, new SkillsFragment());
        transaction.commit();
    }
    //EDUCATION
    //**********************************************************************************************************************
    //**********************************************************************************************************************
    public void showExport() {
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_wizard, new ExportFragment());
        transaction.commit();
    }


    @Override
    public void callMotherTongueAddDialog() {
        //int accentColor = ThemeSingleton.get().widgetColor;
        //if (accentColor == 0)
        int accentColor = getResources().getColor(R.color.colorAccent);

        MotherTongueDialog.newInstance(false, accentColor)
                .show(getFragmentManager(), "changelog");
    }

    @Override
    public void callMotherTongueEditDialog(long itemID) {
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_LANGUAGE + "/" + itemID);
        //int accentColor = ThemeSingleton.get().widgetColor;
        //if (accentColor == 0)
        int accentColor = getResources().getColor(R.color.colorAccent);

        MotherTongueDialog.newInstance(false, accentColor, uri)
                .show(getFragmentManager(), "changelog");
    }


    @Override
    public void showEvaluationAddEditFragment(String type, Uri uri) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        switch (type) {
            case JSONFunctions.evaluation_add_tag:
                OtherLanguageEvaluationFrag fragment = new OtherLanguageEvaluationFrag();
                transaction.replace(R.id.root_wizard, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case JSONFunctions.evaluation_update_tag:
                OtherLanguageEvaluationFrag fragmentUpdate = OtherLanguageEvaluationFrag.newInstance(uri);
                transaction.replace(R.id.root_wizard, fragmentUpdate);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public void showSkillsAddEditFragment(String skillsType, Uri uri) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
                SkillsEditFragment fragment = SkillsEditFragment.newInstance(skillsType, uri);
                transaction.replace(R.id.root_wizard, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
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
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
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

                    @Override
                    public void onNegative(MaterialDialog dialog) {

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
        params.put(JSONFunctions.USER_EMAIL, userDetails.get(MyContentProvider.USER_EMAIL));
        params.put(JSONFunctions.PROFILE_NAME, profileDetails.get(MyContentProvider.PROFILE_NAME));
        params.put(JSONFunctions.PROFILE_SURNAME, profileDetails.get(MyContentProvider.PROFILE_SURNAME));

        Log.d("name", profileDetails.get(MyContentProvider.PROFILE_NAME));
        Log.d("surname", profileDetails.get(MyContentProvider.PROFILE_SURNAME));

        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_export)
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
                pDialog.dismiss();

                try {
                    JSONArray arr = new JSONArray(response);

                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonEvaluation = (JSONObject) arr.get(i);

                        String success = jsonEvaluation.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonEvaluation.getString(JSONFunctions.JSON_ERROR);


                        if (Integer.parseInt(success) == 1) {
                            showDialogProceed();
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
                            switch (deleteCase){
                                //DELETE FROM EXPEREINCE
                                case JSONFunctions.experience_delete_tag:

                                    Uri experienceURI = Uri.parse(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE + "/" +itemID);
                                    getContentResolver().delete(experienceURI, null, null);

                                    //SHOW EDUCATION FRAGMENT
                                    showExperienceFragment();
                                    break;

                                //DELETE FROM EXPEREINCE
                                case JSONFunctions.education_delete_tag:

                                    Uri educationURI = Uri.parse(MyContentProvider.CONTENT_URI_EDUCATION + "/" +itemID);
                                    getContentResolver().delete(educationURI, null, null);

                                    //SHOW EDUCATION FRAGMENT
                                    showEducation();
                                    break;

                                //DELETE FROM LANGUAGE
                                case JSONFunctions.language_delete_tag:
                                    Uri languageURI = Uri.parse(MyContentProvider.CONTENT_URI_LANGUAGE + "/" +itemID);
                                    getContentResolver().delete(languageURI, null, null);
                                    break;

                                //DELETE FROM SKILLS
                                case JSONFunctions.skills_delete_tag:

                                    String skillsType = jsonDelete.getString(JSONFunctions.SKILLS_TYPE);
                                    Uri skillsUri = Uri.parse(MyContentProvider.CONTENT_URI_SKILLS + "/" + itemID);
                                    String nullDATA = null;
                                    ContentValues values = new ContentValues();
                                    switch (skillsType){
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
                                    getContentResolver().update(skillsUri,values,null,null);

                            }


                        }else if(Integer.parseInt(error) == 1){
                            showToast(getResources().getString(R.string.message_unexpected_message));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    //CALLBACKS GO TO
    //*****************************************************************************************************************************

    @Override
    public void goToProfile() {
        showProfile();
    }

    @Override
    public void goToApplication() {
        showApplicationFragment();
    }

    @Override
    public void goToExperience() {
        showExperienceFragment();
    }

    @Override
    public void goToEducation() {
        showEducation();
    }

    @Override
    public void goToMotherLanguage() {
        showMotherLanguage();
    }

    @Override
    public void goToOtherLanguage() {
        showOtherLanguage();
    }

    @Override
    public void goToSkills() {
        showSkills();
    }

    @Override
    public void goToExport() {
        showExport();
    }

    //SHOW DIALOG PROCEED
    //********************************************************************************************************************
    private void showDialogProceed(){
        new MaterialDialog.Builder(this)
                .content(R.string.dialog_message_successfully_export)
                .widgetColorRes(R.color.material_dialog_buttons)
                .positiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        //SET SHARED PREFERENCES
                        sessionManager.setWizard(true);
                        //END CURRENT ACTIVITY
                        finish();
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                })
                .show();
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

    //POP BACKSTACK ON BASK PRESSED
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

