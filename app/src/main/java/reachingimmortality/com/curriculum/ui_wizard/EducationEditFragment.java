package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.HashMap;

import reachingimmortality.com.curriculum.MainActivity;
import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.database_library.SQLController;
import reachingimmortality.com.curriculum.dialogs.MyDatePickerDialog;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class EducationEditFragment extends Fragment implements View.OnClickListener, MyDatePickerDialog.OnDateSelectedCallback {

    //UI elements
    private TextView tvFromDate, tvToDate;
    private EditText etQualification, etProvider, etCity, etMainSubjects;
    private MaterialAutoCompleteTextView acCountry;

    //Adapters
    private SimpleCursorAdapter mAdapter;

    //Item ID
    private Uri educationItemUri;

    //Controllers and interfaces
    private JSONFunctions jsonFunctions;
    private LayoutController layoutController;
    private SQLController sqlController;
    private WizardCallback wizardCallback;

    public static EducationEditFragment newInstance(Uri uri) {
        EducationEditFragment fragment = new EducationEditFragment();

        Bundle args = new Bundle();
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_EDUCATION, uri);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_education_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TextView
        tvFromDate = (TextView) view.findViewById(R.id.eduWizFromDate);
        tvToDate = (TextView) view.findViewById(R.id.eduWizToDate);

        //Edit texts
        etQualification = (EditText) view.findViewById(R.id.eduWizEtQualification);
        etProvider = (EditText) view.findViewById(R.id.eduWizEtProvider);
        etCity = (EditText) view.findViewById(R.id.eduWizEtCity);
        etMainSubjects = (EditText) view.findViewById(R.id.eduWizEtMainSub);
        acCountry = (MaterialAutoCompleteTextView) view.findViewById(R.id.eduWizEtCountry);

        //Buttons
        Button btSave = (Button) view.findViewById(R.id.btMainPositiveButton);
        Button btCancel = (Button) view.findViewById(R.id.btMainNegativeButton);
        ImageButton btCalFrom = (ImageButton) view.findViewById(R.id.eduWizBtCalFrom);
        ImageButton btCalTo = (ImageButton) view.findViewById(R.id.eduWizBtCalTo);

        //Status bar
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layTitle);
        layoutTitle.setPadding(10, layoutController.getStatusBarHeight(getActivity()), 10, 10);

        //SET BUTTON TITLE
        btSave.setText(R.string.button_ok);
        btCancel.setText(R.string.button_cancel);

        //Load data
        loadCountriesData();

        //Check for existing data
        Bundle args = getArguments();
        if (args != null) {
            //Init Edit mode
            educationItemUri = args.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_EDUCATION);
            fillData();
        }

        //Clicklisteners
        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCalFrom.setOnClickListener(this);
        btCalTo.setOnClickListener(this);
    }

    private void fillData() {
        Cursor mCursor = getActivity().getContentResolver().query(educationItemUri, null, null, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                etQualification.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_QUALIFICATION_NAME)));
                etProvider.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_PROVIDER)));
                etCity.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_CITY)));
                acCountry.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_COUNTRY)));
                etMainSubjects.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_MAIN_SUBJECTS)));

                tvFromDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_START_DATE)));
                tvToDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_END_DATE)));
            }
            mCursor.close();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            wizardCallback = (WizardCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jsonFunctions = new JSONFunctions();
        layoutController = new LayoutController();
        sqlController = new SQLController(getActivity());
    }

    //LOAD AUTOCOMPLETE COUNTRY
    //*****************************************************************************************************************
    //*****************************************************************************************************************
    private void loadCountriesData() {
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{MyContentProvider.COUNTRY_NAME},
                new int[]{android.R.id.text1}, 0);
        acCountry.setAdapter(mAdapter);
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
        String select = MyContentProvider.COUNTRY_NAME + " LIKE ? ";
        String[] selectArgs = {str + "%"};
        String[] contactsProjection = new String[]{
                MyContentProvider.COUNTRY_ID,
                MyContentProvider.COUNTRY_NAME
        };

        return getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_COUNTRIES_LIST, contactsProjection, select, selectArgs, null);
    }

    //CLICK LISTENERS
    //*****************************************************************************************************************
    //*****************************************************************************************************************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btMainPositiveButton:
                String educationID;
                String educationCase;

                if (educationItemUri != null) {
                    educationID = educationItemUri.getPathSegments().get(1);
                    educationCase = JSONFunctions.education_update_tag;
                } else {
                    educationID = null;
                    educationCase = JSONFunctions.education_add_tag;
                }

                String experienceJSONList = jsonFunctions.composeEducationJSON(
                        educationID,
                        tvFromDate.getText().toString(),
                        tvToDate.getText().toString(),
                        etQualification.getText().toString(),
                        etProvider.getText().toString(),
                        etCity.getText().toString(),
                        acCountry.getText().toString(),
                        etMainSubjects.getText().toString());

                wizardCallback.insertEducationIntoMySQL(educationCase, experienceJSONList);

                break;

            case R.id.btMainNegativeButton:

                getActivity().onBackPressed();

                break;

            case R.id.eduWizBtCalFrom:
                sqlController.open();
                HashMap<String, String> dateStartSection = sqlController.getEducationDateSection();

                MyDatePickerDialog datePickerDialogFrom = MyDatePickerDialog.newInstance(MainActivity.DATE_FROM,
                        dateStartSection.get(MyContentProvider.DATE_EDUCATION_START_DAY),
                        dateStartSection.get(MyContentProvider.DATE_EDUCATION_START_MONTH),
                        dateStartSection.get(MyContentProvider.DATE_EDUCATION_START_YEAR));

                datePickerDialogFrom.setTargetFragment(this,0);
                datePickerDialogFrom.show(getFragmentManager(), "dialog");

                break;

            case R.id.eduWizBtCalTo:
                sqlController.open();
                HashMap<String, String> dateEndSection = sqlController.getEducationDateSection();

                MyDatePickerDialog datePickerDialogTo = MyDatePickerDialog.newInstance(MainActivity.DATE_TO,
                        dateEndSection.get(MyContentProvider.DATE_EDUCATION_END_DAY),
                        dateEndSection.get(MyContentProvider.DATE_EDUCATION_END_MONTH),
                        dateEndSection.get(MyContentProvider.DATE_EDUCATION_END_YEAR));

                datePickerDialogTo.setTargetFragment(this, 0);
                datePickerDialogTo.show(getFragmentManager(),"dialog");

                break;
        }
    }

    //DATE PICKER LISTENER
    //*****************************************************************************************************************
    //*****************************************************************************************************************
    @Override
    public void onPositiveClick(String dateCase, int year, int monthOfYear, int dayOfMonth) {

        String dateFormat = year+"-"+monthOfYear+"-"+dayOfMonth;

        switch (dateCase){
            case MainActivity.DATE_FROM:
                tvFromDate.setText(dateFormat);
                break;
            case MainActivity.DATE_TO:
                tvToDate.setText(dateFormat);
                break;
        }
    }

    @Override
    public void onNegativeClick() {

    }
}
