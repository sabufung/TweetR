package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.util.Util;
import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetItemClickListener;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.commons.DatabaseUtils;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.Tweet_Table;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networks.RestApplication;
import com.codepath.apps.restclienttemplate.networks.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends BaseActivity {
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.rvTimeline)
    RecyclerView rvTimeline;
    @BindView(R.id.fabCompose)
    FloatingActionButton fabCompose;
    LinearLayoutManager linearLayoutManager;
    TweetAdapter mAdapter;
    List<Tweet> tweetList;
    EndlessRecyclerViewScrollListener mScrollListener;
    User mCurrentUser;
    boolean isOffline;
    public final int TWEET_POST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);


        tweetList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        rvTimeline.setLayoutManager(linearLayoutManager);

        mAdapter = new TweetAdapter(tweetList, new TweetItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i = new Intent(getBaseContext(), TweetDetailActivity.class);
                i.putExtra("TWEET", Parcels.wrap(tweetList.get(position)));
                startActivity(i);
            }
        });
        rvTimeline.setAdapter(mAdapter);

        rvTimeline.addItemDecoration(new DividerItemDecoration(rvTimeline.getContext(), DividerItemDecoration.VERTICAL));

        mScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchTweet(page, false);
            }
        };
        rvTimeline.addOnScrollListener(mScrollListener);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTweet(1, true);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                i.putExtra("USER", Parcels.wrap(mCurrentUser));
                startActivityForResult(i, TWEET_POST_CODE);
            }
        });


        if (isNetworkAvailable()) {
            if (isOnline()) {
                DatabaseUtils.clearDB();
                client.getCurrentUser(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        mCurrentUser = new User(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        throwable.printStackTrace();
                    }
                });
                fetchTweet(1, true);
            }
        } else {
            isOffline = true;
            mAdapter.clear();
            List<Tweet> dbTweets = (ArrayList<Tweet>) SQLite.select().from(Tweet.class).queryList();
            mAdapter.add(dbTweets);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TWEET_POST_CODE) {
            if (resultCode == RESULT_OK) {
                mAdapter.append(0, (Tweet) Parcels.unwrap(data.getParcelableExtra("BODY")));
            }
        }
    }

    public void fetchTweet(int page, final boolean isRefresh) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (statusCode == 200) {

                    swipeContainer.setRefreshing(false);
                    if (isRefresh) {
                        mAdapter.clear();
                    }
                    ArrayList<Tweet> tweets = Tweet.fromJson(jsonArray);
                    mAdapter.add(tweets);
                    Log.d("DEBUG", "timeline: " + jsonArray.toString());
                    Log.d("BUU", tweets.size() + "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                swipeContainer.setRefreshing(false);
                Log.d("BUU", "LOi cmnr");
            }
        });
    }
}
