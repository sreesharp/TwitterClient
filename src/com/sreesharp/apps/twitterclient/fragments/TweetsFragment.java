package com.sreesharp.apps.twitterclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sreesharp.apps.twitterclient.R;
import com.sreesharp.apps.twitterclient.TwitterClient;
import com.sreesharp.apps.twitterclient.TwitterClientApp;
import com.sreesharp.apps.twitterclient.activities.LoginActivity;
import com.sreesharp.apps.twitterclient.activities.PostActivity;
import com.sreesharp.apps.twitterclient.adapters.TwitterArrayAdapter;
import com.sreesharp.apps.twitterclient.listeners.EndlessScrollListener;
import com.sreesharp.apps.twitterclient.listeners.OnProfileSelectedListener;
import com.sreesharp.apps.twitterclient.models.Tweet;
import com.sreesharp.apps.twitterclient.models.TweetsView;
import com.sreesharp.apps.twitterclient.models.User;


public class TweetsFragment extends Fragment{
	
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private User currentUser; //Current user
	private String lastTweetId=null;
	private String userName = ""; // used for fetching user timeline
	
	private TweetsView tweetsView = TweetsView.UserTimeline;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(getArguments() != null)
			tweetsView = (TweetsView) getArguments().getSerializable("viewtype");
		
		View view = inflater.inflate(R.layout.fragment_tweets, container, false);
		
		client = TwitterClientApp.getRestClient();
		lvTweets = (ListView) view.findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TwitterArrayAdapter(getActivity(),tweets);
		lvTweets.setAdapter(aTweets);
			
		getUserDetails();
		
		if(tweetsView != TweetsView.UserTimeline)
			populateTimeline();
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline();
			}
		});
		
	    return view;
	}
	
	public void setTweetsView(TweetsView view)
	{
		setTweetsView( view, "");
	}
	
	public void setTweetsView(TweetsView view, String userName)
	{
		this.tweetsView = view;
		this.userName = userName;
		refreshView();
		
	}
	
	private void refreshView()
	{
		lastTweetId = null;
		populateTimeline();
	}

	private void populateTimeline() {
		
		if(tweetsView == TweetsView.HomeTimeline)
		{
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
		else if(tweetsView == TweetsView.Mention)
		{
			client.getMentionsTimeline(new JsonHttpResponseHandler() {
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
		else if(tweetsView == TweetsView.UserTimeline)
		{
			client.getUserTimeline(new JsonHttpResponseHandler() {
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
			}, userName, lastTweetId);
		}
		
	}
	

	
	public void getUserDetails(){
		client.getUserDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				currentUser = User.fromJSON(json);
				Log.d("DEBUG","USER>> "+json.toString());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		});
	}

	
}
