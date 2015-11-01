package reachingimmortality.com.curriculum.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.libs.InstantAutoComplete;
import reachingimmortality.com.curriculum.ui_wizard.WizardActivity;

/**
 * Created by ReachingIm on 12.10.2015..
 */
public class MotherTongDialog extends DialogFragment {

    //UI elements
    private InstantAutoComplete acLanguage;
    //Variables
    private Uri motherTongueUri;
    //CONTROLLERS
    private JSONFunctions jsonFunctions;
    private DialogMotherCallback callback;

    public static MotherTongDialog newInstance(Uri uri) {
        MotherTongDialog dialog = new MotherTongDialog();

        Bundle args = new Bundle();
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_MOTHER_TONGUE, uri);

        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (DialogMotherCallback) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;
        try {
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_mother_tongue, null);
            acLanguage = (InstantAutoComplete) customView.findViewById(R.id.acMotherTongue);
            if(getArguments() != null) {
                motherTongueUri = getArguments().getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_MOTHER_TONGUE);
            }
            if (motherTongueUri != null) {
                fillData();
            }

        } catch (InflateException e) {
            throw new IllegalStateException("Cannot inflate custom view.");
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.title_skills_mother_tongues)
                .customView(customView, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .positiveColorRes(R.color.material_dialog_buttons)
                .negativeColorRes(R.color.material_dialog_buttons)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String languageID;
                        String languageCase;
                        jsonFunctions = new JSONFunctions();
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

                        callback.onMotherPositiveClick(languageCase, jsonLanguageList);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        callback.onMotherNegativeClick();
                    }
                })
                .build();

        return dialog;
    }

    private void fillData() {
        Cursor mCursor = getActivity().getContentResolver().query(
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


    //INTERFACE DIALOG
    public interface DialogMotherCallback{
        void onMotherPositiveClick(String languageCase, String jsonLanguageList);

        void onMotherNegativeClick();
    }
}

