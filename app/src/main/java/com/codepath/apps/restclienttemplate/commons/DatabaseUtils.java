package com.codepath.apps.restclienttemplate.commons;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

/**
 * Created by BuuPV on 3/6/2017.
 */

public class DatabaseUtils {
    public static void clearDB(){
        Tweet.clearTable();
        User.clearTable();
    }
}
