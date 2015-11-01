package reachingimmortality.com.curriculum.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.List;


/**
 * Created by Reaching Immortality on 10.7.2015..
 */
public class ParallaxPagerAdapter extends FragmentStatePagerAdapter {

    public static int position = 0;

    private List<Fragment> myFragments;

    public ParallaxPagerAdapter(FragmentManager fragmentManager, List<Fragment> myFrags) {
        super(fragmentManager);
        myFragments = myFrags;
    }


    @Override
    public Fragment getItem(int position) {

        return myFragments.get(position);

    }

    @Override
    public int getCount() {

        return myFragments.size();
    }


    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        position = position;
    }


}

