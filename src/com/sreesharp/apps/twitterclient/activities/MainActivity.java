package com.sreesharp.apps.twitterclient.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sreesharp.apps.twitterclient.R;
import com.sreesharp.apps.twitterclient.TwitterClient;
import com.sreesharp.apps.twitterclient.TwitterClientApp;
import com.sreesharp.apps.twitterclient.fragments.TweetsFragment;
import com.sreesharp.apps.twitterclient.listeners.FragmentTabListener;
import com.sreesharp.apps.twitterclient.listeners.OnProfileSelectedListener;
import com.sreesharp.apps.twitterclient.models.Tweet;
import com.sreesharp.apps.twitterclient.models.TweetsView;
import com.sreesharp.apps.twitterclient.models.User;

public class MainActivity extends FragmentActivity implements OnProfileSelectedListener {
	
	TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private User user; //Current user
	private String lastTweetId=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		client = TwitterClientApp.getRestClient();
		//lvTweets = (ListView) findViewById(R.id.lvTweets);
		//tweets = new ArrayList<Tweet>();
		//aTweets = new TwitterArrayAdapter(this,tweets);
		//lvTweets.setAdapter(aTweets);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		
			
		getUserDetails();
		
		//populateTimeline();
				
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

        Bundle args0 = new Bundle();
        args0.putSerializable("viewtype", TweetsView.HomeTimeline);
		
		Tab tab1 = actionBar
			.newTab()
			.setText("HOME")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<TweetsFragment>(R.id.flContainer, this, "HOME",
						TweetsFragment.class,args0));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Bundle args1 = new Bundle();
        args1.putSerializable("viewtype", TweetsView.Mention);
		
		Tab tab2 = actionBar
			.newTab()
			.setText("MENTIONS")
			.setIcon(R.drawable.ic_mention)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<TweetsFragment>(R.id.flContainer, this, "MENTIONS",
			    		TweetsFragment.class, args1));

		actionBar.addTab(tab2);
		
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
	
	public void onPostClick(MenuItem mi){
		Intent i = new Intent(this, PostActivity.class);
		i.putExtra("user", user);
		startActivityForResult(i, 10);
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
	public void OnProfileSelected(String screenName) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("screenName", screenName);
		startActivity(i);
		
	}
	
	public void onProfileClick(MenuItem mi)
	{
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("user", user);
		startActivity(i);
	}
}
