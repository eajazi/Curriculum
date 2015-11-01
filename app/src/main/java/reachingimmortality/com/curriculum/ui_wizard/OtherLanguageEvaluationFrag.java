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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.adapters.CustomCursorAdapterEvaluation;
import reachingimmortality.com.curriculum.adapters.NothingSelectedSpinnerAdapter;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.database_library.SQLController;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherLanguageEvaluationFrag extends Fragment implements View.OnClickListener {
    //UI ELEMENTS
    private Spinner spListening, spReading, spInteraction, spProduction, spWriting;
    private EditText etLanguage;
    private ActionButton fabEvalMain;

    //ADAPTERS
    private CustomCursorAdapterEvaluation listeningAdapter, readingAdapter, interactionAdapter, productionAdapter, writingAdapter;

    //Uri-es
    private Uri otherLangItemUri;

    //CURSORS
    private Cursor olCursor, listenCursor, readCursor, interactionCursor, productionCursor, writeCursor;

    //CONTROLLERS & INTERFACES
    private SQLController sqlController;
    private JSONFunctions jsonFunctions;
    private WizardCallback wizardCallback;
    private LayoutController layoutController;

    public static OtherLanguageEvaluationFrag newInstance(Uri uri) {
        OtherLanguageEvaluationFrag fragment = new OtherLanguageEvaluationFrag();
        Bundle args = new Bundle();
        args.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_OTHER_LANGUAGE, uri);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_other_language_evaluation, container, false);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            wizardCallback = (WizardCallback) activity;
        }catch (Exception ex){ex.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS INIT
        //Text views
        TextView title = (TextView) view.findViewById(R.id.evalWizardTitle);
        //Spinners
        spListening = (Spinner) view.findViewById(R.id.evalWizardSpListening);
        spReading = (Spinner) view.findViewById(R.id.evalWizardSpReading);
        spInteraction = (Spinner) view.findViewById(R.id.evalWizardSpInteraction);
        spProduction = (Spinner) view.findViewById(R.id.evalWizardSpProduction);
        spWriting = (Spinner) view.findViewById(R.id.evalWizardSpWriting);

        //Edit texts
        etLanguage = (EditText) view.findViewById(R.id.evalWizardEtLanguage);

        //Buttons
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);
        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);

        //SET LAYOUT ELEMENTS
        LinearLayout layoutTitle = (LinearLayout) view.findViewById(R.id.evalWizardLayTitle);
        layoutController = new LayoutController();
        layoutTitle.setPadding(0, layoutController.getStatusBarHeight(getActivity()), 0, 0);
        title.setText(R.string.title_skills_evaluation);

        btBack.setText(R.string.button_cancel);
        btNext.setText(R.string.button_ok);

        //Load data
        loadSpinnerData();

        Bundle args = getArguments();
        if (args != null) {
            otherLangItemUri = args.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_OTHER_LANGUAGE);
            loadEvaluationData();
        }

        //CLICK LISTENERS
        btBack.setOnClickListener(this);
        btNext.setOnClickListener(this);
    }

    //SPINNERS DATA
    //********************************************************************************************************************
    private void loadSpinnerData() {
        //LISTENING
        listeningAdapter = new CustomCursorAdapterEvaluation(getActivity(),
                R.layout.spinner_evaluation_selected,
                getEvCursor(new String[]{String.valueOf(getResources().getString(R.string.listening))}),
                new String[]{MyContentProvider.EVALUATION_VIEW_LEVEL, MyContentProvider.EVALUATION_VIEW_DESCRIPTION},
                new int[]{R.id.evalMainLevel, R.id.evalMainLevelDescription},
                true);

        listeningAdapter.setDropDownViewResource(R.layout.spinner_evaluation_dropdown);

        //READING
        readingAdapter = new CustomCursorAdapterEvaluation(getActivity(),
                R.layout.spinner_evaluation_selected,
                getEvCursor(new String[]{String.valueOf(getResources().getString(R.string.reading))}),
                new String[]{MyContentProvider.EVALUATION_VIEW_LEVEL, MyContentProvider.EVALUATION_VIEW_DESCRIPTION},
                new int[]{R.id.evalMainLevel, R.id.evalMainLevelDescription},
                true);

        readingAdapter.setDropDownViewResource(R.layout.spinner_evaluation_dropdown);

        //INTERACTION
        interactionAdapter = new CustomCursorAdapterEvaluation(getActivity(),
                R.layout.spinner_evaluation_selected,
                getEvCursor(new String[]{String.valueOf(getResources().getString(R.string.spoken_interaction))}),
                new String[]{MyContentProvider.EVALUATION_VIEW_LEVEL, MyContentProvider.EVALUATION_VIEW_DESCRIPTION},
                new int[]{R.id.evalMainLevel, R.id.evalMainLevelDescription},
                true);

        interactionAdapter.setDropDownViewResource(R.layout.spinner_evaluation_dropdown);

        //PRODUCTION
        productionAdapter = new CustomCursorAdapterEvaluation(getActivity(),
                R.layout.spinner_evaluation_selected,
                getEvCursor(new String[]{String.valueOf(getResources().getString(R.string.spoken_production))}),
                new String[]{MyContentProvider.EVALUATION_VIEW_LEVEL, MyContentProvider.EVALUATION_VIEW_DESCRIPTION},
                new int[]{R.id.evalMainLevel, R.id.evalMainLevelDescription},
                true);

        productionAdapter.setDropDownViewResource(R.layout.spinner_evaluation_dropdown);

        //WRITING
        writingAdapter = new CustomCursorAdapterEvaluation(getActivity(),
                R.layout.spinner_evaluation_selected,
                getEvCursor(new String[]{String.valueOf(getResources().getString(R.string.writing))}),
                new String[]{MyContentProvider.EVALUATION_VIEW_LEVEL, MyContentProvider.EVALUATION_VIEW_DESCRIPTION},
                new int[]{R.id.evalMainLevel, R.id.evalMainLevelDescription},
                true);

        writingAdapter.setDropDownViewResource(R.layout.spinner_evaluation_dropdown);

        //Spinners evauation
        spListening.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        listeningAdapter,
                        R.layout.spinner_evaluation_prompt_dark,
                        getActivity()));

        spReading.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        readingAdapter,
                        R.layout.spinner_evaluation_prompt_dark,
                        getActivity()));

        spInteraction.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        interactionAdapter,
                        R.layout.spinner_evaluation_prompt_dark,
                        getActivity()));

        spProduction.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        productionAdapter,
                        R.layout.spinner_evaluation_prompt_dark,
                        getActivity()));

        spWriting.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        writingAdapter,
                        R.layout.spinner_evaluation_prompt_dark,
                        getActivity()));
    }

    private Cursor getEvCursor(String[] selectArgs) {
        String mSelectionClause = MyContentProvider.EVALUATION_VIEW_SUB_ID + " = ?";
        return getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATION
                , null, mSelectionClause, selectArgs, null);
    }
    //SAVE DATA
    //*************************************************************************************************************************
    //*************************************************************************************************************************
    private void saveData() {

        //INSERTING / UPDATE LANGUAGE TABLE
        //*************************************************************************************************************************
        //*************************************************************************************************************************
        ArrayList<HashMap<String,String>> evaluationList = new ArrayList<>();
        jsonFunctions = new JSONFunctions();
        //LANGUAGE
        //*************************************************************************************************************************
        String languageCase;
        String languageID;

        if(otherLangItemUri != null){
            languageCase = JSONFunctions.language_update_tag;
            languageID = otherLangItemUri.getPathSegments().get(1);
        }else{
            languageCase = JSONFunctions.language_add_tag;
            languageID = null;
        }

        String jsonLanguageList = jsonFunctions.composeLanguageJSON(
                languageID,
                etLanguage.getText().toString(),
                getResources().getString(R.string.type_other_lang));

        //EVALUATION
        //*************************************************************************************************************************
        String evaluationCase = null;
        String langEvaluationID = null;

        //GET LISTENING ASSESSMENT EVALUATION
        //*************************************************************************************************************************
        if(spListening.getSelectedItemId()>0){

            if(otherLangItemUri != null){

                listenCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                        MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                        new String[]{getResources().getString(R.string.listening), languageID},
                        null);

                if(listenCursor.moveToFirst()){
                    langEvaluationID = listenCursor.getString(listenCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ID));
                    evaluationCase = JSONFunctions.evaluation_update_tag;
                }else{
                    langEvaluationID = null;
                    evaluationCase = JSONFunctions.evaluation_add_tag;
                }
            } else { evaluationCase = JSONFunctions.evaluation_add_tag; }
            //HashMap evaluation
            HashMap<String,String> evaluation = new HashMap<>();

            evaluation.put(JSONFunctions.JSON_CASE_TAG, evaluationCase);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ID, langEvaluationID);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ASSESSMENT__ID, String.valueOf(spListening.getSelectedItemId()));

            evaluationList.add(evaluation);
        }

        //GET READING ASSESSMENT EVALUATION
        //*************************************************************************************************************************
        if(spReading.getSelectedItemId()>0) {

            if (otherLangItemUri != null) {

                listenCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                        MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK + " = ?",
                        new String[]{getResources().getString(R.string.reading), languageID},
                        null);

                if (listenCursor.moveToFirst()) {
                    langEvaluationID = listenCursor.getString(listenCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ID));
                    evaluationCase = JSONFunctions.evaluation_update_tag;
                } else {
                    langEvaluationID = null;
                    evaluationCase = JSONFunctions.evaluation_add_tag;
                }
            } else { evaluationCase = JSONFunctions.evaluation_add_tag; }
            //HashMap evaluation
            HashMap<String, String> evaluation = new HashMap<>();

            evaluation.put(JSONFunctions.JSON_CASE_TAG, evaluationCase);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ID, langEvaluationID);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ASSESSMENT__ID, String.valueOf(spReading.getSelectedItemId()));

            evaluationList.add(evaluation);
        }

        //GET INTERACTION ASSESSMENT EVALUATION
        //*************************************************************************************************************************
        if(spInteraction.getSelectedItemId()>0){

            if(otherLangItemUri != null){

                listenCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                        MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                        new String[]{getResources().getString(R.string.spoken_interaction), languageID},
                        null);

                if(listenCursor.moveToFirst()){
                    langEvaluationID = listenCursor.getString(listenCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ID));
                    evaluationCase = JSONFunctions.evaluation_update_tag;
                }else{
                    langEvaluationID = null;
                    evaluationCase = JSONFunctions.evaluation_add_tag;
                }
            } else { evaluationCase = JSONFunctions.evaluation_add_tag; }
            //HashMap evaluation
            HashMap<String,String> evaluation = new HashMap<>();

            evaluation.put(JSONFunctions.JSON_CASE_TAG, evaluationCase);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ID, langEvaluationID);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ASSESSMENT__ID, String.valueOf(spInteraction.getSelectedItemId()));

            evaluationList.add(evaluation);
        }

        //GET PRODUCTION ASSESSMENT EVALUATION
        //*************************************************************************************************************************
        if(spProduction.getSelectedItemId()>0){

            if(otherLangItemUri != null){

                listenCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                        MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                        new String[]{getResources().getString(R.string.spoken_production), languageID},
                        null);

                if(listenCursor.moveToFirst()){
                    langEvaluationID = listenCursor.getString(listenCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ID));
                    evaluationCase = JSONFunctions.evaluation_update_tag;
                }else{
                    langEvaluationID = null;
                    evaluationCase = JSONFunctions.evaluation_add_tag;
                }
            } else { evaluationCase = JSONFunctions.evaluation_add_tag; }

            //HashMap evaluation
            HashMap<String,String> evaluation = new HashMap<>();

            evaluation.put(JSONFunctions.JSON_CASE_TAG, evaluationCase);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ID, langEvaluationID);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ASSESSMENT__ID, String.valueOf(spProduction.getSelectedItemId()));

            evaluationList.add(evaluation);
        }

        //GET WRITING ASSESSMENT EVALUATION
        //*************************************************************************************************************************
        if(spWriting.getSelectedItemId()>0){

            if(otherLangItemUri != null){

                listenCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                        MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                        new String[]{getResources().getString(R.string.writing), languageID},
                        null);

                if(listenCursor.moveToFirst()){
                    langEvaluationID = listenCursor.getString(listenCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ID));
                    evaluationCase = JSONFunctions.evaluation_update_tag;
                }else{
                    langEvaluationID = null;
                    evaluationCase = JSONFunctions.evaluation_add_tag;
                }
            } else { evaluationCase = JSONFunctions.evaluation_add_tag; }

            //HashMap evaluation
            HashMap<String,String> evaluation = new HashMap<>();
            evaluation.put(JSONFunctions.JSON_CASE_TAG, evaluationCase);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ID, langEvaluationID);
            evaluation.put(JSONFunctions.LANGUAGE_EVALUATION_ASSESSMENT__ID, String.valueOf(spWriting.getSelectedItemId()));

            evaluationList.add(evaluation);

        }

        String jsonEvaluationList = jsonFunctions.composeEvaluationJSON(evaluationList);
        Log.d("arrayList evaluation", "" + evaluationList);

        wizardCallback.insertLanguageIntoMYSQL(languageCase, jsonLanguageList, jsonEvaluationList);
    }

    //EVALUATION DATA
    //********************************************************************************************************************
    public void loadEvaluationData(){

        String languageID = otherLangItemUri.getPathSegments().get(1);

        olCursor = getActivity().getContentResolver().query(otherLangItemUri, null, null, null, null);
        if (olCursor.moveToFirst()) {
            etLanguage.setText(olCursor.getString(olCursor.getColumnIndex(MyContentProvider.LANGUAGE_NAME)));
        }
        olCursor.close();

        //Listening cursor
        listenCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                new String[]{getResources().getString(R.string.listening), languageID},
                null);

        if (listenCursor.moveToFirst()) {
            int level = listenCursor.getInt(listenCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ASSESSMENT_ID));
            spListening.setSelection(getPositionById(spListening, level));
        }


        //Reading cursor
        readCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                new String[]{getResources().getString(R.string.reading), languageID},
                null);

        if (readCursor.moveToFirst()) {
            int level = readCursor.getInt(readCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ASSESSMENT_ID));
            spReading.setSelection(getPositionById(spReading, level));
        }
        readCursor.close();

        //Interaction cursor
        interactionCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                new String[]{getResources().getString(R.string.spoken_interaction), languageID},
                null);

        if (interactionCursor.moveToFirst()) {
            int level = interactionCursor.getInt(interactionCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ASSESSMENT_ID));
            spInteraction.setSelection(getPositionById(spInteraction, level));
        }
        interactionCursor.close();

        //Production cursor
        productionCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                new String[]{getResources().getString(R.string.spoken_production), languageID},
                null);

        if (productionCursor.moveToFirst()) {
            int level = productionCursor.getInt(productionCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ASSESSMENT_ID));
            spProduction.setSelection(getPositionById(spProduction, level));
        }
        productionCursor.close();

        //Writing cursor
        writeCursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_EVALUATED_LANGUAGE, null,
                MyContentProvider.LANG_EVALUATION_SUB_TYPE + " = ? and " + MyContentProvider.LANGUAGE_EVALUATION_LANG_FK +" = ?",
                new String[]{getResources().getString(R.string.writing), languageID},
                null);

        if (writeCursor.moveToFirst()) {
            int level = writeCursor.getInt(writeCursor.getColumnIndex(MyContentProvider.LANG_EVALUATION_ASSESSMENT_ID));
            spWriting.setSelection(getPositionById(spWriting, level));
            writeCursor.close();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btMainPositiveButton:
                saveData();
                break;

            case R.id.btMainNegativeButton:
                getActivity().onBackPressed();
                break;
        }
    }

    //GET SPINNER POSITION BY ID
    //********************************************************************************************************************************
    private static int getPositionById(Spinner spinner, int itemID) {

        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            long itemId = spinner.getItemIdAtPosition(i);
            if (itemId == itemID) {
                index = i;
                break;
            }
        }
        return index;
    }
}
