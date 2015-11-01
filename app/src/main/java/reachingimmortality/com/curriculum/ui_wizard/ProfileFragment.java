package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;

import java.util.HashMap;

import reachingimmortality.com.curriculum.interfaces.WizardCallback;
import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;


public class ProfileFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    //Controllers
    private SimpleCursorAdapter mAdapter;
    private WizardCallback wizardCallback;
    private JSONFunctions jsonFunctions;
    private SQLController sqlController;
    private LayoutController layoutController;

    //UI elements
    private AutoCompleteTextView acCountry;
    private EditText etName,etSurname,etMail,etCity,etPostalCode,etWebBlog,etStreetAdress,etTelNumber;

    //HASHMAP PROFILE
    private HashMap<String, String> profileDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TITLE
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layPersTitle);

        //BUTTONS
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);

        //EDIT TEXTS
        etName= (EditText) view.findViewById(R.id.proWizardName);
        etSurname= (EditText) view.findViewById(R.id.proWizardSurname);
        etMail = (EditText) view.findViewById(R.id.proWizardEmail);
        etStreetAdress= (EditText) view.findViewById(R.id.proWizardStreetAdress);
        etCity= (EditText) view.findViewById(R.id.proWizardCity);
        etPostalCode = (EditText) view.findViewById(R.id.proWizardPostal);
        acCountry = (AutoCompleteTextView) view.findViewById(R.id.proWizardCountry);
        etTelNumber = (EditText) view.findViewById(R.id.proWizardTelNum);
        etWebBlog = (EditText) view.findViewById(R.id.proWizardWebBlog);

        layoutTitle.setPadding(10, layoutController.getStatusBarHeight(getActivity()), 10, 10);

        loadAutoComplete();

        //CHECK IF THERE'S DATA FOR PROFILE
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_PROFILE,null,null,null,null);
        if(cursor.moveToFirst()){
            etName.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_NAME)));
            etSurname.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_SURNAME)));
            etMail.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_EMAIL)));
            etStreetAdress.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_STREET_ADRESS)));
            etCity.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_CITY)));
            acCountry.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_COUNTRY)));
            etPostalCode.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_POSTAL_CODE)));
            etTelNumber.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_TEL_NUMBER)));
            etWebBlog.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_WEB_BLOG)));
        }
        cursor.close();

        //SET BUTTON TEXTS
        btBack.setVisibility(View.GONE);
        btNext.setText(R.string.button_next);
        //CLICK LISTENERS
        btNext.setOnClickListener(this);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            wizardCallback = (WizardCallback) getActivity();
            jsonFunctions = new JSONFunctions();
            layoutController = new LayoutController();
            sqlController = new SQLController(getActivity().getApplicationContext());
        }catch (Exception ex){ex.printStackTrace();}

    }

    private void loadAutoComplete() {
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[] {MyContentProvider.COUNTRY_NAME},
                new int[] { android.R.id.text1 }, 0);
        acCountry.setAdapter(mAdapter);
        acCountry.setOnClickListener(this);
        //getActivity().getLoaderManager().initLoader(0, null, this);
        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence str) {
                return getCursor(str);
            }
        });
        mAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {

                int index = cur.getColumnIndex(MyContentProvider.COUNTRY_NAME);
                return cur.getString(index);
            }
        });
    }

    private Cursor getCursor(CharSequence str) {

        String select =MyContentProvider.COUNTRY_NAME + " LIKE ? ";
        String[]  selectArgs = { str + "%"};
        String[] contactsProjection = new String[] {
                MyContentProvider.COUNTRY_ID,
                MyContentProvider.COUNTRY_NAME
        };

        return getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_COUNTRIES_LIST, contactsProjection, select, selectArgs, null);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MyContentProvider.CONTENT_URI_COUNTRIES_LIST;
        return new CursorLoader(null,uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //AUTO Complete text
            case R.id.proWizardCountry:
                acCountry.showDropDown();
                break;

            case R.id.btMainPositiveButton:

                String profile = jsonFunctions.composeProfileList(
                        etName.getText().toString(),
                        etSurname.getText().toString(),
                        etMail.getText().toString(),
                        etStreetAdress.getText().toString(),
                        etPostalCode.getText().toString(),
                        etCity.getText().toString(),
                        acCountry.getText().toString(),
                        etTelNumber.getText().toString(),
                        etWebBlog.getText().toString());

                wizardCallback.insertProfileIntoMySQL(profile);
                break;

        }
    }

}
