package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsFragment extends Fragment implements View.OnClickListener {


    //UI ELEMENTS
    private TextView tvCommunication, tvManagament, tvJobRelated, tvDigComp;

    //INTERFACE
    private WizardCallback wizardCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_skills, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            wizardCallback = (WizardCallback) activity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SET SHARED PREFS
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.setProfile(false);
        sessionManager.setApplication(false);
        sessionManager.setExperience(false);
        sessionManager.setEducation(false);
        sessionManager.setMotherLanguage(false);
        sessionManager.setOtherLanguage(true);

        //UI ELEMENTS
        //TEXTVIEWS
        tvCommunication = (TextView) view.findViewById(R.id.skillMainTvCommunication);
        tvManagament = (TextView) view.findViewById(R.id.skillMainTvManagament);
        tvJobRelated = (TextView) view.findViewById(R.id.skillMainTvJobRelated);
        tvDigComp = (TextView) view.findViewById(R.id.skillMainTvDigComp);

        //BUTTONS
        Button btCommEdit = (Button) view.findViewById(R.id.skillMainCommEdit);
        Button btManEdit = (Button) view.findViewById(R.id.skillMainManEdit);
        Button btJobEdit = (Button) view.findViewById(R.id.skillMainJobEdit);
        Button btDigEdit = (Button) view.findViewById(R.id.skillMainDigEdit);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);

        //SET UI ELEMENTS
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.skillsWizardLayTitle);
        LayoutController layoutController = new LayoutController();
        layoutTitle.setPadding(10, layoutController.getStatusBarHeight(getActivity()), 10, 10);

        btBack.setText(R.string.button_back);
        btNext.setText(R.string.button_next);

        //LOAD DATA
        loadSkillData();

        //CLICK LISTENERS
        btBack.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btCommEdit.setOnClickListener(this);
        btManEdit.setOnClickListener(this);
        btJobEdit.setOnClickListener(this);
        btDigEdit.setOnClickListener(this);
    }

    //LOAD DATA
    //*****************************************************************************************************************
    private void loadSkillData() {
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_SKILLS,null,null,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                tvCommunication.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_COMMUNICATION)));
                tvManagament.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_MANAGMENT)));
                tvJobRelated.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_JOB_RELATED)));
                tvDigComp.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_IT)));

            }
        }
    }
    //ON CLICK
    //*****************************************************************************************************************
    @Override
    public void onClick(View v) {
        Uri skillItemUri = null;
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_SKILLS,null,null,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                skillItemUri = Uri.parse(MyContentProvider.CONTENT_URI_SKILLS + "/"+cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_ID)) );
            }
        }
        switch (v.getId()){
            case R.id.btMainPositiveButton:
                wizardCallback.goToExport();
                break;

            case R.id.btMainNegativeButton:
                wizardCallback.goToOtherLanguage();
                break;

            case R.id.skillMainCommEdit:
                wizardCallback.showSkillsAddEditFragment(JSONFunctions.COMMUNICATION_TYPE,skillItemUri);
                break;

            case R.id.skillMainManEdit:
                wizardCallback.showSkillsAddEditFragment(JSONFunctions.MANAGAMENT_TYPE, skillItemUri);
                break;

            case R.id.skillMainJobEdit:
                wizardCallback.showSkillsAddEditFragment(JSONFunctions.JOB_RELATED_TYPE, skillItemUri);
                break;

            case R.id.skillMainDigEdit:
                wizardCallback.showSkillsAddEditFragment(JSONFunctions.DIG_COMPETENCE_TYPE, skillItemUri);
                break;
        }
    }
}
