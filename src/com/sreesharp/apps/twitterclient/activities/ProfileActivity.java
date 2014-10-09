package com.sreesharp.apps.twitterclient.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sreesharp.apps.twitterclient.R;
import com.sreesharp.apps.twitterclient.TwitterClientApp;
import com.sreesharp.apps.twitterclient.Utils.Utility;
import com.sreesharp.apps.twitterclient.fragments.TweetsFragment;
import com.sreesharp.apps.twitterclient.models.TweetsView;
import com.sreesharp.apps.twitterclient.models.User;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		User user = (User)getIntent().getSerializableExtra("user");
		TweetsFragment tweetsFragment = (TweetsFragment) getSupportFragmentManager().findFragmentById(R.id.userTweetsFragment);
		
		if(user == null){
			String screenName = getIntent().getStringExtra("screenName");
			getUserDetails(screenName);
			tweetsFragment.setTweetsView(TweetsView.UserTimeline, screenName);
		}
		else
		{
			updateHeader(user);
			tweetsFragment.setTweetsView(TweetsView.UserTimeline);
		}
		
	}
	
	private void updateHeader(User user)
	{
		ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
		ivProfile.setBackgroundResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.getProfileImageUrl(),ivProfile);
		
		ImageView ivBackground = (ImageView) findViewById(R.id.ivBackground);
		ivBackground.setBackgroundResource(android.R.color.transparent);
		ImageLoader imageLoaderBackground = ImageLoader.getInstance();
		imageLoaderBackground.displayImage(user.getProfileBackground(),ivBackground);
		
		TextView tvDisaplyName = (TextView)findViewById(R.id.tvDisplayName);
		tvDisaplyName.setText(user.getName());
		
		TextView tvTwiiterId = (TextView)findViewById(R.id.tvTwtterId);
		tvTwiiterId.setText("@"+ user.getScreenName());
		
		TextView tvFollowing = (TextView)findViewById(R.id.tvFollowingCount);
		TextView tvFollowers = (TextView)findViewById(R.id.tvFollowersCount);
		TextView tvTweets = (TextView)findViewById(R.id.tvTweetsCount);
		
		tvFollowers.setText( Utility.kFormatter(user.getFollowersCount()));
		tvFollowing.setText( Utility.kFormatter(user.getFollowingCount()));
		tvTweets.setText( Utility.kFormatter(user.getTweetsCount()));
	}
	
	public void getUserDetails(String screenName){
		TwitterClientApp.getRestClient().getUserDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				User user = User.fromJSON(json);
				updateHeader(user);
				Log.d("DEBUG","USER>> "+json.toString());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		},screenName);
	}
}
