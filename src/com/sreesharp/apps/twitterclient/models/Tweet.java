package com.sreesharp.apps.twitterclient.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sreesharp.apps.twitterclient.Utils.Utility;

import android.text.format.DateUtils;

public class Tweet {
	private String body;
	private long uid;
	private String createAt;
	private User user;
	
	public static Tweet fromJSON(JSONObject json)
	{
		Tweet tweet = new Tweet();
		try
		{
			tweet.body = json.getString("text");
			tweet.uid = json.getLong("id");
			tweet.createAt = Utility.getRelativeTimeAgo(json.getString("created_at"));
			tweet.user = User.fromJSON (json.getJSONObject("user"));
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray)
	{
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for(int i=0; i < jsonArray.length(); i++)
		{
			JSONObject json = null;
			try {
				json = jsonArray.getJSONObject(i);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			Tweet tweet = fromJSON(json);
			if( tweets != null)
				tweets.add(tweet);
		}
		return tweets;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreateAt() {
		return createAt;
	}

	public User getUser() {
		return user;
	}
	
	
	
	
}
