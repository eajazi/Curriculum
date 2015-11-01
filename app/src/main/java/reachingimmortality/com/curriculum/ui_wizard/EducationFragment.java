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
import reachingimmortality.com.curriculum.adapters.CursorEducationAdapter;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;
import reachingimmortality.com.curriculum.listeners.RecyclerClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class EducationFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerClickListener.OnItemClickListener {

    //UI ELEMENTS
    private RecyclerView mRecyclerView;
    private ActionButton fabWizEdu;

    //ADAPTERS & CURSORS
    private CursorEducationAdapter mAdapter;
    private Cursor myCursor;

    //CONTROLLERS & INTERFACES
    private WizardCallback mCallback;
    private SessionManager sessionManager;
    private LayoutController layoutController;

    //CASES
    private final int CASE_FAB_EDIT = 1;
    private final int CASE_FAB_NEXT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_education, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SET SHARED PREFS
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.setProfile(false);
        sessionManager.setApplication(false);
        sessionManager.setExperience(true);

        //UI ELEMENTS
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEdu);
        fabWizEdu = (ActionButton) view.findViewById(R.id.fabWizEdu);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);

        //SET LAYOUT ELEMENTS
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layEduTitle);
        layoutTitle.setPadding(0, layoutController.getStatusBarHeight(getActivity()), 0, 0);

        btBack.setText(R.string.button_back);
        btNext.setText(R.string.button_next);

        //FAB ANIMATION
        fabWizEdu.setImageResource(R.mipmap.ic_add_white);
        fabWizEdu.setButtonColorRipple(android.R.color.white);
        fabWizEdu.bringToFront();

        fabWizEdu.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        fabWizEdu.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);


        fabWizEdu.playShowAnimation();
        fabWizEdu.show();

        //CLICK LISTENERS
        fabWizEdu.setOnClickListener(this);
        btBack.setOnClickListener(this);
        btNext.setOnClickListener(this);

        //LOAD DATA
        loadData();
    }

    //LOAD DATA
    //************************************************************************************************************************
    //************************************************************************************************************************
    private void loadData() {
        String[] projection = {MyContentProvider.EDUCATION_ID, MyContentProvider.EDUCATION_START_DATE, MyContentProvider.EDUCATION_END_DATE, MyContentProvider.EDUCATION_QUALIFICATION_NAME,
                MyContentProvider.EDUCATION_PROVIDER};
        myCursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_EDUCATION,  // The content URI of the words table
                projection,                       // The columns to return for each row
                null,                   // Either null, or the word the user entered
                null,                    // Either empty, or the string the user entered
                null);
        mAdapter = new CursorEducationAdapter(myCursor);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActivity().getLoaderManager().initLoader(2, null, this);
        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), this));
    }


    //LOADER MANAGER
    //************************************************************************************************************************
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MyContentProvider.CONTENT_URI_EDUCATION;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (WizardCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        layoutController = new LayoutController();
        sessionManager = new SessionManager(getActivity());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabWizEdu:

                fabWizEdu.hide();
                fabWizEdu.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(CASE_FAB_EDIT), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
                break;

            case R.id.btMainPositiveButton:
                fabWizEdu.hide();
                fabWizEdu.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(CASE_FAB_NEXT), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
                break;

            case R.id.btMainNegativeButton:
                fabWizEdu.hide();
                fabWizEdu.playHideAnimation();
                mCallback.goToExperience();
                break;
        }
    }

    //POST DELAY RUN- FAB ANIMATION
    //*******************************************************************************************************************
    public Runnable getShowRunnable(final int caseType) {
        return new Runnable() {
            @Override
            public void run() {
                switch (caseType) {
                    case CASE_FAB_EDIT:
                        mCallback.showEducationAddEditFragment(JSONFunctions.education_add_tag, null);

                        break;

                    case CASE_FAB_NEXT:

                        mCallback.goToMotherLanguage();

                        break;
                }
            }
        };
    }

    //RECYCLER CLICK LISTENER
    //*******************************************************************************************************************
    @Override
    public void onItemClick(View view, int position) {
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_EDUCATION + "/" + mAdapter.getItemId(position));
        mCallback.showEducationAddEditFragment(JSONFunctions.education_update_tag, uri);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        mCallback.showDeleteDialog(mAdapter.getItemId(position), JSONFunctions.education_delete_tag, null);
    }
}
