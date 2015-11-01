package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
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
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;
import reachingimmortality.com.curriculum.dialogs.MyDatePickerDialog;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExperienceEditFragment extends Fragment implements View.OnClickListener, MyDatePickerDialog.OnDateSelectedCallback {
    //UI elements
    private TextView tvFromDate, tvToDate;
    private EditText etPosition, etEmployer, etCity, etMainActivities;
    private MaterialAutoCompleteTextView acCountry;

    //Adapters
    private SimpleCursorAdapter mAdapter;

    //Item ID
    private Uri workExperienceItemUri;

    //Controllers and interfaces
    private JSONFunctions jsonFunctions;
    private LayoutController layoutController;
    private SQLController sqlController;
    private WizardCallback wizardCallback;

    public static ExperienceEditFragment newInstance(Uri uri) {
        ExperienceEditFragment fragment = new ExperienceEditFragment();

        Bundle args = new Bundle();
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_WORK_EXPERIENCE, uri);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_experience_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TextView
        tvFromDate = (TextView) view.findViewById(R.id.expWizFromDate);
        tvToDate = (TextView) view.findViewById(R.id.expWizToDate);

        //Edit texts
        etPosition = (EditText) view.findViewById(R.id.expWizEtPosition);
        etEmployer = (EditText) view.findViewById(R.id.expWizEtEmployer);
        etCity = (EditText) view.findViewById(R.id.expWizEtCity);
        etMainActivities = (EditText) view.findViewById(R.id.expWizEtMainAct);
        acCountry = (MaterialAutoCompleteTextView) view.findViewById(R.id.expWizEtCountry);

        //Buttons
        Button btSave = (Button) view.findViewById(R.id.btMainPositiveButton);
        Button btCancel = (Button) view.findViewById(R.id.btMainNegativeButton);
        ImageButton btCalFrom = (ImageButton) view.findViewById(R.id.expWizBtCalFrom);
        ImageButton btCalTo = (ImageButton) view.findViewById(R.id.expWizBtCalTo);

        //Status bar
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layWorkAddTitle);
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
            workExperienceItemUri = args.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_WORK_EXPERIENCE);
            fillData();
        }
        //Clicklisteners
        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCalFrom.setOnClickListener(this);
        btCalTo.setOnClickListener(this);
    }

    private void fillData() {
        Cursor mCursor = getActivity().getContentResolver().query(workExperienceItemUri, null, null, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                etPosition.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_POSITION)));
                etEmployer.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_EMPLOYER)));
                etCity.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_CITY)));
                acCountry.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_COUNTRY)));
                etMainActivities.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_MAIN_ACTIVITIES)));

                tvFromDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_START_DATE)));
                tvToDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_END_DATE)));
            }
            mCursor.close();
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
        sqlController = new SQLController(activity);
        layoutController = new LayoutController();
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
                String experienceID;
                String experienceCase;

                if (workExperienceItemUri != null) {
                    experienceID = workExperienceItemUri.getPathSegments().get(1);
                    experienceCase = JSONFunctions.experience_update_tag;
                } else {
                    experienceID = null;
                    experienceCase = JSONFunctions.experience_add_tag;
                }

                String experienceJSONList = jsonFunctions.composeExperienceJSON(
                        experienceID,
                        tvFromDate.getText().toString(),
                        tvToDate.getText().toString(),
                        etPosition.getText().toString(),
                        etEmployer.getText().toString(),
                        etCity.getText().toString(),
                        acCountry.getText().toString(),
                        etMainActivities.getText().toString());

                wizardCallback.insertExperienceIntoMySQL(experienceCase, experienceJSONList);

                break;

            case R.id.btMainNegativeButton:

                getActivity().onBackPressed();

                break;

            case R.id.expWizBtCalFrom:
                sqlController.open();
                HashMap<String, String> dateStartSection = sqlController.getExperienceDateSection();

                MyDatePickerDialog datePickerDialogFrom = MyDatePickerDialog.newInstance(MainActivity.DATE_FROM,
                        dateStartSection.get(MyContentProvider.DATE_EXPERIENCE_START_DAY),
                        dateStartSection.get(MyContentProvider.DATE_EXPERIENCE_START_MONTH),
                        dateStartSection.get(MyContentProvider.DATE_EXPERIENCE_START_YEAR));

                datePickerDialogFrom.setTargetFragment(this,0);
                datePickerDialogFrom.show(getFragmentManager(), "dialog");

                break;

            case R.id.expWizBtCalTo:
                sqlController.open();
                HashMap<String, String> dateEndSection = sqlController.getExperienceDateSection();

                MyDatePickerDialog datePickerDialogTo = MyDatePickerDialog.newInstance(MainActivity.DATE_TO,
                        dateEndSection.get(MyContentProvider.DATE_EXPERIENCE_END_DAY),
                        dateEndSection.get(MyContentProvider.DATE_EXPERIENCE_END_MONTH),
                        dateEndSection.get(MyContentProvider.DATE_EXPERIENCE_END_YEAR));

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
