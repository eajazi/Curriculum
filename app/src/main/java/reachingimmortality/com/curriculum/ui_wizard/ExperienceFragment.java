package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.MainActivity;
import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.adapters.CursorExperienceAdapter;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;
import reachingimmortality.com.curriculum.listeners.RecyclerClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExperienceFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,RecyclerClickListener.OnItemClickListener {

    //UI ELEMENTS
    private RecyclerView mRecyclerView;
    private ActionButton fabWizExp;

    //ADAPTERS & CURSORS
    private CursorExperienceAdapter mAdapter;
    private Cursor myCursor;

    //CONTROLLERS & INTERFACES
    private WizardCallback mCallback;
    private LayoutController layoutController;

    //CASES
    private final int CASE_FAB_EDIT = 1;
    private final int CASE_FAB_NEXT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_experience, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SET SHARED PREFS
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.setProfile(false);
        sessionManager.setApplication(true);

        //UI ELEMENTS
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewExp);
        fabWizExp = (ActionButton) view.findViewById(R.id.fabWizExp);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);

        //SET TITLE BELOW STATUS BAR
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layExpTitle);
        layoutTitle.setPadding(0, layoutController.getStatusBarHeight(getActivity()), 0, 0);

        btBack.setText(R.string.button_back);
        btNext.setText(R.string.button_next);

        //FAB ANIMATION
        fabWizExp.setImageResource(R.mipmap.ic_add_white);
        fabWizExp.setButtonColorRipple(android.R.color.white);
        fabWizExp.bringToFront();

        fabWizExp.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        fabWizExp.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);


        fabWizExp.playShowAnimation();
        fabWizExp.show();

        //RECYCLER VIEW
        loadData();

        //CLICK LISTENERS
        fabWizExp.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btBack.setOnClickListener(this);
    }

    //LOAD DATA
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    private void loadData() {
        String[] projection = {MyContentProvider.EDUCATION_ID,MyContentProvider.WORK_EXPERIENCE_EMPLOYER,
                MyContentProvider.WORK_EXPERIENCE_POSITION,MyContentProvider.WORK_EXPERIENCE_START_DATE,
                MyContentProvider.WORK_EXPERIENCE_END_DATE};
        myCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE, projection,
                null, null, null);
        mAdapter = new CursorExperienceAdapter(myCursor);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActivity().getLoaderManager().initLoader(1, null, this);
        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(),this));
    }

    //CLICK LISTENERS
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fabWizExp:
                fabWizExp.hide();
                fabWizExp.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(CASE_FAB_EDIT), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
                break;

            case R.id.btMainPositiveButton:
                fabWizExp.hide();
                fabWizExp.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(CASE_FAB_NEXT), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
                break;

            case R.id.btMainNegativeButton:
                fabWizExp.hide();
                fabWizExp.playHideAnimation();
                mCallback.goToApplication();
        }
    }

    //RECYCLER CLICK LISTENER
    //*******************************************************************************************************************
    //*******************************************************************************************************************
    @Override
    public void onItemClick(View view, int position) {
        fabWizExp.hide();
        fabWizExp.playHideAnimation();
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE + "/" + mAdapter.getItemId(position));
        mCallback.showExperienceAddEditFragment(JSONFunctions.experience_update_tag, uri);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        fabWizExp.hide();
        fabWizExp.playHideAnimation();
        mCallback.showDeleteDialog(mAdapter.getItemId(position), JSONFunctions.experience_delete_tag, null);
    }

    //POST DELAY RUN- FAB ANIMATION
    //*******************************************************************************************************************
    public Runnable getShowRunnable(final int caseType) {
        return new Runnable() {
            @Override
            public void run() {
                switch (caseType){
                    case CASE_FAB_EDIT:
                        mCallback.showExperienceAddEditFragment(JSONFunctions.experience_add_tag, null);
                        break;

                    case CASE_FAB_NEXT:
                        mCallback.goToEducation();
                        break;
                }
            }
        };
    }

    //LOADER MANAGER
    //*******************************************************************************************************************
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MyContentProvider.CONTENT_URI_WORK_EXPERIENCE;
        return new CursorLoader(getActivity(),uri,null,null,null,null);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (WizardCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        layoutController = new LayoutController();
    }

}
