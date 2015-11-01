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
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.MainActivity;
import reachingimmortality.com.curriculum.adapters.MtCursorRecyclerAdapter;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;
import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.listeners.RecyclerClickListener;


public class LanguageOtherFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, RecyclerClickListener.OnItemClickListener {

    //UI ELEMENTS
    private RecyclerView mRecyclerView;
    private ActionButton fabWizOther;

    //ADAPTERS & CURSORS
    private MtCursorRecyclerAdapter olAdapter;

    //CONTROLLERS & INTERFACES
    private WizardCallback wizardCallback;
    private LayoutController layoutController;

    //CASES
    private final int CASE_FAB_EDIT = 1;
    private final int CASE_FAB_NEXT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_language, container, false);
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
        sessionManager.setMotherLanguage(true);

        //UI ELEMENTS
        TextView title = (TextView) view.findViewById(R.id.langWizTitle);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLang);
        fabWizOther = (ActionButton) view.findViewById(R.id.fabWizLang);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);

        //SET LAYOUT ELEMENTS
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layLangTitle);
        layoutTitle.setPadding(0, layoutController.getStatusBarHeight(getActivity()), 0, 0);
        title.setText(R.string.title_skills_other_languages);

        btBack.setText(R.string.button_back);
        btNext.setText(R.string.button_next);

        //FAB ANIMATION
        fabWizOther.setImageResource(R.mipmap.ic_add_white);
        fabWizOther.setButtonColorRipple(android.R.color.white);
        fabWizOther.bringToFront();

        fabWizOther.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        fabWizOther.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);


        fabWizOther.playShowAnimation();
        fabWizOther.show();

        //CLICK LISTENERS
        fabWizOther.setOnClickListener(this);
        btBack.setOnClickListener(this);
        btNext.setOnClickListener(this);

        //LOAD DATA
        loadData();
    }

    //LOAD DATA
    //************************************************************************************************************************
    //************************************************************************************************************************
    private void loadData() {
        Cursor mlCursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, null, null, null, null);
        olAdapter = new MtCursorRecyclerAdapter(mlCursor);
        mRecyclerView.setAdapter(olAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActivity().getLoaderManager().initLoader(12, null, this);
        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), this));
    }


    //LOADER MANAGER
    //************************************************************************************************************************
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        olAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        olAdapter.swapCursor(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            wizardCallback = (WizardCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        layoutController = new LayoutController();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabWizLang:

                fabWizOther.hide();
                fabWizOther.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(CASE_FAB_EDIT), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;

            case R.id.btMainPositiveButton:

                fabWizOther.hide();
                fabWizOther.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(CASE_FAB_NEXT), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;

            case R.id.btMainNegativeButton:

                fabWizOther.hide();
                fabWizOther.playHideAnimation();
                wizardCallback.goToMotherLanguage();

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
                        wizardCallback.showEvaluationAddEditFragment(JSONFunctions.evaluation_add_tag, null);
                        break;

                    case CASE_FAB_NEXT:
                        wizardCallback.goToSkills();

                        break;
                }
            }
        };
    }

    //RECYCLER CLICK LISTENER
    //*******************************************************************************************************************
    @Override
    public void onItemClick(View view, int position) {
        fabWizOther.hide();
        fabWizOther.playHideAnimation();
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_LANGUAGE + "/" +olAdapter.getItemId(position));
        wizardCallback.showEvaluationAddEditFragment(JSONFunctions.evaluation_update_tag, uri);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        wizardCallback.showDeleteDialog(olAdapter.getItemId(position),JSONFunctions.language_delete_tag, null);
    }
}
