package reachingimmortality.com.curriculum;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsMainFragment extends Fragment implements View.OnClickListener {

    //UI ELEMENTS
    private TextView tvCommunication, tvManagament, tvJobRelated, tvDigComp;

    public SkillsMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_skills_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS
        //TextViews
        tvCommunication = (TextView) view.findViewById(R.id.skillMainTvCommunication);
        tvManagament = (TextView) view.findViewById(R.id.skillMainTvManagament);
        tvJobRelated = (TextView) view.findViewById(R.id.skillMainTvJobRelated);
        tvDigComp = (TextView) view.findViewById(R.id.skillMainTvDigComp);

        //Buttons
        Button btCommEdit = (Button) view.findViewById(R.id.skillMainCommEdit);
        Button btManEdit = (Button) view.findViewById(R.id.skillMainManEdit);
        Button btJobEdit = (Button) view.findViewById(R.id.skillMainJobEdit);
        Button btDigEdit = (Button) view.findViewById(R.id.skillMainDigEdit);

        //LOAD DATA
        loadSkillData();

        //CLICK LISTENERS
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
            case R.id.skillMainCommEdit:
                ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(SkillsMainEditFragment.newInstance(JSONFunctions.COMMUNICATION_TYPE,
                                skillItemUri), getResources().getString(R.string.title_skills_communication));
                break;
            case R.id.skillMainManEdit:
                ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(SkillsMainEditFragment.newInstance(JSONFunctions.MANAGAMENT_TYPE,
                        skillItemUri), getResources().getString(R.string.title_skills_managament));
                break;
            case R.id.skillMainJobEdit:
                ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(SkillsMainEditFragment.newInstance(JSONFunctions.JOB_RELATED_TYPE,
                        skillItemUri), getResources().getString(R.string.title_skills_job_related));
                break;
            case R.id.skillMainDigEdit:
                ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(SkillsMainEditFragment.newInstance(JSONFunctions.DIG_COMPETENCE_TYPE,
                        skillItemUri), getResources().getString(R.string.title_skills_dig_competence));
                break;
        }
    }
}
