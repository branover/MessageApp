package overall.brandon.messageapp;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private ArrayList<CharSequence> tabTitles = new ArrayList<CharSequence>();

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabTitles.add("Registration");
        this.tabTitles.add("Peers List");

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new RegisterFragment();
            case 1:
                return new PeersFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}