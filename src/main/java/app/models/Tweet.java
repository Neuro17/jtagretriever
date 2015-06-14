package app.models;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tweet_test")
public class Tweet implements Serializable { 
		
	@EmbeddedId
	private TweetKey myKey;
	private double lat;
	private double lng;
	private String text;
	private String place;
	private String time;
    
	protected Tweet() {}

	public Tweet(long tweetId, double lat, double lng, String text,
			String place, String time) {		
		
		this.lat = lat;
		this.lng = lng;
		this.text = text;
		this.place = place;
		this.time = time;
	}
	
	public Tweet(String eventName, double lat, double lng, String text,
			String place, String time) {
		
		super();
		this.lat = lat;
		this.lng = lng;
		this.text = text;
		this.place = place;
		this.time = time;
	}

	public Tweet(long tweetId, String eventName, double lat, double lng,
			String text, String place, String time) {
		
		super();
		this.lat = lat;
		this.lng = lng;
		this.text = text;
		this.place = place;
		this.time = time;
	}
	
	public Tweet(TweetKey myKey, double lat, double lng) {
		
		super();
		this.myKey = myKey;
		this.lat = lat;
		this.lng = lng;
	}

	public Tweet(TweetKey myKey, double lat, double lng, String text,
			String place, String time) {
		
		super();
		this.myKey = myKey;
		this.lat = lat;
		this.lng = lng;
		this.text = text;
		this.place = place;
		this.time = time;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public TweetKey getMyKey() {
		return myKey;
	}

	public void setMyKey(TweetKey mykey) {
		this.myKey = mykey;
	}

	@Override
	public String toString() {
		return "Tweet [myKey=" + myKey + ", lat=" + lat + ", lng=" + lng
				+ ", text=" + text + ", place=" + place + ", time=" + time
				+ "]";
	}
}