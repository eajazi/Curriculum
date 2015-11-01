package reachingimmortality.com.curriculum;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;
import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.MainCallback;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationMainFragment extends Fragment implements View.OnClickListener {

    //UI ELEMENTS
    private TextView tvType, tvDescr;
    private MainCallback mainCallback;
    private ActionButton fabAppMain;

    public ApplicationMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS INIT
        tvType = (TextView) view.findViewById(R.id.tvAppMainType);
        tvDescr = (TextView) view.findViewById(R.id.tvAppMainDescr);

        //FAB ANIMATIONS
        fabAppMain = (ActionButton) getActivity().findViewById(R.id.fabMain);
        fabAppMain.setImageResource(R.mipmap.ic_edit_white);
        fabAppMain.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabAppMain.setButtonColorRipple(android.R.color.white);

        fabAppMain.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabAppMain.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabAppMain.playShowAnimation();
        fabAppMain.show();

        //INIT LISTENERS
        fabAppMain.setOnClickListener(this);

        //CHECK FOR DATA
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_VIEW_USER_APPLICATION,null,null,null,null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                String appType = cursor.getString(cursor.getColumnIndex(MyContentProvider.APPLICATION_TYPE_NAME));
                String appDescr = cursor.getString(cursor.getColumnIndex(MyContentProvider.APPLICATION_DESCRIPTION));
                //APP TYPE
                if(appType.equals("")){
                    tvType.setText(getResources().getString(R.string.empty_content));
                }else{
                    tvType.setText(appType);
                }

                //APP DESCRIPTION
                if(appDescr.equals("")){
                    tvDescr.setText(getResources().getString(R.string.empty_content));
                }else{
                    tvDescr.setText(appDescr);
                }

            }else{
                tvType.setText(getResources().getString(R.string.empty_content));
                tvDescr.setText(getResources().getString(R.string.empty_content));
            }
        }else{
            tvType.setText(getResources().getString(R.string.empty_content));
            tvDescr.setText(getResources().getString(R.string.empty_content));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mainCallback = (MainCallback) activity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabMain:
                fabAppMain.hide();
                fabAppMain.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;
        }
    }
    private Runnable getShowRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                goToAppEdit();
            }
        };

    }

    private void goToAppEdit() {
        ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(new ApplicationMainEditFragment(),
                getActivity().getResources().getString(R.string.title_application_edit));

    }
}
