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

import reachingimmortality.com.curriculum.adapters.items.CursorEducationItemAdapter;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.MainCallback;
import reachingimmortality.com.curriculum.libs.SimpleDividerItemDecoration;
import reachingimmortality.com.curriculum.listeners.RecyclerClickListener;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class EducationMainFragment extends Fragment implements  View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,RecyclerClickListener.OnItemClickListener {
    //UI ELEMENTS
    private RecyclerView recyclerViewEdu;
    private ActionButton fabEduMain;

    //ADAPTERS
    private CursorEducationItemAdapter mAdapter;

    //CONTROLLERS & INTERFACES
    private MainCallback mainCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_education_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //INIT UI ELEMENTS
        recyclerViewEdu = (RecyclerView) view.findViewById(R.id.eduMainRecycler);

        //FAB ANIMATIONS
        fabEduMain = (ActionButton) getActivity().findViewById(R.id.fabMain);
        fabEduMain.setImageResource(R.mipmap.ic_add_white);
        fabEduMain.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabEduMain.setButtonColorRipple(android.R.color.white);

        fabEduMain.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabEduMain.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabEduMain.playShowAnimation();
        fabEduMain.show();

        //FILL DATA
        fillData();

        //INIT LISTENERS
        fabEduMain.setOnClickListener(this);
    }

    private void fillData() {
        String[] projection = {MyContentProvider.EDUCATION_ID,MyContentProvider.EDUCATION_QUALIFICATION_NAME,
                MyContentProvider.EDUCATION_PROVIDER,MyContentProvider.EDUCATION_START_DATE,
                MyContentProvider.EDUCATION_END_DATE};
        Cursor myCursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_EDUCATION,
                projection,null,null,null);

        mAdapter = new CursorEducationItemAdapter(myCursor);
        recyclerViewEdu.setAdapter(mAdapter);
        recyclerViewEdu.setHasFixedSize(true);
        recyclerViewEdu.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEdu.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        getActivity().getLoaderManager().initLoader(2, null, this);
        recyclerViewEdu.addOnItemTouchListener(new RecyclerClickListener(getActivity(), this));
    }


    //LOADER MANAGER
    //******************************************************************************************************************
    //******************************************************************************************************************
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MyContentProvider.CONTENT_URI_EDUCATION;
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
                fabEduMain.hide();
                fabEduMain.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(null), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_EDUCATION + "/" + mAdapter.getItemId(position));

        fabEduMain.hide();
        fabEduMain.playHideAnimation();
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
                goToEduEdit(uri);
            }
        };

    }

    private void goToEduEdit(Uri itemUri) {
        if(itemUri == null){
            ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(new EducationMainEditFragment(),
                    getActivity().getResources().getString(R.string.title_education_edit));
        }else{
            ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(EducationMainEditFragment.newInstance(itemUri),
                    getActivity().getResources().getString(R.string.title_education_edit));
        }

    }
}
