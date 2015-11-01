package reachingimmortality.com.curriculum;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.adapters.NothingSelectedSpinnerAdapter;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.MainCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationMainEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    //UI ELEMENTS
    private Spinner appSpinner;
    private MaterialEditText etDescr;
    private ActionButton fabAppMainEdit;

    //ADAPTERS
    private SimpleCursorAdapter mAdapter;

    //CONTROLLERS & INTERFACES
    private JSONFunctions jsonFunctions;
    private MainCallback mainCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_main_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //INIT UI ELEMENTS
        appSpinner = (Spinner) view.findViewById(R.id.appMainEditSpinnerType);
        etDescr = (MaterialEditText) view.findViewById(R.id.appMainEditDescription);
        fabAppMainEdit = (ActionButton) getActivity().findViewById(R.id.fabMain);

        //FAB ANIMATION
        fabAppMainEdit = (ActionButton) getActivity().findViewById(R.id.fabMain);

        fabAppMainEdit.setImageResource(R.mipmap.ic_checked_white);
        fabAppMainEdit.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabAppMainEdit.setButtonColorRipple(android.R.color.white);

        fabAppMainEdit.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabAppMainEdit.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabAppMainEdit.hide();
        fabAppMainEdit.playShowAnimation();
        fabAppMainEdit.show();

        //LOAD DATA
        loadSpinnerData();
        loadDataFromSQLite();

        //LISTENERS
        fabAppMainEdit.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mainCallback = (MainCallback) activity;
        }catch (Exception ex){ex.printStackTrace();}
        jsonFunctions = new JSONFunctions();
    }

    private void loadDataFromSQLite() {
        //CHECK IF THERE'S DATA IN SQLITE
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_USER_APPLICATION,null,null,null,null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                appSpinner.setSelection(getIndex(appSpinner,
                        cursor.getString(cursor.getColumnIndex(MyContentProvider.APPLICATION_TYPE_NAME))));
                etDescr.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.APPLICATION_DESCRIPTION)));
            }
            cursor.close();
        }
    }

    private void loadSpinnerData() {
        Boolean isThereData = null;
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_USER_APPLICATION,
                null, null, null, null);
        isThereData = cursor != null && cursor.moveToFirst();

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.spinner_item_row_selected_fill,
                null,
                new String[]{MyContentProvider.APPLICATION_TYPE_NAME},
                new int[]{R.id.spinnerDropdownItem}, 0);

        mAdapter.setDropDownViewResource(R.layout.spinner_item_row_dropdown);

        appSpinner.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        if (isThereData) {
            appSpinner.setAdapter(mAdapter);

            getActivity().getLoaderManager().initLoader(0, null, this);
        } else {
            appSpinner.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            mAdapter,
                            R.layout.spinner_item_row_prompt,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            getActivity()));
            getActivity().getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MyContentProvider.CONTENT_URI_APPLICATION_TYPE;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabMain:

                fabAppMainEdit.hide();
                fabAppMainEdit.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;
        }
    }

    private Runnable getShowRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                String applicationID;
                String userApplicationID;
                String applicationCASE;

                Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_USER_APPLICATION,
                        new String[]{MyContentProvider.USER_APPLICATION_APPLICATION_FK,MyContentProvider.APPLICATION_ID},null,null,null);
                if(cursor != null) {
                    if (cursor.moveToFirst()) {
                        applicationCASE = JSONFunctions.application_update_tag;
                        applicationID = cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_APPLICATION_APPLICATION_FK));
                        userApplicationID = cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_APPLICATION_ID));
                    }else {
                        applicationCASE = JSONFunctions.application_add_tag;
                        applicationID = null;
                        userApplicationID = null;
                    }cursor.close();
                }else{
                    applicationCASE = JSONFunctions.application_add_tag;
                    applicationID = null;
                    userApplicationID = null;
                }

                String jsonApplicationList = jsonFunctions.composeApplicationList(
                        applicationID,
                        etDescr.getText().toString(),
                        String.valueOf(appSpinner.getSelectedItemId()),
                        userApplicationID);

                mainCallback.saveApplicationData(applicationCASE, jsonApplicationList);
            }
        };
    }

    private int getIndex(Spinner spinner, String string) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(string)){
                index = i;
            }
        }
        return index;
    }

}
