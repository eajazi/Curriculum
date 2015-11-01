package reachingimmortality.com.curriculum;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.software.shell.fab.ActionButton;

import java.util.HashMap;

import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;
import reachingimmortality.com.curriculum.dialogs.MyDatePickerDialog;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.MainCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class EducationMainEditFragment extends Fragment implements View.OnClickListener, MyDatePickerDialog.OnDateSelectedCallback {

    //UI ELEMENTS
    private TextView tvFromDate, tvToDate;
    private EditText etQualification, etProvider, etCountry, etCity, etMainSubjects;
    private ActionButton fabEduMainEdit;
    //Interfaces
    private MainCallback mainCallback;

    //Item ID
    private Uri educationItemUri;

    //Controllers and interfaces
    private JSONFunctions jsonFunctions;
    private SQLController sqlController;

    public static EducationMainEditFragment newInstance(Uri uri) {

        EducationMainEditFragment fragment = new EducationMainEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_EDUCATION, uri);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_education_main_edit, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mainCallback = (MainCallback) activity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        jsonFunctions = new JSONFunctions();
        sqlController = new SQLController(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS INIT
        //Edit texts
        etQualification = (EditText) view.findViewById(R.id.eduMainEtQualification);
        etProvider = (EditText) view.findViewById(R.id.eduMainEtProvider);
        etCountry = (EditText) view.findViewById(R.id.eduMainEtCountry);
        etCity = (EditText) view.findViewById(R.id.eduMainEtCity);
        etMainSubjects = (EditText) view.findViewById(R.id.eduMainEtMainSub);

        //TextViews
        tvFromDate = (TextView) view.findViewById(R.id.eduMainFromDate);
        tvToDate = (TextView) view.findViewById(R.id.eduMainToDate);

        //FAB ANIMATIONS
        fabEduMainEdit = (ActionButton) getActivity().findViewById(R.id.fabMain);

        fabEduMainEdit.setImageResource(R.mipmap.ic_checked_white);
        fabEduMainEdit.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabEduMainEdit.setButtonColorRipple(android.R.color.white);

        fabEduMainEdit.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabEduMainEdit.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabEduMainEdit.hide();
        fabEduMainEdit.playShowAnimation();
        fabEduMainEdit.show();

        ImageButton btCalFrom = (ImageButton) view.findViewById(R.id.eduMainBtCalFrom);
        ImageButton btCalTo = (ImageButton) view.findViewById(R.id.eduMainBtCalTo);

        //Check for arguments
        Bundle args = getArguments();
        if (args != null) {
            //Init Edit mode
            educationItemUri = args.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_EDUCATION);
        }

        //FILL DATA
        if(educationItemUri != null){
            fillData();
        }
        //CLICK LISTENERS
        fabEduMainEdit.setOnClickListener(this);
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
                etCountry.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_COUNTRY)));
                etMainSubjects.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_MAIN_SUBJECTS)));

                tvFromDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_START_DATE)));
                tvToDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.EDUCATION_END_DATE)));
            }
            mCursor.close();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabMain:
                fabEduMainEdit.hide();
                fabEduMainEdit.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;

            case R.id.eduMainBtCalFrom:
                sqlController.open();
                HashMap<String, String> dateStartSection = sqlController.getEducationDateSection();

                MyDatePickerDialog datePickerDialogFrom = MyDatePickerDialog.newInstance(MainActivity.DATE_FROM,
                        dateStartSection.get(MyContentProvider.DATE_EDUCATION_START_DAY),
                        dateStartSection.get(MyContentProvider.DATE_EDUCATION_START_MONTH),
                        dateStartSection.get(MyContentProvider.DATE_EDUCATION_START_YEAR));

                datePickerDialogFrom.setTargetFragment(this,0);
                datePickerDialogFrom.show(getFragmentManager(), "dialog");

                break;

            case R.id.eduMainBtCalTo:
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

    private Runnable getShowRunnable() {
        return new Runnable() {
            @Override
            public void run() {

                String educationID;
                String educationCase;

                if(educationItemUri != null){
                    educationID = educationItemUri.getPathSegments().get(1);
                    educationCase = JSONFunctions.education_update_tag;
                }else{
                    educationID = null;
                    educationCase = JSONFunctions.education_add_tag;
                }

                String educationJSONList = jsonFunctions.composeEducationJSON(
                        educationID,
                        tvFromDate.getText().toString(),
                        tvToDate.getText().toString(),
                        etQualification.getText().toString(),
                        etProvider.getText().toString(),
                        etCity.getText().toString(),
                        etCountry.getText().toString(),
                        etMainSubjects.getText().toString());

                mainCallback.saveEducation(educationCase, educationJSONList);
            }
        };

    }

    @Override
    public void onPositiveClick(String dateCase, int year, int monthOfYear, int dayOfMonth) {

        String dateFormat = year+"-"+monthOfYear+"-"+dayOfMonth;

        switch (dateCase){
            case MainActivity.DATE_EDU_FROM:
                tvFromDate.setText(dateFormat);
                break;
            case MainActivity.DATE_EDU_TO:
                tvToDate.setText(dateFormat);
                break;
        }
    }

    @Override
    public void onNegativeClick() {

    }
}
