package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.HomePagerAdapter;
import com.codepath.apps.restclienttemplate.adapters.ProfilePageAdapter;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.vpPager)
    ViewPager viewPager;
    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvScreenName)
    TextView tvScreenName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvFriendCount)
    TextView tvFriendCount;
    @BindView(R.id.tvFollowerCount)
    TextView tvFollowerCount;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    User mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentUser = Parcels.unwrap(getIntent().getParcelableExtra("USER"));
        getSupportActionBar().setTitle(mCurrentUser.getName());
        loadImage(ivAvatar, mCurrentUser.getProfileImageUrl());
        loadImage(ivCover, mCurrentUser.getProfileBackgroundImageUrl());
        tvName.setText(mCurrentUser.getName());
        tvScreenName.setText("@" + mCurrentUser.getScreenName());
        tvFollowerCount.setText(String.valueOf(mCurrentUser.getFollowersCount()));
        tvFriendCount.setText(String.valueOf(mCurrentUser.getFriendsCount()));
        viewPager.setAdapter(new ProfilePageAdapter(getSupportFragmentManager(),
                ProfileActivity.this,mCurrentUser));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void loadImage(ImageView view, String path) {
        Glide.with(getBaseContext())
                .load(path)
                .fitCenter()
                .bitmapTransform(new RoundedCornersTransformation(getBaseContext(), 7, 2))
                .crossFade()
                .into(view);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
