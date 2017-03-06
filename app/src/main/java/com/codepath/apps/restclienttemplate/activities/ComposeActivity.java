package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.R.attr.path;
import static com.codepath.apps.restclienttemplate.R.id.etContent;
import static com.codepath.apps.restclienttemplate.R.id.ibClose;
import static com.codepath.apps.restclienttemplate.R.id.ivAvatar;
import static com.codepath.apps.restclienttemplate.R.id.swipeContainer;
import static com.codepath.apps.restclienttemplate.R.id.tvLimit;
import static com.codepath.apps.restclienttemplate.R.id.tvScreenName;

public class ComposeActivity extends BaseActivity {
    @BindView(R.id.btnTweet)
    Button btnTweet;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.ibClose)
    ImageView ibClose;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvScreenName)
    TextView tvScreenName;

    User mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);
        Intent i = getIntent();
        mCurrentUser = Parcels.unwrap(i.getParcelableExtra("USER"));
        tvName.setText(mCurrentUser.getName());
        tvScreenName.setText("@" + mCurrentUser.getScreenName());

        Glide.with(getBaseContext())
                .load(mCurrentUser.getProfileImageUrl())
                .fitCenter()
                .bitmapTransform(new RoundedCornersTransformation(getBaseContext(), 7, 2))
                .crossFade()
                .into(ivAvatar);

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeActivity.this.finish();
            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = etContent.getText().toString();
                client.postTweet(content, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            Intent data = new Intent();
                            Tweet tweet = new Tweet(response);
                            data.putExtra("BODY", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data);
                            ComposeActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        Toast.makeText(ComposeActivity.this, "error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int remaining = 140 - s.toString().length();
                tvLimit.setText("" + remaining);

                if (remaining > 0) {
                    tvLimit.setTextColor(Color.BLACK);

                } else {
                    tvLimit.setTextColor(Color.GRAY);
                }
            }
        });

    }

    public void postTweet(String body) {

    }
}
