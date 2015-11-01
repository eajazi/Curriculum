package reachingimmortality.com.curriculum.ui_login;

import android.Manifest;
import android.app.Application;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import reachingimmortality.com.curriculum.MainActivity;
import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.LoginCallback;
import reachingimmortality.com.curriculum.ui_wizard.WizardActivity;

public class LoginActivity extends FragmentActivity implements LoginCallback, FragmentManager.OnBackStackChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //TAGS and constant variables
    private static final String TAG_LOGIN = "loginFrag";
    private static final String TAG_REGISTER = "registerFrag";
    private static final String TAG = "loginActivity" ;
    private static final String REGULAR_LOGIN ="login";
    private static final String GOOGLE_LOGIN="google_login";

    //CONTROLLERS
    private SQLController sqlController;
    private SessionManager sessionManager;
    private MaterialDialog pDialog;
    private JSONFunctions jsonFunctions;

    private FragmentManager fragmentManager;
    private Application userApplicationData;

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;

    /* RequestCode for resolutions to get GET_ACCOUNTS permission on M */
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    // Client for accessing Google APIs
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //INIT CONTROLLERS
        sessionManager = new SessionManager(getApplicationContext());
        sqlController = new SQLController(getApplicationContext());
        jsonFunctions = new JSONFunctions();
        fragmentManager = getFragmentManager();

        //SET BACKGROUND
        ImageView imgBground = (ImageView) findViewById(R.id.img_bground_login);
        Picasso.with(getApplicationContext()).load(R.mipmap.bground_login).
                centerCrop().fit().into(imgBground);

        //SHOW LOGIN FRAGMENT
        LoginFrag loginFrag = new LoginFrag();
        fragmentManager.beginTransaction().
                replace(R.id.root_login_frame, loginFrag, TAG_LOGIN)
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .commit();

        getFragmentManager().addOnBackStackChangedListener(this);

        // [START create_google_api_client]
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        // [END create_google_api_client]

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Clear the default account so that GoogleApiClient will not automatically
        // connect in the future.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    // [START on_save_instance_state]
    //****************************************************************************************************************
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    // [START on_activity_result]
    //****************************************************************************************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    // [START onRequestPermissionsResult]
    //****************************************************************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:" + requestCode);
        if (requestCode == RC_PERM_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tryGoogleLogin();
            } else {
                Log.d(TAG, "GET_ACCOUNTS Permission Denied.");
            }
        }
    }

    //Callback method login for user login
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void login(String email,String password) {
        tryLogin(email, password, REGULAR_LOGIN);
    }

    //Callback method registern for user register
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void register(String email,String uname,String password) {
        registerUser(email, uname, password);
    }

    //Callback method for replacing login with register screen/fragment
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void goToRegister() {
        //Show register fragment
        getFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.root_login_frame, new RegisterFrag(), TAG_REGISTER)
                .addToBackStack("register")
                .commit();

    }

    //Callback method for showing login screen/fragment
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void goToLogin() {
        //Show login fragment
        getFragmentManager().popBackStack();
    }

    //Connecting to server / login URL
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    private void tryLogin(String email,String password, String CASE_LOGIN) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.p_dialog_login_title)
                .content(R.string.p_dialog_content_wait)
                .theme(Theme.LIGHT)
                .widgetColorRes(R.color.material_dialog_buttons)
                .progress(true, 0)
                .show();

        //Params
        switch (CASE_LOGIN){
            case REGULAR_LOGIN:

                params.put(JSONFunctions.TAG, JSONFunctions.login_tag);
                params.put(JSONFunctions.USER_EMAIL,email);
                params.put(JSONFunctions.USER_PASSWORD, password);

                break;

            case GOOGLE_LOGIN:

                params.put(JSONFunctions.TAG, JSONFunctions.google_login_tag);
                params.put(JSONFunctions.USER_EMAIL, email);

                break;
        }


        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                System.out.println(responseString);
                try {

                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonUser = (JSONObject) arr.get(i);

                        String success = jsonUser.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonUser.getString(JSONFunctions.JSON_ERROR);

                        if (Integer.parseInt(success) == 1) {
                            //DELETE TEMP DATA FROM DATABASE
                            jsonFunctions.clearTempDataSQLite(getApplicationContext());
                            //INSERTING NEW DATA
                            ContentValues values = new ContentValues();

                            values.put(MyContentProvider.USER_ID, jsonUser.getString(JSONFunctions.USER_UID));
                            values.put(MyContentProvider.USER_UID, jsonUser.getString(JSONFunctions.USER_UNIQUE_ID));
                            values.put(MyContentProvider.USER_EMAIL, jsonUser.getString(JSONFunctions.USER_EMAIL));
                            values.put(MyContentProvider.USER_UNAME, jsonUser.getString(JSONFunctions.USER_USERNAME));
                            values.put(MyContentProvider.USER_CREATED_AT, jsonUser.getString(JSONFunctions.USER_CREATED_AT));

                            getContentResolver().insert(MyContentProvider.CONTENT_URI_USER, values);

                            //GET USER PROFILE DATA
                            String isProfile = jsonUser.getString(JSONFunctions.IS_USER_PRO);
                            if (Boolean.parseBoolean(isProfile)) {

                                ContentValues profileValues = new ContentValues();

                                profileValues.put(MyContentProvider.PROFILE_USERS_PK_FK, jsonUser.getString(JSONFunctions.PROFILE_USERS_UID));
                                profileValues.put(MyContentProvider.PROFILE_NAME, jsonUser.getString(JSONFunctions.PROFILE_NAME));
                                profileValues.put(MyContentProvider.PROFILE_SURNAME, jsonUser.getString(JSONFunctions.PROFILE_SURNAME));
                                profileValues.put(MyContentProvider.PROFILE_EMAIL, jsonUser.getString(JSONFunctions.PROFILE_EMAIL));
                                profileValues.put(MyContentProvider.PROFILE_TEL_NUMBER, jsonUser.getString(JSONFunctions.PROFILE_TEL_NUMBER));
                                profileValues.put(MyContentProvider.PROFILE_STREET_ADRESS, jsonUser.getString(JSONFunctions.PROFILE_STREET_ADRESS));
                                profileValues.put(MyContentProvider.PROFILE_COUNTRY, jsonUser.getString(JSONFunctions.PROFILE_COUNTRY));
                                profileValues.put(MyContentProvider.PROFILE_CITY, jsonUser.getString(JSONFunctions.PROFILE_CITY));
                                profileValues.put(MyContentProvider.PROFILE_POSTAL_CODE, jsonUser.getString(JSONFunctions.PROFILE_POSTAL_CODE));
                                profileValues.put(MyContentProvider.PROFILE_WEB_BLOG, jsonUser.getString(JSONFunctions.PROFILE_WEB_BLOG));

                                //INSERT INTO PROFILE TABLE
                                getContentResolver().insert(MyContentProvider.CONTENT_URI_PROFILE, profileValues);
                            }
                            //GET APPLICATION DATA
                            getUserApplicationData(jsonUser.getString(JSONFunctions.USER_UID));
                        } else if (Integer.parseInt(error) == 1) {
                            pDialog.hide();
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_incorrect_email_or_password), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //REGISTRATION
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    private void registerUser(String email,String uname,String password) {

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.p_dialog_login_title)
                .content(R.string.p_dialog_content_wait)
                .widgetColorRes(R.color.material_dialog_buttons)
                .progress(true, 0)
                .show();

        //Params
        params.put(JSONFunctions.TAG, JSONFunctions.register_tag);
        params.put(JSONFunctions.USER_EMAIL,email);
        params.put(JSONFunctions.USER_USERNAME,uname);
        params.put(JSONFunctions.USER_PASSWORD, password);

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                System.out.println(responseString);
                pDialog.hide();
                pDialog.dismiss();
                try {
                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonUser = (JSONObject) arr.get(i);
                        String success = jsonUser.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonUser.getString(JSONFunctions.JSON_ERROR);
                        if (Integer.parseInt(success) == 1) {
                            //clear temp data from SQLite database
                            jsonFunctions.clearTempDataSQLite(getApplicationContext());
                            //Insert temp data into SQLite database
                            ContentValues values = new ContentValues();

                            values.put(MyContentProvider.USER_ID, jsonUser.getString(JSONFunctions.USER_UID));
                            values.put(MyContentProvider.USER_UID, jsonUser.getString(JSONFunctions.USER_UNIQUE_ID));
                            values.put(MyContentProvider.USER_EMAIL, jsonUser.getString(JSONFunctions.USER_EMAIL));
                            values.put(MyContentProvider.USER_UNAME, jsonUser.getString(JSONFunctions.USER_USERNAME));
                            values.put(MyContentProvider.USER_CREATED_AT, jsonUser.getString(JSONFunctions.USER_CREATED_AT));

                            getContentResolver().insert(MyContentProvider.CONTENT_URI_USER, values);

                            //Go TO WIZARD
                            showGoToWizardDialog();

                        } else if (Integer.parseInt(error) == 2) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_user_already_exists), Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(error) == 3) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_invalid_email), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onBackStackChanged() {
        int count = getFragmentManager().getBackStackEntryCount();
        for(int i=count-1; i>=0; i--){
            FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(i);
            Log.d("Fragment Manager","transaction"+entry.getName());
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentManager fm = getFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }
    //GET DATA FROM SERVER DATABASE
    //***********************************************************************************************************************************
    //***********************************************************************************************************************************

    //GET USER APP
    //***********************************************************************************************************************************
    public void getUserApplicationData(final String userUID) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //Params
        params.put(JSONFunctions.TAG, JSONFunctions.get_user_application_tag);
        params.put(JSONFunctions.USER_UID, userUID);

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                System.out.println(responseString);
                try {

                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonUserApp = (JSONObject) arr.get(i);

                        String isApp = jsonUserApp.getString(JSONFunctions.IS_USER_APP);
                        String error = jsonUserApp.getString(JSONFunctions.JSON_ERROR);

                        if (Boolean.parseBoolean(isApp)) {
                            //INSERTING APPLICATION DATA
                            ContentValues values = new ContentValues();

                            values.put(MyContentProvider.APPLICATION_ID, jsonUserApp.getString(JSONFunctions.APPLICATION_ID));
                            values.put(MyContentProvider.APPLICATION_DESCRIPTION, jsonUserApp.getString(JSONFunctions.APPLICATION_DESCRIPTION));
                            values.put(MyContentProvider.APPLICATION_TYPE_ID_FK, jsonUserApp.getString(JSONFunctions.APPLICATION_TYPE__ID));

                            getContentResolver().insert(MyContentProvider.CONTENT_URI_APPLICATION, values);

                            //INSERTING USER APPLICATION DATA
                            ContentValues userAppValues = new ContentValues();

                            userAppValues.put(MyContentProvider.APPLICATION_ID, jsonUserApp.getString(JSONFunctions.USER_APPLICATION_ID));
                            userAppValues.put(MyContentProvider.USER_APPLICATION_APPLICATION_FK, jsonUserApp.getString(JSONFunctions.APPLICATION_ID));
                            userAppValues.put(MyContentProvider.USER_APPLICATION_USER_FK, userUID);

                            getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_APPLICATION, userAppValues);

                        } else if (Integer.parseInt(error) == 3) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_unexpected_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                    //GET USER WORK EXPERIENCE DATA
                    getUserWorkExpData(userUID);
                    Log.d("userID", userUID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //GET USER WORK EXPERIENCE
    //***********************************************************************************************************************************
    private void getUserWorkExpData(final String userUID) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //Params
        params.put(JSONFunctions.TAG, JSONFunctions.get_user_experience_tag);
        params.put(JSONFunctions.USER_UID, userUID);

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                System.out.println(responseString);
                try {

                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {
                        Log.d("", "experience insert");
                        JSONObject jsonUserExp = (JSONObject) arr.get(i);

                        String sucess = jsonUserExp.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonUserExp.getString(JSONFunctions.JSON_ERROR);

                        if (Integer.parseInt(sucess) == 1) {
                            String isExp = jsonUserExp.getString(JSONFunctions.IS_USER_EXP);

                            if (Boolean.parseBoolean(isExp)) {

                                //INSERTING EXPERIENCE DATA
                                ContentValues values = new ContentValues();

                                values.put(MyContentProvider.WORK_EXPERIENCE_ID, jsonUserExp.getString(JSONFunctions.EXPERIENCE__ID));
                                values.put(MyContentProvider.WORK_EXPERIENCE_START_DATE, jsonUserExp.getString(JSONFunctions.EXPERIENCE_START_DATE));
                                values.put(MyContentProvider.WORK_EXPERIENCE_END_DATE, jsonUserExp.getString(JSONFunctions.EDUCATION_END_DATE));
                                values.put(MyContentProvider.WORK_EXPERIENCE_POSITION, jsonUserExp.getString(JSONFunctions.EXPERIENCE_POSITION));
                                values.put(MyContentProvider.WORK_EXPERIENCE_EMPLOYER, jsonUserExp.getString(JSONFunctions.EXPERIENCE_EMPLOYER));
                                values.put(MyContentProvider.WORK_EXPERIENCE_COUNTRY, jsonUserExp.getString(JSONFunctions.EDUCATION_COUNTRY));
                                values.put(MyContentProvider.WORK_EXPERIENCE_CITY, jsonUserExp.getString(JSONFunctions.EXPERIENCE_CITY));
                                values.put(MyContentProvider.WORK_EXPERIENCE_MAIN_ACTIVITIES, jsonUserExp.getString(JSONFunctions.EXPERIENCE_MAIN_ACTIVITIES));

                                //INSERT INTO EXPERIENCE TABLE
                                getContentResolver().insert(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE, values);

                                //INSERTING USER EXPERIENCE DATA
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(MyContentProvider.SQL_INSERT_OR_REPLACE, false);
                                contentValues.put(MyContentProvider.USER_WORK_EXPERIENCE_ID, jsonUserExp.getString(JSONFunctions.USER_EXPERIENCE_ID));
                                contentValues.put(MyContentProvider.USER_WORK_EXPERIENCE_USER_FK, userUID);
                                contentValues.put(MyContentProvider.USER_WORK_EXPERIENCE_WORK_EXPERIENCE_FK, jsonUserExp.getString(JSONFunctions.EXPERIENCE__ID));

                                //INSERT INTO USER EXPERIENCE TABLE
                                getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_WORK_EXPERIENCE, contentValues);

                            }

                        } else if (Integer.parseInt(error) == 3) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_unexpected_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                    //GET USER EDUCATION DATA
                    getUserEducation(userUID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //GET USER EDUCATION
    //***********************************************************************************************************************************
    private void getUserEducation(final String userUID) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //Params
        params.put(JSONFunctions.TAG, JSONFunctions.get_user_education_tag);
        params.put(JSONFunctions.USER_UID, userUID);

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                System.out.println(responseString);
                try {

                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonUserEdu = (JSONObject) arr.get(i);

                        String sucess = jsonUserEdu.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonUserEdu.getString(JSONFunctions.JSON_ERROR);

                        if (Integer.parseInt(sucess) == 1) {
                            String isEdu = jsonUserEdu.getString(JSONFunctions.IS_USER_EDU);

                            if (Boolean.parseBoolean(isEdu)) {
                                Log.d("", "insertEducation");
                                //INSERTING EDUCATION DATA
                                ContentValues values = new ContentValues();

                                values.put(MyContentProvider.EDUCATION_ID, jsonUserEdu.getString(JSONFunctions.EDUCATION__ID));
                                values.put(MyContentProvider.EDUCATION_START_DATE, jsonUserEdu.getString(JSONFunctions.EDUCATION_START_DATE));
                                values.put(MyContentProvider.EDUCATION_END_DATE, jsonUserEdu.getString(JSONFunctions.EDUCATION_END_DATE));
                                values.put(MyContentProvider.EDUCATION_QUALIFICATION_NAME, jsonUserEdu.getString(JSONFunctions.EDUCATION_QUALIFICATION));
                                values.put(MyContentProvider.EDUCATION_PROVIDER, jsonUserEdu.getString(JSONFunctions.EDUCATION_PROVIDER));
                                values.put(MyContentProvider.EDUCATION_COUNTRY, jsonUserEdu.getString(JSONFunctions.EDUCATION_COUNTRY));
                                values.put(MyContentProvider.EDUCATION_CITY, jsonUserEdu.getString(JSONFunctions.EDUCATION_CITY));
                                values.put(MyContentProvider.EDUCATION_MAIN_SUBJECTS, jsonUserEdu.getString(JSONFunctions.EDUCATION_MAIN_SUBJECTS));

                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_EDUCATION, values);

                                //INSERTING USER EDUCATION DATA
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(MyContentProvider.USER_EDUCATION_ID, jsonUserEdu.getString(JSONFunctions.USER_EDUCATION_ID));
                                contentValues.put(MyContentProvider.USER_EDUCATION_USERS_FK, userUID);
                                contentValues.put(MyContentProvider.USER_EDUCATION_EDUCATION_FK, jsonUserEdu.getString(JSONFunctions.EDUCATION__ID));

                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_EDUCATION, contentValues);

                            }

                        } else if (Integer.parseInt(error) == 3) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_unexpected_message), Toast.LENGTH_SHORT).show();
                        }
                    }

                    //GET USER LANGUAGES DATA
                    getUserLanguages(userUID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //GET USER LANGUAGE
    //***********************************************************************************************************************************
    private void getUserLanguages(final String userUID) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //Params
        params.put(JSONFunctions.TAG, JSONFunctions.get_user_language_tag);
        params.put(JSONFunctions.USER_UID, userUID);

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                System.out.println(responseString);
                try {

                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonUserLang = (JSONObject) arr.get(i);

                        String sucess = jsonUserLang.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonUserLang.getString(JSONFunctions.JSON_ERROR);

                        if (Integer.parseInt(sucess) == 1) {
                            String isLang = jsonUserLang.getString(JSONFunctions.IS_USER_LANG);

                            if (Boolean.parseBoolean(isLang)) {

                                //INSERTING LANGUAGE DATA
                                ContentValues values = new ContentValues();

                                values.put(MyContentProvider.LANGUAGE_ID, jsonUserLang.getString(JSONFunctions.LANGUAGE__ID));
                                values.put(MyContentProvider.LANGUAGE_NAME, jsonUserLang.getString(JSONFunctions.LANGUAGE_NAME));
                                values.put(MyContentProvider.LANGUAGE_TYPE_FK, jsonUserLang.getString(JSONFunctions.LANGUAGE_TYPE_FK));

                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_LANGUAGE, values);

                                //INSERTING USER LANGUAGE DATA
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(MyContentProvider.USER_LANG_ID, jsonUserLang.getString(JSONFunctions.USER_LANGUAGE_ID));
                                contentValues.put(MyContentProvider.USER_LANG_USERS_ID_FK, userUID);
                                contentValues.put(MyContentProvider.USER_LANG_LANG_FK, jsonUserLang.getString(JSONFunctions.LANGUAGE__ID));

                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_USER_LANGUAGE, contentValues);

                            }

                        } else if (Integer.parseInt(error) == 3) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_unexpected_message), Toast.LENGTH_SHORT).show();
                        }

                    }

                    //GET USER EDUCATION DATA
                    getLanguageEvaluation(userUID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //GET LANGUAGE EVALUATION
    //***********************************************************************************************************************************
    private void getLanguageEvaluation(String userUID) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //Params
        params.put(JSONFunctions.TAG, JSONFunctions.get_language_eval_tag);
        params.put(JSONFunctions.USER_UID, userUID);

        client.post(JSONFunctions.indexURL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                System.out.println(responseString);
                pDialog.hide();
                pDialog.dismiss();

                try {

                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonLangEval = (JSONObject) arr.get(i);

                        String sucess = jsonLangEval.getString(JSONFunctions.JSON_SUCCESS);
                        String error = jsonLangEval.getString(JSONFunctions.JSON_ERROR);

                        if (Integer.parseInt(sucess) == 1) {
                            String isEval = jsonLangEval.getString(JSONFunctions.IS_LANG_EV);

                            if (Boolean.parseBoolean(isEval)) {

                                //INSERT INTO EVALUATION
                                ContentValues values = new ContentValues();

                                values.put(MyContentProvider.LANGUAGE_EVALUATION_ID, jsonLangEval.getString(JSONFunctions.LANGUAGE_EVALUATION_ID));
                                values.put(MyContentProvider.LANGUAGE_EVALUATION_LANG_FK, jsonLangEval.getString(JSONFunctions.LANGUAGE_EVALUATION_LANGUAGE__ID));
                                values.put(MyContentProvider.LANGUAGE_EVALUATION_ASSESSMENT_FK, jsonLangEval.getString(JSONFunctions.LANGUAGE_EVALUATION_ASSESSMENT__ID));

                                getApplicationContext().getContentResolver().insert(MyContentProvider.CONTENT_URI_EVALUATION, values);

                            }

                        } else if (Integer.parseInt(error) == 3) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_unexpected_message), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //CHECK IF USER IS FIRST TIME HERE
                if (sessionManager.isNoMoreFirstTimeHere()) {
                    //START MAIN ACTIVITY
                    sessionManager.setLogin(true);
                    finish();
                    startActivity(new Intent(getApplicationContext(),
                            MainActivity.class));
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                } else {
                    showGoToWizardDialog();
                }
            }
        });
    }

    //GO TO WIZARD DIALOG CHECK
    //*****************************************************************************************************************
    //*****************************************************************************************************************
    private void showGoToWizardDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_welcome_title)
                .content(R.string.contentDialogFirstTime)
                .positiveText(R.string.button_agree)
                .positiveColorRes(R.color.material_dialog_buttons)
                .negativeColorRes(R.color.material_dialog_buttons)
                .negativeText(R.string.skip)
                .theme(Theme.LIGHT)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                        sessionManager.setLogin(true);
                        sessionManager.setIsNoMoreFirstTime(true);
                        startActivity(new Intent(getApplicationContext(),
                                WizardActivity.class));
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                        sessionManager.setLogin(true);
                        sessionManager.setWizard(true);
                        sessionManager.setIsNoMoreFirstTime(true);
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                })
                .show();
    }


    //GOOGLE SIGN-IN CALLBACKS
    //***********************************************************************************************************************
    //***********************************************************************************************************************
    //GOOGLE SIGN IN
    //***********************************************************************************************************************
    @Override
    public void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        pDialog = new MaterialDialog.Builder(this)
                .title(R.string.p_dialog_login_title)
                .content(R.string.p_dialog_content_wait)
                .theme(Theme.LIGHT)
                .widgetColorRes(R.color.material_dialog_buttons)
                .progress(true, 0)
                .show();
    }

    //GOOGLE SIGN OUT
    //***********************************************************************************************************************
    private void onSignOutClicked() {
        // Clear the default account so that GoogleApiClient will not automatically
        // connect in the future.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        // Show the signed-in UI
        tryGoogleLogin();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Log.w(TAG, "onConnectionSuspended:" + i);
        // Show the signed-out UI
        if (pDialog != null){
            pDialog.dismiss();
            pDialog.hide();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            if (pDialog != null){
                pDialog.dismiss();
                pDialog.hide();
            }
        }
    }

    //ERROR DIALOG
    //***********************************************************************************************************************
    private void showErrorDialog(ConnectionResult connectionResult) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, RC_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mShouldResolve = false;
                            }
                        }).show();
            } else {
                Log.w(TAG, "Google Play Services Error:" + connectionResult);
                String errorString = apiAvailability.getErrorString(resultCode);
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

                mShouldResolve = false;
                // Show the signed-out UI
                if (pDialog != null){
                    pDialog.dismiss();
                    pDialog.hide();
                }
            }
        }
    }

    //CHECK ACCOUNT PERMISSIONS
    //***********************************************************************************************************************
    private boolean checkAccountsPermission() {
        final String perm = Manifest.permission.GET_ACCOUNTS;
        int permissionCheck = ContextCompat.checkSelfPermission(this, perm);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // We have the permission
            return true;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
            // Need to show permission rationale, display a snackbar and then request
            // the permission again when the snackbar is dismissed.
            Snackbar.make(findViewById(R.id.mainLayout),
                    R.string.contacts_permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Request the permission again.
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{perm},
                                    RC_PERM_GET_ACCOUNTS);
                        }
                    }).show();
            return false;
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{perm},
                    RC_PERM_GET_ACCOUNTS);
            return false;
        }
    }

    //TRY LOGIN WITH GOOGLE ACCOUNT
    //***********************************************************************************************************************
    private void tryGoogleLogin() {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (currentPerson != null) {
            if(pDialog != null){
                pDialog.dismiss();
                pDialog.hide();
            }
            // Show signed-in user's name
            String name = currentPerson.getDisplayName();

            // Show users' email address (which requires GET_ACCOUNTS permission)
            if (checkAccountsPermission()) {
                String currentAccount = Plus.AccountApi.getAccountName(mGoogleApiClient);
                tryLogin(currentAccount, null, GOOGLE_LOGIN);
            }
        } else {
            // If getCurrentPerson returns null there is generally some error with the
            // configuration of the application (invalid Client ID, Plus API not enabled, etc).
            Log.w(TAG, getString(R.string.error_null_person));
            if(pDialog != null){
                pDialog.dismiss();
                pDialog.hide();
            }
            Toast.makeText(this,"Error signin in",Toast.LENGTH_SHORT).show();
        }
    }

}

