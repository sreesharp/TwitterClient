package com.sreesharp.apps.twitterclient.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.res.Resources;
import android.text.format.DateUtils;

public class Utility {
	
    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;
    /**
     * This constant is actually the length of 364 days, not of a year!
     */
    public static final long YEAR_IN_MILLIS = WEEK_IN_MILLIS * 52;

    public static String kFormatter(long num) {
    	if(num < 999)
    		return String.format("%d", num); 
    	else if (num < 999999)
    		return String.format("%.1f", num/1000.0) + 'K';
    	else
    		return String.format("%.1f", num/1000000.0) + 'M';
    }

    
	public static String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = getTwitterRelativeTimeSpanString(dateMillis,
																System.currentTimeMillis(), 
																DateUtils.SECOND_IN_MILLIS).toString();

		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	/**
	 * Only handles past time in twitter format.
	 * 2s
	 * 2m
	 * 2h
	 * 3d
	 * 
	 * @param time
	 * @param now
	 * @param minResolution
	 * @return
	 */
    public static CharSequence getTwitterRelativeTimeSpanString(long time, 
    															long now, 
    															long minResolution) {
    	StringBuilder result = new StringBuilder();
    	
    	Resources r = Resources.getSystem();
        boolean past = (now >= time);
        long duration = Math.abs(now - time);

        long count;
        if (duration < MINUTE_IN_MILLIS && minResolution < MINUTE_IN_MILLIS) {
            count = duration / SECOND_IN_MILLIS;
            if (past) {
            	result.append(count);
            	result.append("s");
            } else {
            	result.append("-");
            	result.append(count);
            	result.append("s");
            }
        } else if (duration < HOUR_IN_MILLIS && minResolution < HOUR_IN_MILLIS) {
            count = duration / MINUTE_IN_MILLIS;
            if (past) {
            	result.append(count);
            	result.append("m");
            } else {
            	result.append("-");
            	result.append(count);
            	result.append("m");
            }
        } else if (duration < DAY_IN_MILLIS && minResolution < DAY_IN_MILLIS) {
            count = duration / HOUR_IN_MILLIS;
            if (past) {
            	result.append(count);
            	result.append("h");
            } else {
            	result.append("-");
            	result.append(count);
            	result.append("h");
            }
        } else if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
        		result.append( DateUtils.getRelativeTimeSpanString(time, now, minResolution) );
        } else {

            result.append( DateUtils.formatDateRange(null, time, time, 0) );
        }
        
        return result.toString();
    }

}
