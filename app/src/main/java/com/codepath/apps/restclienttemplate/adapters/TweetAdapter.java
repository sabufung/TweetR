package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetItemClickListener;
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.activities.TweetDetailActivity;
import com.codepath.apps.restclienttemplate.models.Medium;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.networks.RestApplication;
import com.codepath.apps.restclienttemplate.networks.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.R.id.list;
import static java.lang.System.load;
import static jp.co.cyberagent.android.gpuimage.GPUImageRenderer.NO_IMAGE;

/**
 * Created by BuuPV on 3/5/2017.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tweet> listTweet;
    private Context context;
    private TweetItemClickListener listener;

    public TweetAdapter(List<Tweet> listTweet, TweetItemClickListener listener) {

        this.listTweet = listTweet;
        this.listener = listener;
    }

    public final int NO_IMAGE = 0, IMAGE = 1, GIF = 2, VIDEO = 3;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.timeline_tweet, parent, false);

        // Return a new holder instance
        final TweetViewHolder viewHolder = new TweetViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TweetViewHolder viewHolder = (TweetViewHolder) holder;
        configureTweetViewHolder(viewHolder, position);
    }

    private void configureTweetViewHolder(final TweetViewHolder viewHolder, int position) {
        final Tweet tweet = (Tweet) listTweet.get(position);
        if (tweet != null) {
            viewHolder.sbFavorite.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    RestClient client = RestApplication.getRestClient();
                    if (buttonState) {
                        client.createFavorite(tweet.getIdStr(), new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Tweet responseTweet = new Tweet(response);
                                viewHolder.tvFavorite.setTextColor(context.getResources().getColor(R.color.favorite_active));
                                viewHolder.tvFavorite.setText(String.valueOf(responseTweet.getFavoriteCount()));
                            }
                        });
                    } else {
                        client.destroyFavorite(tweet.getIdStr(), new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Tweet responseTweet = new Tweet(response);
                                viewHolder.tvFavorite.setText(String.valueOf(responseTweet.getFavoriteCount()));
                            }

                        });
                    }
                }
            });
            if (tweet.isFavorited()){
                viewHolder.sbFavorite.setChecked(true);
            }
            if (tweet.isRetweeted()){
                viewHolder.sbRetweet.setChecked(true);
            }
            viewHolder.tvName.setText(tweet.getUser().getName());
            viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
            viewHolder.tvText.setText(tweet.getText());
            viewHolder.tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));
            viewHolder.tvFavorite.setText(String.valueOf(tweet.getFavoriteCount()));
            viewHolder.tvDuration.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
            loadImage(viewHolder.ivAvatar, tweet.getUser().getProfileImageUrl());
            if (tweet.getEntities() != null){
                if (tweet.getEntities().getMedia() != null) {
                    if (tweet.getEntities().getMedia().size() > 0) {
                        viewHolder.ivThumbnail.setVisibility(View.VISIBLE);
                        loadImage(viewHolder.ivThumbnail, tweet.getEntities().getMedia().get(0).getMediaUrl());
                    } else {
                        viewHolder.ivThumbnail.setVisibility(View.INVISIBLE);
                    }
                } else {
                    viewHolder.ivThumbnail.setVisibility(View.INVISIBLE);
                }
            }
            viewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("USER", Parcels.wrap(tweet.getUser()));
                    context.startActivity(i);
                }
            });
        }
    }

    public void loadImage(ImageView view, String path) {
        Glide.with(context)
                .load(path)
                .bitmapTransform(new RoundedCornersTransformation(context, 7, 2))
                .crossFade()
                .into(view);
    }

    @Override
    public int getItemCount() {
        return listTweet.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;
        @BindView(R.id.tvScreenName)
        TextView tvScreenName;
        @BindView(R.id.tvDuration)
        TextView tvDuration;
        @BindView(R.id.tvText)
        TextView tvText;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.sbFavorite)
        SparkButton sbFavorite;
        @BindView(R.id.sbRetweet)
        SparkButton sbRetweet;
        @BindView(R.id.tvRetweet)
        TextView tvRetweet;
        @BindView(R.id.tvFavorite)
        TextView tvFavorite;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
        }

    }

    public void add(List<Tweet> tweets) {
        listTweet.addAll(tweets);
        this.notifyDataSetChanged();
    }

    public void clear() {
        listTweet.clear();
        this.notifyDataSetChanged();
    }

    public void append(int position, Tweet tweet) {
        listTweet.add(position, tweet);
        this.notifyItemInserted(position);
        this.notifyDataSetChanged();
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
