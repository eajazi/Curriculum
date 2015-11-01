package reachingimmortality.com.curriculum;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.dialogs.MotherTongDialog;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.LanguageParentListener;
import reachingimmortality.com.curriculum.interfaces.MainCallback;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class LanguageMainFragment extends Fragment implements MotherTongDialog.DialogMotherCallback, LanguageParentListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final int LOADER_MOTHER_TONGUE = 5;
    private static final int LOADER_OTHER_LANG = 6;

    //UI ELEMENTS
    private ActionButton fabLangMain;

    //CONTROLLERS & INTERFACES
    private MainCallback mainCallback;
    private JSONFunctions jsonFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS INIT
        viewPager = (ViewPager) view.findViewById(R.id.langMainViewPager);
        //FAB ANIMATIONS
        fabLangMain = (ActionButton) getActivity().findViewById(R.id.fabMain);
        fabLangMain.setImageResource(R.mipmap.ic_add_white);
        fabLangMain.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabLangMain.setButtonColorRipple(android.R.color.white);

        fabLangMain.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabLangMain.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabLangMain.playShowAnimation();
        fabLangMain.show();

        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.langMainTabs);
        tabLayout.setupWithViewPager(viewPager);

        //SET CLICK LISTENERS
        fabLangMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabLangMain.hide();
                fabLangMain.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(MainActivity.CASE_MOTHER_TONG, 0), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
            }
        });
    }



    public void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MotherTongMainFragment(), getString(R.string.title_skills_mother_tongues));
        adapter.addFragment(new OtherLangMainFragment(), getString(R.string.title_skills_other_languages));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fabLangMain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fabLangMain.hide();
                                fabLangMain.playHideAnimation();
                                new Handler().postDelayed(getShowRunnable(MainActivity.CASE_MOTHER_TONG, 0), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                            }
                        });
                        break;
                    case 1:
                        fabLangMain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fabLangMain.hide();
                                fabLangMain.playHideAnimation();
                                new Handler().postDelayed(getShowRunnable(MainActivity.CASE_OTHER_LANG, 0), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    //CALLBACKS
    //********************************************************************************************************************
    private Runnable getShowRunnable(final int skillCase, final long itemId) {
        return new Runnable() {
            @Override
            public void run() {
                switch (skillCase){
                    case  MainActivity.CASE_MOTHER_TONG:
                        if(itemId != 0){
                            showMotherDialog(String.valueOf(itemId));
                        }else{
                            showMotherDialog(null);
                        }

                        break;
                    case MainActivity.CASE_OTHER_LANG:
                        showEvaluationFragment();
                        break;
                }

            }
        };
    }

    @Override
    public void onMotherPositiveClick(String languageCase, String jsonLanguageList) {
        mainCallback.insertLanguage(languageCase, jsonLanguageList, null, fabLangMain);
    }

    @Override
    public void onMotherNegativeClick(){
        fabLangMain.playShowAnimation();
        fabLangMain.show();
    }

    private void showMotherDialog(String itemId) {
        if(itemId == null){
            MotherTongDialog motherTongDialog = MotherTongDialog.newInstance(null);
            motherTongDialog.setTargetFragment(this, 0);
            motherTongDialog.show(getFragmentManager(), null);
        }else{
            Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE +"/"+itemId);
            MotherTongDialog motherTongDialog = MotherTongDialog.newInstance(uri);
            motherTongDialog.setTargetFragment(this, 0);
            motherTongDialog.show(getFragmentManager(), null);
        }
    }

    private void showEvaluationFragment() {
        ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(new OtherLangMainEvaluationFragment(),
                getActivity().getResources().getString(R.string.title_skills_evaluation));
    }


    @Override
    public void callMotherTongueEditDialog(long itemId) {
        fabLangMain.hide();
        fabLangMain.playHideAnimation();
        new Handler().postDelayed(getShowRunnable(MainActivity.CASE_MOTHER_TONG, itemId), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

    }

    @Override
    public void showDeleteDialog(long itemId, String deleteCase, String skillType) {
        fabLangMain.hide();
        fabLangMain.playHideAnimation();
        mainCallback.showDeleteDialog(itemId, deleteCase, null);
    }



}
