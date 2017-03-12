package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.fragment.HomeFragment;
import com.codepath.apps.restclienttemplate.fragment.MentionTimelineFragment;
import com.codepath.apps.restclienttemplate.fragment.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;

/**
 * Created by BuuPV on 3/12/2017.
 */

public class ProfilePageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"TWEETS", "PHOTOS", "FAVORITE"};
    private Context context;
    private User user;

    public ProfilePageAdapter(FragmentManager fm, Context context, User user) {
        super(fm);
        this.context = context;
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserTimelineFragment.newInstance(user);
            case 1:
                return HomeFragment.newInstance();
            case 2:
                return HomeFragment.newInstance();
            default:
                return HomeFragment.newInstance();
        }

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
