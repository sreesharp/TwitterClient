package com.sreesharp.apps.twitterclient.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sreesharp.apps.twitterclient.R;
import com.sreesharp.apps.twitterclient.models.Tweet;

public class TwitterArrayAdapter extends ArrayAdapter<Tweet> {

	public TwitterArrayAdapter(Context context, 
			List<Tweet> tweets) {
		super(context, R.layout.tweet_item, tweets);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		Tweet tweet = getItem(position);
		if(convertView == null)
		{
			LayoutInflater inflator = LayoutInflater.from(getContext());
			view = inflator.inflate(R.layout.tweet_item, parent, false);
		}
		else
		{
			view = convertView;
		}
		ImageView ivProfile = (ImageView) view.findViewById(R.id.ivProfileImg);
		TextView tvUserName = (TextView)view.findViewById(R.id.tvUserName);
		TextView tvScreenName = (TextView)view.findViewById(R.id.tvUserId);
		TextView tvbody = (TextView)view.findViewById(R.id.tvBody);
		TextView tvTime = (TextView)view.findViewById(R.id.tvTime);
		
		ivProfile.setBackgroundResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(),ivProfile);
		
		tvUserName.setText(tweet.getUser().getName());
		tvScreenName.setText("@"+tweet.getUser().getScreenName());
		
		tvbody.setText(Html.fromHtml(tweet.getBody()));
		
		tvTime.setText(tweet.getCreateAt());
		
		return view;
	}
	
	

}
