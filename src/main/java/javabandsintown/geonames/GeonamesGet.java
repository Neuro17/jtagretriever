package javabandsintown.geonames;

import java.util.TimeZone;

import org.apache.http.client.utils.URIBuilder;

import com.google.gson.JsonObject;

public class GeonamesGet extends GeonamesConnector{
	
	public GeonamesGet(){
		uriBld = new URIBuilder();
		buildURI();
	}
	
	public GeonamesGet setLat(double lat) {
		uriBld.setParameter("lat", Double.toString(lat));

		return this;
	}
	
	public GeonamesGet setLng(double lng) {
		uriBld.setParameter("lng", Double.toString(lng));

		return this;
	}
	
	public GeonamesGet setUsername() {
		uriBld.setParameter("username",GeonamesConfig.getUsername());

		return this;
	}
	
	public GeonamesGet setJson(){
		uriBld.setPath(GeonamesConfig.getTimeZoneJson() + "?");

		return this;
	}

	public String search(){
		JsonObject geoNamesAsJson;
		String s = null;
		
		build();

		geoNamesAsJson = executeRequest();
		if(geoNamesAsJson != null)
			s = geoNamesAsJson.getAsJsonObject().get("timezoneId").getAsString();
				
		return s;
	}
	
	public int getHoursToAddToGMT(double lat, double lng){
		setLat(lat);
		setLng(lng);
		setUsername();
		String searchResult = search();
		if( searchResult != null)
			return TimeZone.getTimeZone(searchResult).getRawOffset()/3600000;
		else 
			return -99;
	}
	
//TODO WTF???	
	public String getGMTStandard(){
		
		return null;
	}
}