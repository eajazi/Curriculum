package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.MainCallback;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsEditFragment extends Fragment implements View.OnClickListener {

    //UI ELEMENTS
    private ImageView imgCircIcon;
    private TextView tvTitle;
    private EditText etSkill;
    private ActionButton fabSkillMainEdit;

    //CONTROLLERS & INTERFACES
    private WizardCallback wizardCallback;
    private JSONFunctions jsonFunctions;

    //Uri-es
    private Uri skillsItemUri;

    //CONSTANT VARIABLES
    private String skillType;

    public static SkillsEditFragment newInstance(String skillType, Uri itemUri) {
        SkillsEditFragment fragment = new SkillsEditFragment();

        Bundle args = new Bundle();
        args.putString(JSONFunctions.SKILLS_TYPE, skillType);
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_SKILL, itemUri);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_skills_edit, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            wizardCallback = (WizardCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //UI ELEMENTS INIT
        imgCircIcon = (ImageView) view.findViewById(R.id.skillWizardEditCircleIcon);
        tvTitle = (TextView) view.findViewById(R.id.skillWizardEditTitle);
        etSkill = (EditText) view.findViewById(R.id.skillWizardEditEt);

        //BUTTONS
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);

        //SET UI ELEMENTS
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.skillWizardLayEditTitle);
        LayoutController layoutController = new LayoutController();
        layoutTitle.setPadding(10, layoutController.getStatusBarHeight(getActivity()), 10, 10);

        btBack.setText(R.string.button_cancel);
        btNext.setText(R.string.button_ok);

        //Load data
        if (getArguments() != null) {
            skillType = getArguments().getString(JSONFunctions.SKILLS_TYPE);
            skillsItemUri = getArguments().getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_SKILL);
        }

        loadData();

        //CLICK LISTENERS
        btBack.setOnClickListener(this);
        btNext.setOnClickListener(this);
    }

    private void loadData() {
        String skillType = getArguments().getString(JSONFunctions.SKILLS_TYPE);
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_SKILLS, null, null, null, null);
        if (skillType != null) {
            switch (skillType) {
                case JSONFunctions.COMMUNICATION_TYPE:
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            tvTitle.setText(getString(R.string.title_skills_communication));
                            imgCircIcon.setImageResource(R.mipmap.ic_comm_blue);
                            etSkill.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_COMMUNICATION)));
                        } else {
                            tvTitle.setText(getString(R.string.title_skills_communication));
                            imgCircIcon.setImageResource(R.mipmap.ic_comm_blue);
                        }
                    }
                    break;

                case JSONFunctions.MANAGAMENT_TYPE:
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            tvTitle.setText(getString(R.string.title_skills_managament));
                            imgCircIcon.setImageResource(R.mipmap.ic_man_blue);
                            etSkill.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_MANAGMENT)));
                        } else {
                            tvTitle.setText(getString(R.string.title_skills_managament));
                            imgCircIcon.setImageResource(R.mipmap.ic_man_blue);
                        }
                    }
                    break;

                case JSONFunctions.JOB_RELATED_TYPE:
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            tvTitle.setText(getString(R.string.title_skills_job_related));
                            imgCircIcon.setImageResource(R.mipmap.ic_job_blue);
                            etSkill.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_JOB_RELATED)));
                        } else {
                            tvTitle.setText(getString(R.string.title_skills_job_related));
                            imgCircIcon.setImageResource(R.mipmap.ic_job_blue);
                        }
                    }
                    break;

                case JSONFunctions.DIG_COMPETENCE_TYPE:
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            tvTitle.setText(getString(R.string.title_skills_dig_competence));
                            imgCircIcon.setImageResource(R.mipmap.ic_dig_blue);
                            etSkill.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.SKILL_IT)));
                        } else {
                            tvTitle.setText(getString(R.string.title_skills_dig_competence));
                            imgCircIcon.setImageResource(R.mipmap.ic_dig_blue);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btMainPositiveButton:
                String skillsCase;
                String skillsID;

                jsonFunctions = new JSONFunctions();

                if (skillsItemUri != null) {
                    skillsCase = JSONFunctions.skills_update_tag;
                    skillsID = skillsItemUri.getPathSegments().get(1);
                } else {
                    skillsCase = JSONFunctions.skills_add_tag;
                    skillsID = null;
                }
                String skillsJSON;

                if (getArguments() != null) {
                    //SWITCH SKILLS TYPE
                    //*********************************************************************************************************************
                    switch (skillType) {

                        //COMMUNICATION CASE
                        //**************************************************************************************************************
                        case JSONFunctions.COMMUNICATION_TYPE:
                            Log.d("type", "communication");
                            skillsJSON = jsonFunctions.composeCommunicationJSON(skillsID, JSONFunctions.COMMUNICATION_TYPE, etSkill.getText().toString());
                            wizardCallback.insertSkillsIntoMySQL(skillsCase, JSONFunctions.COMMUNICATION_TYPE, skillsJSON);

                            break;

                        //MANAGAMENT CASE
                        //**************************************************************************************************************
                        case JSONFunctions.MANAGAMENT_TYPE:
                            Log.d("type", "managament");
                            skillsJSON = jsonFunctions.composeCommunicationJSON(skillsID, JSONFunctions.MANAGAMENT_TYPE, etSkill.getText().toString());
                            wizardCallback.insertSkillsIntoMySQL(skillsCase, JSONFunctions.MANAGAMENT_TYPE, skillsJSON);

                            break;

                        //JOB RELATED CASE
                        //**************************************************************************************************************
                        case JSONFunctions.JOB_RELATED_TYPE:
                            Log.d("type", "job related");
                            skillsJSON = jsonFunctions.composeCommunicationJSON(skillsID, JSONFunctions.JOB_RELATED_TYPE, etSkill.getText().toString());
                            wizardCallback.insertSkillsIntoMySQL(skillsCase, JSONFunctions.JOB_RELATED_TYPE, skillsJSON);

                            break;

                        //DIGITAL COMPETENCE CASE
                        //**************************************************************************************************************
                        case JSONFunctions.DIG_COMPETENCE_TYPE:
                            Log.d("type", "digitl competence");
                            skillsJSON = jsonFunctions.composeCommunicationJSON(skillsID, JSONFunctions.DIG_COMPETENCE_TYPE, etSkill.getText().toString());
                            wizardCallback.insertSkillsIntoMySQL(skillsCase, JSONFunctions.DIG_COMPETENCE_TYPE, skillsJSON);

                            break;

                    }
                }
                break;

            case R.id.btMainNegativeButton:
                getActivity().onBackPressed();
                break;
        }
    }

}
