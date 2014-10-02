package com.sreesharp.apps.twitterclient.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sreesharp.apps.twitterclient.EndlessScrollListener;
import com.sreesharp.apps.twitterclient.R;
import com.sreesharp.apps.twitterclient.TwitterClient;
import com.sreesharp.apps.twitterclient.TwitterClientApp;
import com.sreesharp.apps.twitterclient.adapters.TwitterArrayAdapter;
import com.sreesharp.apps.twitterclient.models.Tweet;
import com.sreesharp.apps.twitterclient.models.User;


public class TimelineActivity extends Activity {
	
	TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private User user; //Current user
	private String lastTweetId=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		client = TwitterClientApp.getRestClient();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TwitterArrayAdapter(this,tweets);
		lvTweets.setAdapter(aTweets);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		
		getUserDetails();
		
		populateTimeline();
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline();
			}
		});
	}

	private void populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll( Tweet.fromJSONArray(json));
				lastTweetId = tweets.get(tweets.size()-1).getId();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug",e.toString());
				Log.d("debug",s.toString());
			}
		}, lastTweetId);
		
	}
	

	
	public void getUserDetails(){
		client.getUserDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				user = User.fromJSON(json);
				Log.d("DEBUG","USER>> "+json.toString());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void onLogout(MenuItem mi){
		client.clearAccessToken();
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
		finish();
	}
	
	public void onPost(MenuItem mi){
		Intent i = new Intent(this, PostActivity.class);
		i.putExtra("user", user);
		startActivityForResult(i, 10);
	}
	
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode ==10){
    		if(resultCode == RESULT_OK){
    			tweets.clear();
            	lastTweetId = null;
            	lvTweets.invalidate();
    			populateTimeline();
    		}
    	}
    }
}
