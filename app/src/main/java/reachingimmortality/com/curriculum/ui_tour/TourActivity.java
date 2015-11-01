package reachingimmortality.com.curriculum.ui_tour;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.adapters.ParallaxPagerAdapter;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.interfaces.TourCallback;
import reachingimmortality.com.curriculum.libs.CircleIndicator;
import reachingimmortality.com.curriculum.libs.ParallaxPageTransformer;
import reachingimmortality.com.curriculum.ui_login.LoginActivity;

public class TourActivity extends AppCompatActivity implements TourCallback {

    //IMAGE TAGS
    public static final String IMG_BGROUND_TAG = "img_bgroung";
    public static final String IMG_LOGO_TAG = "img_logo";
    public static final String IMG_TXT_ABOVE_TAG = "img_txt_above";
    public static final String IMG_TXT_BELOW_TAG = "img_txt_below";

    public static final String TAG_IS_LAST = "isLast";

    //List of fragments for viewpager
    List<Fragment> fragments = new Vector<Fragment>();
    //Controllers
    private SessionManager sharedPref;
    private Stack<Fragment> fragmentStack;
    //UI elements
    private ViewPager mViewPager;
    private CircleIndicator circleIndicator;
    //Adapters
    private ParallaxPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        //SET TRANSPARENT STATUS BAR FOR KITKAT

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);




        fragmentStack = new Stack<>();
        sharedPref = new SessionManager(this);

        mViewPager = (ViewPager) findViewById(R.id.vpTour);
        circleIndicator = (CircleIndicator) findViewById(R.id.circleIndTour);

        //TOUR 1 - OUTSIDE
        Bundle bFirst = new Bundle();
        bFirst.putInt(IMG_BGROUND_TAG, R.mipmap.bground_tour_story);
        bFirst.putInt(IMG_TXT_ABOVE_TAG, R.mipmap.txt_img_tour_welcome);
        //bFirst.putInt(IMG_LOGO_TAG, R.mipmap.logo_with_black_shadow);
        bFirst.putInt(IMG_TXT_BELOW_TAG, R.mipmap.txt_img_tour_create);
        bFirst.putBoolean(TAG_IS_LAST, false);
        fragments.add(Fragment.instantiate(getApplicationContext(), ParallaxFragment.class.getName(), bFirst));

        //TOUR 2 - SELPHONE
        Bundle bSecond = new Bundle();
        bSecond.putInt(IMG_BGROUND_TAG, R.mipmap.bground_tour_buddy);
        bSecond.putInt(IMG_TXT_BELOW_TAG, R.mipmap.txt_img_tour_buddy);
        bSecond.putBoolean(TAG_IS_LAST, false);
        fragments.add(Fragment.instantiate(getApplicationContext(), ParallaxFragment.class.getName(), bSecond));

        //TOUR 3 - COFFE
        Bundle bThird = new Bundle();
        bThird.putInt(IMG_BGROUND_TAG, R.mipmap.bground_tour_coffe);
        bThird.putInt(IMG_TXT_ABOVE_TAG, R.mipmap.txt_img_tour_coffe);
        bThird.putBoolean(TAG_IS_LAST, false);
        fragments.add(Fragment.instantiate(getApplicationContext(), ParallaxFragment.class.getName(), bThird));

        //TOUR 4 - STORY
        Bundle bFourth = new Bundle();
        bFourth.putInt(IMG_BGROUND_TAG, R.mipmap.bground_tour_nest);
        bFourth.putInt(IMG_TXT_BELOW_TAG, R.mipmap.txt_img_tour_story);
        bFourth.putBoolean(TAG_IS_LAST, true);
        fragments.add(Fragment.instantiate(getApplicationContext(), ParallaxFragment.class.getName(), bFourth));

        //Setting adapter
        mAdapter = new ParallaxPagerAdapter(getFragmentManager(),fragments);
        mViewPager.setAdapter(mAdapter);
        circleIndicator.setViewPager(mViewPager);

        ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.img_background, 2, 2));

        mViewPager.setPageTransformer(true, pageTransformer);

    }





    @Override
    public void onBackPressed() {

        if (fragmentStack.size() == 2) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            fragmentStack.lastElement().onPause();
            ft.remove(fragmentStack.pop());
            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void goToLogin() {
        //Saving to shared prefrences
        sharedPref.setTour(true);
        //Starting activity Login
        finish();
        startActivity(new Intent(getApplicationContext(),
                LoginActivity.class));
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
