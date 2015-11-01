package reachingimmortality.com.curriculum;


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


import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.adapters.items.CursorExperienceItemAdapter;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.MainCallback;
import reachingimmortality.com.curriculum.libs.SimpleDividerItemDecoration;
import reachingimmortality.com.curriculum.listeners.RecyclerClickListener;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExperienceMainFragment extends Fragment implements  View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,RecyclerClickListener.OnItemClickListener {
    //UI ELEMENTS
    private RecyclerView recyclerViewExp;
    private ActionButton fabExpMain;
    //ADAPTERS
    private CursorExperienceItemAdapter mAdapter;
    //CONTROLLERS & INTERFACES
    private MainCallback mainCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experience_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //INIT UI ELEMENTS
        recyclerViewExp = (RecyclerView) view.findViewById(R.id.expMainRecycler);

        //FAB ANIMATIONS
        fabExpMain = (ActionButton) getActivity().findViewById(R.id.fabMain);
        fabExpMain.setImageResource(R.mipmap.ic_add_white);
        fabExpMain.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabExpMain.setButtonColorRipple(android.R.color.white);

        fabExpMain.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabExpMain.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabExpMain.playShowAnimation();
        fabExpMain.show();
        //FILL DATA
        fillData();

        //INIT LISTENERS
        fabExpMain.setOnClickListener(this);
    }

    private void fillData() {
        String[] projection = {MyContentProvider.WORK_EXPERIENCE_ID,MyContentProvider.WORK_EXPERIENCE_EMPLOYER,
                MyContentProvider.WORK_EXPERIENCE_POSITION,MyContentProvider.WORK_EXPERIENCE_START_DATE,
                MyContentProvider.WORK_EXPERIENCE_END_DATE};

        Cursor myCursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_WORK_EXPERIENCE,
                projection,null,null,null);

        mAdapter = new CursorExperienceItemAdapter(myCursor);
        recyclerViewExp.setAdapter(mAdapter);
        recyclerViewExp.setHasFixedSize(true);
        recyclerViewExp.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewExp.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        getActivity().getLoaderManager().initLoader(1, null, this);
        recyclerViewExp.addOnItemTouchListener(new RecyclerClickListener(getActivity(),this));
    }


    //LOADER MANAGER
    //******************************************************************************************************************
    //******************************************************************************************************************
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabMain:
                fabExpMain.hide();
                fabExpMain.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(null), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_WORK_EXPERIENCE + "/" + mAdapter.getItemId(position));

        fabExpMain.hide();
        fabExpMain.playHideAnimation();
        new Handler().postDelayed(getShowRunnable(uri), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        //mainCallback.showDeleteDialog(mAdapter.getItemId(position),JSONFunctions.experience_delete_tag, null);
    }

    private Runnable getShowRunnable(final Uri uri) {
        return new Runnable() {
            @Override
            public void run() {
                goToExpEdit(uri);
            }
        };

    }

    private void goToExpEdit(Uri itemUri) {
        if(itemUri == null){
            ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(new ExperienceMainEditFragment(),
                    getActivity().getResources().getString(R.string.title_experience_edit));
        }else{
            ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(ExperienceMainEditFragment.newInstance(itemUri),
                    getActivity().getResources().getString(R.string.title_experience_edit));
        }

    }

}
