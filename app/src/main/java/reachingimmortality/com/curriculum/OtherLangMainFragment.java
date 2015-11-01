package reachingimmortality.com.curriculum;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reachingimmortality.com.curriculum.adapters.MtCursorRecyclerAdapter;
import reachingimmortality.com.curriculum.controllers.MyLinearLayoutManager;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.LanguageParentListener;
import reachingimmortality.com.curriculum.libs.SimpleDividerItemDecoration;
import reachingimmortality.com.curriculum.listeners.RecyclerClickListener;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherLangMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //UI ELEMENTS
    private RecyclerView rvOtherLang;
    //ADAPTERS
    private MtCursorRecyclerAdapter mtAdapter;
    //CONTROLLERS & INTERFACES
    private LanguageParentListener languageParentListener;
    private JSONFunctions jsonFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_lang_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS INIT
        rvOtherLang = (RecyclerView) view.findViewById(R.id.otherLangMainRecyc);

        //FILL DATA
        fillRecyclers();


    }

    private void fillRecyclers() {
        //MOTHER TONGUE(s)
        Cursor olCursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, null, null, null, null);

        if (olCursor != null) {
            mtAdapter = new MtCursorRecyclerAdapter(olCursor);
            rvOtherLang.setAdapter(mtAdapter);
        }

        rvOtherLang.setHasFixedSize(true);
        rvOtherLang.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        rvOtherLang.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()));

        //SET CLICK LISTENERS
        rvOtherLang.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_LANGUAGE + "/" +mtAdapter.getItemId(position));
                showEvaluationEdit(uri);
            }

            @Override
            public void onItemLongPress(View childView, int position) {
                languageParentListener.showDeleteDialog(mtAdapter.getItemId(position),JSONFunctions.language_delete_tag, null);
            }
        }));

        getActivity().getLoaderManager().initLoader(9, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            languageParentListener = (LanguageParentListener) getParentFragment();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //SHOW EVALUATION EDIT FRAGMENT
    private void showEvaluationEdit(Uri evaluationUri) {
        ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(OtherLangMainEvaluationFragment.newInstance(evaluationUri),
                getActivity().getResources().getString(R.string.title_skills_evaluation));
    }

    //LOADER MANAGER
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mtAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mtAdapter.swapCursor(null);
    }


}
