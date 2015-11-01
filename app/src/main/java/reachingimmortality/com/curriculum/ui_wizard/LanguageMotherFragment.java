package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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


public class LanguageMotherFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, RecyclerClickListener.OnItemClickListener {

    //UI ELEMENTS
    private RecyclerView mRecyclerView;
    private ActionButton fabWizMother;

    //ADAPTERS & CURSORS
    private MtCursorRecyclerAdapter mtAdapter;

    //CONTROLLERS & INTERFACES
    private WizardCallback wizardCallback;
    private LayoutController layoutController;

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
        sessionManager.setEducation(true);

        //UI ELEMENTS
        TextView title = (TextView) view.findViewById(R.id.langWizTitle);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLang);
        fabWizMother = (ActionButton) view.findViewById(R.id.fabWizLang);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);

        //SET LAYOUT ELEMENTS
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.layLangTitle);
        layoutTitle.setPadding(0, layoutController.getStatusBarHeight(getActivity()), 0, 0);
        title.setText(R.string.title_skills_mother_tongues);

        btBack.setText(R.string.button_back);
        btNext.setText(R.string.button_next);

        //FAB ANIMATION
        fabWizMother.setImageResource(R.mipmap.ic_add_white);
        fabWizMother.setButtonColorRipple(android.R.color.white);
        fabWizMother.bringToFront();

        fabWizMother.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        fabWizMother.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);


        fabWizMother.playShowAnimation();
        fabWizMother.show();

        //CLICK LISTENERS
        fabWizMother.setOnClickListener(this);
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
                MyContentProvider.CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, null, null, null, null);
        mtAdapter = new MtCursorRecyclerAdapter(mlCursor);
        mRecyclerView.setAdapter(mtAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActivity().getLoaderManager().initLoader(11, null, this);
        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), this));
    }


    //LOADER MANAGER
    //************************************************************************************************************************
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mtAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mtAdapter.swapCursor(null);
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

                fabWizMother.hide();
                fabWizMother.playHideAnimation();
                wizardCallback.callMotherTongueAddDialog();

                break;

            case R.id.btMainPositiveButton:

                fabWizMother.hide();
                fabWizMother.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;

            case R.id.btMainNegativeButton:

                fabWizMother.show();
                fabWizMother.playShowAnimation();
                wizardCallback.goToEducation();

                break;
        }
    }

    //POST DELAY RUN- FAB ANIMATION
    //*******************************************************************************************************************
    public Runnable getShowRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                wizardCallback.goToOtherLanguage();
            }
        };
    }

    //RECYCLER CLICK LISTENER
    //*******************************************************************************************************************
    @Override
    public void onItemClick(View view, int position) {
        fabWizMother.hide();
        fabWizMother.playHideAnimation();
        wizardCallback.callMotherTongueEditDialog(mtAdapter.getItemId(position));
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        wizardCallback.showDeleteDialog(mtAdapter.getItemId(position), JSONFunctions.language_delete_tag, null);
    }
}
