package com.sreesharp.apps.twitterclient.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
	
	
	private static final long serialVersionUID = 1530081805073592309L;
	private String name;
	private long uid;
	private String screenName;
	private String profileImageUrl;
	private long followersCount;
	private long followingCount;
	private long tweetsCount;
	private String profileBackground;
	
	public long getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(long followersCount) {
		this.followersCount = followersCount;
	}

	public long getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(long followingCount) {
		this.followingCount = followingCount;
	}

	public long getTweetsCount() {
		return tweetsCount;
	}

	public void setTweetsCount(long tweetsCount) {
		this.tweetsCount = tweetsCount;
	}

	public static User fromJSON(JSONObject json)
	{
		User user = new User();
		try{
			user.name = json.getString("name");
			user.uid = json.getLong("id");
			user.screenName = json.getString("screen_name");
			user.profileImageUrl = json.getString("profile_image_url");
			user.followingCount = json.getLong("friends_count");
			user.followersCount = json.getLong("followers_count");
			user.tweetsCount = json.getLong("statuses_count");
			user.profileBackground = json.getString("profile_background_image_url");
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
			return null;
		}
		return user;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getProfileBackground() {
		return profileBackground;
	}

	public void setProfileBackground(String profileBackground) {
		this.profileBackground = profileBackground;
	}
}
