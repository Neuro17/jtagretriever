package app.instagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.common.Location;
import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.locations.LocationSearchFeed;
import org.jinstagram.entity.tags.TagInfoData;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.tags.TagSearchFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.model.QueryParam;
import org.joda.time.DateTime;

public class PhotoRetriever {
	
	private final static Logger log = LogManager.getLogger(PhotoRetriever.class);

	private Instagram instagram = null;
	
    private static String CLIENT_ID = "f72f37aa491541a79412ce319f2e061f";

    public PhotoRetriever(){
    	instagram = new Instagram(CLIENT_ID);
    }
    
    //	ogni metodo senza (int count) di default ritorna 20 MediaFeedData
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng) throws InstagramException{
    	
    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng);
    	return mediaFeed.getData();
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, int count) throws InstagramException{

    	List<MediaFeedData> mfd = this.getMediaByLocation(lat, lng);
    	return mfd.subList(0, count);    	
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, Long radius) throws InstagramException{
    	
    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, null, null, radius.intValue());
    	return mediaFeed.getData();
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, Long radius, int count) throws InstagramException{
    	
    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, null, null, radius.intValue());
    	return mediaFeed.getData().subList(0, count);
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end) throws InstagramException{

    	MediaFeed mediaFeed = 
    			instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), 0);
    	return mediaFeed.getData();
    	
    }
        
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end, int count) throws InstagramException{

    	MediaFeed mediaFeed = 
    			instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), 0);
    	return mediaFeed.getData().subList(0, count);
    	
    }
        
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end, Long radius) throws InstagramException{

    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), radius.intValue());
    	return mediaFeed.getData();
    	
    }
        
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end, Long radius, int count) throws InstagramException{

    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), radius.intValue());
    	return mediaFeed.getData().subList(0, count);
    	
    }
    
    public List<MediaFeedData> getMediaByTag(String tag) throws InstagramException{
    	
    	TagMediaFeed tmf = instagram.getRecentMediaTags(tag);    	
		return tmf.getData();		

    }
    
    public List<MediaFeedData> getMediaByTag(String tag,int count) throws InstagramException{
       	TagMediaFeed tmf = instagram.getRecentMediaTags(tag);
    	
    	List<MediaFeedData> mediaList = tmf.getData();		
    	int total = mediaList.size();
    	
    	MediaFeed recentMediaNextPage = instagram.getRecentMediaNextPage(tmf.getPagination());
    	
    	while(recentMediaNextPage.getPagination() != null && total < count) {
            mediaList.addAll(recentMediaNextPage.getData());
            recentMediaNextPage = instagram.getRecentMediaNextPage(recentMediaNextPage.getPagination());
            total = mediaList.size();
        }
    	
    	return mediaList.subList(0, count);
    }
    
    public List<MediaFeedData> getMediaByTagList(List<String> tagList) 
    													throws InstagramException{
    	List<MediaFeedData> mfdList = new ArrayList<MediaFeedData>();

    	for(String tag : tagList){
    		List<MediaFeedData> tmp = getMediaByTag(tag);
    		if(tmp != null) {
    			mfdList.addAll(tmp);
    		}
    	}
    	
    	return mfdList;
    }
    
    public List<MediaFeedData> getMediaByTagList(List<String> tagList, int count) 
    													throws InstagramException{
    	List<MediaFeedData> mfdList = new ArrayList<MediaFeedData>();

    	for(String tag : tagList){
    		mfdList.addAll(getMediaByTag(tag,count));
    	}
    	
    	return mfdList;
    }
    
    public List<MediaFeedData> getMedia(String tag,double lat,double lng, 
    		DateTime start, DateTime end, Long radius, int count) throws InstagramException{

    	List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
    	  	
    	TagMediaFeed tmf = instagram.getRecentMediaTags(tag);
    	List<MediaFeedData> mediaList = tmf.getData();		
    
    	finalMFD.addAll(checkMedia(mediaList,lat,lng,start,end,radius));

    	TagMediaFeed searchT = instagram.getRecentMediaTags(tag);

    	List<MediaFeedData> listofmedia = searchT.getData();
    	finalMFD.addAll(checkMedia(listofmedia,lat,lng,start,end,radius));
    	
    	while(searchT.getPagination() != null && finalMFD.size() < count){
    		searchT = instagram.getTagMediaInfoNextPage(searchT.getPagination());
	    	listofmedia = searchT.getData();
	     	finalMFD.addAll(checkMedia(listofmedia,lat,lng,start,end,radius));
log.trace("partial " + finalMFD.size());;
    	}

    	if(finalMFD.size() > count)
    		return finalMFD.subList(0, count);    
    	else
    		return finalMFD;
    }    
    
    public List<MediaFeedData> getMedia2(String tag,double lat,double lng, 
    		DateTime start, DateTime end, Long radius, int count) throws InstagramException{

//log.trace("entering getMedia2");
//log.trace("searching for " + lat + " " + lng);
//log.trace("from " + start + " to " + end);

		List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
		
		LocationSearchFeed locations = instagram.searchLocation(lat, lng,radius.intValue());
		List<Location> locationList = locations.getLocationList();
		MediaFeed mf = null;

		for(Location loc : locationList){
log.trace("processing " + loc.getName());
			mf = instagram.getRecentMediaByLocation(loc.getId());
			
			finalMFD.addAll(checkTags(mf.getData(),tag));
			
			while(mf.getPagination().getNextUrl() != null && finalMFD.size() < count){
				mf = instagram.getRecentMediaNextPage(mf.getPagination());
				finalMFD.addAll(checkTags(mf.getData(),tag));
				
log.trace("partial size " + finalMFD.size());
			}
		}
    	
log.trace(finalMFD.size());
		if(finalMFD.size() > count)
    		return finalMFD.subList(0, count);    
    	else
    		return finalMFD;
    }

	private List<MediaFeedData> checkTags(List<MediaFeedData> mediaList, String tag) {
		List<MediaFeedData> partialMFD = new ArrayList<MediaFeedData>();
//log.trace("entering checkTags");
		for(MediaFeedData mfd : mediaList){
//log.debug(mfd.getLocation().getLatitude());
//log.debug(mfd.getLocation().getLongitude());
//log.debug(new DateTime(Long.parseLong(mfd.getCreatedTime())*1000));
			if(mfd.getTags().contains(tag))
				partialMFD.add(mfd);
		}
		
		return partialMFD;
	}

	private List<MediaFeedData> checkMedia(List<MediaFeedData> mediaList, double lat, double lng,
			DateTime end, DateTime start, Long radius) {

		List<MediaFeedData> finalMediaList = new ArrayList<MediaFeedData>(); 
		
		for(MediaFeedData media : mediaList){
			if(media.getLocation() != null && media.getCreatedTime() != null){
				if(checkLocation(media.getLocation(),lat,lng,radius) 
						&& checkDate(media.getCreatedTime(),start,end)
						){
					finalMediaList.add(media);
//log.trace(media.getTags());
				}
			}
		}
		
		return finalMediaList;
	}

	private boolean checkDate(String createdTime, DateTime start, DateTime end) {

		DateTime createdDateTime = new DateTime(Long.parseLong(createdTime)*1000);

		if(createdDateTime.isBefore(end.getMillis()) && 
				createdDateTime.isAfter(start.getMillis())){
//log.trace(createdDateTime + " valid");
			return true;
		}
		else{
//log.trace(createdDateTime + " not valid");
			return false;
		}
	}

	private boolean checkLocation(Location location, double lat, double lng,Long radius) {

		if(distFrom(location.getLatitude(),location.getLongitude(),lat,lng) <= radius)
			return true;
		else
			return false;
	}
    
	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);
//log.debug(dist);
	    return dist;
	}
	
}
