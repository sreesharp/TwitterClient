package com.sreesharp.apps.twitterclient.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sreesharp.apps.twitterclient.R;
import com.sreesharp.apps.twitterclient.TwitterClient;
import com.sreesharp.apps.twitterclient.TwitterClientApp;
import com.sreesharp.apps.twitterclient.models.User;

public class PostActivity extends Activity {
	
	private User user;
	private ImageView ivProfileImage;
	private TextView tvName;
	private TextView tvScreenName;
	private EditText etCompose;
	private Button btTweet;
	private TwitterClient client;
	private TextView tvCharCount;
	private int totalCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		
		client = TwitterClientApp.getRestClient();
		totalCount = 140;
		user = (User) getIntent().getSerializableExtra("user");
		
		
		ivProfileImage = (ImageView) findViewById(R.id.ivComposeProfileImage);
		tvName = (TextView) findViewById(R.id.tvComposeUserName);
		tvScreenName = (TextView) findViewById(R.id.tvComposeScreenName);
		etCompose = (EditText) findViewById(R.id.etCompose);
		btTweet = (Button) findViewById(R.id.btTweet);
		tvCharCount = (TextView) findViewById(R.id.tvCharCount);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
		tvName.setText(user.getName());
		tvScreenName.setText("@"+user.getScreenName());
		tvCharCount.setText(totalCount+"");
	}
	
	public void postTweet(String status){
		client.postTweet(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				Log.d("DEBUG","USER>> POSTED!!!");
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		}, status);
	}
	
	public void onPost(View v) {
		String tweetText = etCompose.getText().toString();
		if(tweetText.isEmpty()){
			Toast.makeText(getBaseContext(), "Compose and Tweet", Toast.LENGTH_SHORT).show();
		}
		else{
			postTweet(tweetText);
			Intent i = new Intent();
			setResult(RESULT_OK,i);
			finish();
		}
	}
}
