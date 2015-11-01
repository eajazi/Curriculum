package reachingimmortality.com.curriculum;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.adapters.MtCursorRecyclerAdapter;
import reachingimmortality.com.curriculum.controllers.MyLinearLayoutManager;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.LanguageParentListener;
import reachingimmortality.com.curriculum.libs.SimpleDividerItemDecoration;
import reachingimmortality.com.curriculum.listeners.RecyclerClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MotherTongMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,RecyclerClickListener.OnItemClickListener {

    //UI ELEMENTS
    private RecyclerView rvMotherTong;
    private ActionButton fabMotherMain;
    //ADAPTERS
    private MtCursorRecyclerAdapter mtAdapter;
    //CONTROLLERS & INTERFACES
    private LanguageParentListener languageParentListener;
    private JSONFunctions jsonFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mother_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS INIT
        rvMotherTong = (RecyclerView) view.findViewById(R.id.motherTongMainRecyc);
        //FAB ANIMATIONS
        fabMotherMain = (ActionButton) getActivity().findViewById(R.id.fabMain);

        fabMotherMain.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabMotherMain.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        //FILL DATA
        fillRecyclers();

        //SET CLICK LISTENERS
        rvMotherTong.addOnItemTouchListener(new RecyclerClickListener(getActivity(), this));
    }

    private void fillRecyclers() {
        //MOTHER TONGUE(s)
        Cursor mlCursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, null, null, null, null);

        if (mlCursor != null) {
            mtAdapter = new MtCursorRecyclerAdapter(mlCursor);
            rvMotherTong.setAdapter(mtAdapter);
        }

        rvMotherTong.setHasFixedSize(true);
        rvMotherTong.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        rvMotherTong.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        getActivity().getLoaderManager().initLoader(8, null, this);
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


    //LOADER MANAGER
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

    //RECYCLER CLICK LISTENER
    @Override
    public void onItemClick(View view, int position) {
        languageParentListener.callMotherTongueEditDialog(mtAdapter.getItemId(position));
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        languageParentListener.showDeleteDialog(mtAdapter.getItemId(position), JSONFunctions.language_delete_tag, null);
    }



}
