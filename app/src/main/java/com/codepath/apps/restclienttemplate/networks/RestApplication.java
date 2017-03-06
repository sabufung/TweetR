package com.codepath.apps.restclienttemplate.networks;

import com.codepath.apps.restclienttemplate.R;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class RestApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/helveticaneue/HelveticaNeue Medium.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);



		RestApplication.context = this;
	}

	public static RestClient getRestClient() {
		return (RestClient) RestClient.getInstance(RestClient.class, RestApplication.context);
	}
}