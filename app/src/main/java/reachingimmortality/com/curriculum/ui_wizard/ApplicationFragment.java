package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.adapters.NothingSelectedSpinnerAdapter;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    //Controllers
    private WizardCallback wizardCallback;
    private JSONFunctions jsonFunctions;
    private LayoutController layoutController;

    private Spinner mySpinner;
    private SimpleCursorAdapter mAdapter;
    private EditText etDescription;

    //CHECK FOR EXISTING DATA
    private Boolean isThereApp = false;
    private int spinnerIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_application, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SET SHARED PREFS
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.setProfile(true);

        //INITALIZING UI ELEMENTS
        mySpinner = (Spinner) view.findViewById(R.id.appWizardSpinnerType);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        etDescription = (EditText) view.findViewById(R.id.appWizardEtDescr);
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layAppViewTitle);

        //SET TITLE PADDING
        layoutTitle.setPadding(10, layoutController.getStatusBarHeight(getActivity().getApplicationContext()), 10, 10);

        //SET BUTTON TITLE
        btNext.setText(R.string.button_next);
        btBack.setText(R.string.button_back);

        //Init listener on button
        btNext.setOnClickListener(this);
        btBack.setOnClickListener(this);
        //CHECK IF THERE'S DATA IN SQLITE
        loadData();
        //LOAD DATA
        loadSpinnerData();
    }

    //LOAD DATA FROM SQLITE
    //*********************************************************************************************************************************
    private void loadData() {
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_USER_APPLICATION, null, null, null, null);
        if (cursor.moveToFirst()) {
            isThereApp = true;
            spinnerIndex = cursor.getInt(cursor.getColumnIndex(MyContentProvider.APPLICATION_TYPE_ID_FK));
            etDescription.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.APPLICATION_DESCRIPTION)));
        }
        cursor.close();

    }

    //LOAD SPINNER DATA
    //*********************************************************************************************************************************
    private void loadSpinnerData() {

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.simple_list_item_dark_bground,
                null,
                new String[]{MyContentProvider.APPLICATION_TYPE_NAME},
                new int[]{android.R.id.text1}, 0);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (isThereApp) {
            mySpinner.setAdapter(mAdapter);

            getActivity().getLoaderManager().initLoader(0, null, this);

            mySpinner.setSelection(spinnerIndex);
        } else {
            mySpinner.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            mAdapter,
                            R.layout.simple_item_nothing_selected,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            getActivity()));
            getActivity().getLoaderManager().initLoader(0, null, this);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            wizardCallback = (WizardCallback) getActivity();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jsonFunctions = new JSONFunctions();
        layoutController = new LayoutController();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MyContentProvider.CONTENT_URI_APPLICATION_TYPE;
        return new CursorLoader(getActivity(), uri, null, null, null, null);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btMainPositiveButton:
                saveData();
                break;

            case R.id.btMainNegativeButton:
                wizardCallback.goToProfile();
                break;
        }
    }

    //ON CLICK SAVE INPUT DATA
    //************************************************************************************************************************
    private void saveData() {
        String applicationID;
        String userApplicationID;
        String applicationCASE;

        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_USER_APPLICATION,
                new String[]{MyContentProvider.USER_APPLICATION_APPLICATION_FK, MyContentProvider.APPLICATION_ID}, null, null, null);
        if (cursor.moveToFirst()) {
            applicationCASE = JSONFunctions.application_update_tag;
            userApplicationID = cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_APPLICATION_ID));
            applicationID = cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_APPLICATION_APPLICATION_FK));
        } else {
            applicationCASE = JSONFunctions.application_add_tag;
            userApplicationID = null;
            applicationID = null;
        }
        cursor.close();

        String jsonApplicationList = jsonFunctions.composeApplicationList(
                applicationID,
                etDescription.getText().toString(),
                String.valueOf(mySpinner.getSelectedItemId()),
                userApplicationID);

        wizardCallback.insertApplicationIntoMySQL(applicationCASE, jsonApplicationList);
    }
}
