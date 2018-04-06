package hanbat.isl.baeminsu.mobileproject.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 8..
 */

public class MainTabPagerAdapter extends FragmentStatePagerAdapter {

    private  final ArrayList<Fragment> mainListFragments = new ArrayList<Fragment>();
    private String tabTitle[] = new String[]{"팔아요","구해요"};

    public MainTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return mainListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mainListFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    public void addFragment(MainTab1 fragment1) {
        mainListFragments.add(fragment1);
    }

    public void addFragment(MainTab2 fragment2) {
        mainListFragments.add(fragment2);
    }


}
