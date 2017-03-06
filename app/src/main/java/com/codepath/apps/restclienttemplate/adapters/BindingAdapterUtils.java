package com.codepath.apps.restclienttemplate.adapters;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by BuuPV on 3/5/2017.
 */

public class BindingAdapterUtils {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), 7, 2))
                .crossFade()
                .into(view);
    }

    @BindingAdapter({"bind:formatDate"})
    public static void formatDate(TextView view, String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
        try {
            Date date = formatter.parse(strDate);
            formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
            view.setText(formatter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @BindingAdapter({"binding:tweetImageUrl"})
    public static void tweetImage(ImageView view, String path) {
        if (path == null) {
            view.setVisibility(View.GONE);
            return;
        }
        Glide.with(view.getContext())
                .load(path)
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), 7, 2))
                .crossFade()
                .into(view);
    }
}
