package com.codepath.apps.restclienttemplate.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetItemClickListener;
import com.codepath.apps.restclienttemplate.activities.TweetDetailActivity;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.commons.DatabaseUtils;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networks.RestApplication;
import com.codepath.apps.restclienttemplate.networks.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MentionTimelineFragment extends Fragment {
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.rvTimeline)
    RecyclerView rvTimeline;
    TweetAdapter mAdapter;
    List<Tweet> tweetList;
    EndlessRecyclerViewScrollListener mScrollListener;
    User mCurrentUser;
    boolean isOffline;
    RestClient client = RestApplication.getRestClient();
    LinearLayoutManager linearLayoutManager;
    public final int TWEET_POST_CODE = 200;

    public MentionTimelineFragment() {
        // Required empty public constructor
    }

    public static MentionTimelineFragment newInstance() {
        MentionTimelineFragment fragment = new MentionTimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        tweetList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvTimeline.setLayoutManager(linearLayoutManager);

        mAdapter = new TweetAdapter(tweetList, new TweetItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i = new Intent(getContext(), TweetDetailActivity.class);
                i.putExtra("TWEET", Parcels.wrap(tweetList.get(position)));
                startActivity(i);
            }
        });
        rvTimeline.setAdapter(mAdapter);

        rvTimeline.addItemDecoration(new DividerItemDecoration(rvTimeline.getContext(), DividerItemDecoration.VERTICAL));

        mScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchMentionTweet(page, false);
            }
        };
        rvTimeline.addOnScrollListener(mScrollListener);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMentionTweet(1, true);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);




        DatabaseUtils.clearDB();

        fetchMentionTweet(1, true);
        return view;
    }

    private void fetchMentionTweet(int page, final boolean isRefresh) {
        client.getMentionTimeline(page, new JsonHttpResponseHandler() {
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
