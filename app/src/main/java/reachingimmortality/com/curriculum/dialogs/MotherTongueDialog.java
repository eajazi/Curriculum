package reachingimmortality.com.curriculum.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;
import reachingimmortality.com.curriculum.libs.InstantAutoComplete;


public class MotherTongueDialog extends DialogFragment {
    //Interfaces
    private WizardCallback wizardCallback;
    //UI elements
    private InstantAutoComplete acLanguage;
    private View positiveAction;
    //Variables
    private Uri motherTongueUri;
    private Cursor mCursor;
    //CONTROLLERS
    private JSONFunctions jsonFunctions;

    public static MotherTongueDialog newInstance(boolean darkTheme, int accentColor) {
        MotherTongueDialog dialog = new MotherTongueDialog();
        Bundle args = new Bundle();
        args.putBoolean("dark_theme", darkTheme);
        args.putInt("accent_color", accentColor);
        dialog.setArguments(args);
        return dialog;
    }

    public static MotherTongueDialog newInstance(boolean darkTheme, int accentColor, Uri uri) {
        MotherTongueDialog dialog = new MotherTongueDialog();
        Bundle args = new Bundle();
        args.putBoolean("dark_theme", darkTheme);
        args.putInt("accent_color", accentColor);
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_MOTHER_TONGUE, uri);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;
        try{
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_mother_tongue, null);
            acLanguage = (InstantAutoComplete) customView.findViewById(R.id.acMotherTongue);
            motherTongueUri = getArguments().getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_MOTHER_TONGUE);
            if(motherTongueUri != null){
                fillData();
            }

        } catch (InflateException e) {
            throw new IllegalStateException("Cannot inflate custom view.");
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(getArguments().getBoolean("dark_theme") ? Theme.DARK : Theme.LIGHT)
                .title(R.string.title_skills_mother_tongues)
                .customView(customView, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        String languageID;
                        String languageCase;
                        if(motherTongueUri != null){
                            languageCase = JSONFunctions.language_update_tag;
                            languageID = motherTongueUri.getPathSegments().get(1);
                        }else{
                            languageCase = JSONFunctions.language_add_tag;
                            languageID = null;
                        }

                        String jsonLanguageList = jsonFunctions.composeLanguageJSON(languageID,
                                acLanguage.getText().toString(),
                                getResources().getString(R.string.type_native_lang));
                        Log.d("",""+acLanguage.getText().toString()+"  "+languageID+" "+getResources().getString(R.string.type_native_lang));
                        wizardCallback.insertLanguageIntoMYSQL(languageCase, jsonLanguageList, null);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        //closing dialog
                        dismiss();
                    }
                }).build();
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        acLanguage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        positiveAction.setEnabled(false); // disabled by default
        return dialog;
    }

    private void fillData() {
        mCursor = getActivity().getContentResolver().query(
                motherTongueUri,
                null,
                null,
                null,
                null);
        if (mCursor.moveToFirst()) {
            acLanguage.setText(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.LANGUAGE_NAME)));
        }
        mCursor.close();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        wizardCallback = (WizardCallback) activity;
        jsonFunctions = new JSONFunctions();
    }

}
