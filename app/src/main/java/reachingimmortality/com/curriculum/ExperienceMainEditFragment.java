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
public class ExperienceMainEditFragment extends Fragment implements View.OnClickListener,MyDatePickerDialog.OnDateSelectedCallback {

    //UI ELEMENTS
    private TextView tvFromDate, tvToDate;
    private EditText etPosition, etEmployer, etCountry, etCity, etMainActivities;
    private ActionButton fabExpMainEdit;
    //Interfaces
    private MainCallback mainCallback;

    //Item ID
    private Uri experienceItemUri;

    //Controllers and interfaces
    private JSONFunctions jsonFunctions;
    private SQLController sqlController;

    public static ExperienceMainEditFragment newInstance(Uri uri) {

        ExperienceMainEditFragment fragment = new ExperienceMainEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_WORK_EXPERIENCE, uri);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experience_main_edit, container, false);
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
        etPosition = (EditText) view.findViewById(R.id.expMainEtPosition);
        etEmployer = (EditText) view.findViewById(R.id.expMainEtEmployer);
        etCountry = (EditText) view.findViewById(R.id.expMainEtCountry);
        etCity = (EditText) view.findViewById(R.id.expMainEtCity);
        etMainActivities = (EditText) view.findViewById(R.id.expMainEtMainAct);

        //TextViews
        tvFromDate = (TextView) view.findViewById(R.id.expMainFromDate);
        tvToDate = (TextView) view.findViewById(R.id.expMainToDate);

        //Image buttons
        ImageButton btCalFrom = (ImageButton) view.findViewById(R.id.expMainBtCalFrom);
        ImageButton btCalTo = (ImageButton) view.findViewById(R.id.expMainBtCalTo);

        //FAB ANIMATIONS
        fabExpMainEdit = (ActionButton) getActivity().findViewById(R.id.fabMain);

        fabExpMainEdit.setImageResource(R.mipmap.ic_checked_white);
        fabExpMainEdit.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabExpMainEdit.setButtonColorRipple(android.R.color.white);

        fabExpMainEdit.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabExpMainEdit.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabExpMainEdit.hide();
        fabExpMainEdit.playShowAnimation();
        fabExpMainEdit.show();



        //Check for arguments
        Bundle args = getArguments();
        if (args != null) {
            //Init Edit mode
            experienceItemUri = args.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_WORK_EXPERIENCE);
        }

        if(experienceItemUri != null){
            fillData();
        }
        //CLICK LISTENERS
        fabExpMainEdit.setOnClickListener(this);
        btCalFrom.setOnClickListener(this);
        btCalTo.setOnClickListener(this);
    }

    private void fillData() {
        Cursor mCursor = getActivity().getContentResolver().query(experienceItemUri, null, null, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                etPosition.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_POSITION)));
                etEmployer.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_EMPLOYER)));
                etCity.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_CITY)));
                etCountry.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_COUNTRY)));
                etMainActivities.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_MAIN_ACTIVITIES)));

                tvFromDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_START_DATE)));
                tvToDate.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_END_DATE)));
            }
            mCursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabMain:
                fabExpMainEdit.hide();
                fabExpMainEdit.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;

            case R.id.expMainBtCalFrom:
                sqlController.open();
                HashMap<String, String> dateStartSection = sqlController.getExperienceDateSection();

                MyDatePickerDialog datePickerDialogFrom = MyDatePickerDialog.newInstance(MainActivity.DATE_FROM,
                        dateStartSection.get(MyContentProvider.DATE_EXPERIENCE_START_DAY),
                        dateStartSection.get(MyContentProvider.DATE_EXPERIENCE_START_MONTH),
                        dateStartSection.get(MyContentProvider.DATE_EXPERIENCE_START_YEAR));

                datePickerDialogFrom.setTargetFragment(this,0);
                datePickerDialogFrom.show(getFragmentManager(), "dialog");

                break;

            case R.id.expMainBtCalTo:
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

    private Runnable getShowRunnable() {
        return new Runnable() {
            @Override
            public void run() {

                String experienceID;
                String experienceCase;

                if(experienceItemUri != null){
                    experienceID = experienceItemUri.getPathSegments().get(1);
                    experienceCase = JSONFunctions.experience_update_tag;
                }else{
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
                        etCountry.getText().toString(),
                        etMainActivities.getText().toString());

                System.out.println(experienceCase);
                System.out.println(experienceJSONList);
                mainCallback.saveExperience(experienceCase, experienceJSONList);
            }
        };
    }
}
