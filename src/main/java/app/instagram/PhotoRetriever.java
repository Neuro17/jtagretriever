package app.instagram;

import java.util.ArrayList;
import java.util.Arrays;
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
//	
	private final static Logger log = LogManager.getLogger(PhotoRetriever.class);

	private Instagram instagram = null;
	
    private static String CLIENT_ID = "f72f37aa491541a79412ce319f2e061f";

    public PhotoRetriever(){
    	instagram = new Instagram(CLIENT_ID);
    }
    
    //	ogni metodo senza (int count) di default ritorna 20 MediaFeedData
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng) throws InstagramException{
// null    	
    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng);
//log.trace(mediaFeed.getPagination() + " getMediaByLocation(double lat,double lng)");
    	return mediaFeed.getData();
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, int count) throws InstagramException{

    	List<MediaFeedData> mfd = this.getMediaByLocation(lat, lng);
    	return mfd.subList(0, count);    	
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, Long radius) throws InstagramException{
//	null   	
    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, null, null, radius.intValue());
//log.trace(mediaFeed.getPagination() + " getMediaByLocation(double lat,double lng, Long radius)");
    	return mediaFeed.getData();
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, Long radius, int count) throws InstagramException{
    	
    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, null, null, radius.intValue());
    	return mediaFeed.getData().subList(0, count);
    }
    
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end) throws InstagramException{
//null
    	MediaFeed mediaFeed = 
    			instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), 0);
//log.trace(mediaFeed.getPagination() + " getMediaByLocation(double lat,double lng, DateTime start, DateTime end)");
    	return mediaFeed.getData();
    	
    }
        
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end, int count) throws InstagramException{

    	MediaFeed mediaFeed = 
    			instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), 0);
    	return mediaFeed.getData().subList(0, count);
    	
    }
        
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end, Long radius) throws InstagramException{
//null
    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), radius.intValue());
//log.trace(mediaFeed.getPagination() + " getMediaByLocation(double lat,double lng, DateTime start, DateTime end, Long radius)");
    	return mediaFeed.getData();
    	
    }
        
    public List<MediaFeedData> getMediaByLocation(double lat,double lng, DateTime start, DateTime end, Long radius, int count) throws InstagramException{

    	MediaFeed mediaFeed = instagram.searchMedia(lat, lng, end.toDate(), start.toDate(), radius.intValue());
    	return mediaFeed.getData().subList(0, count);
    	
    }
    
    public List<MediaFeedData> getMediaByTag(String tag) throws InstagramException{
    	
    	TagMediaFeed tmf = instagram.getRecentMediaTags(tag);    	
log.trace(tmf.getPagination() + " getMediaByTag(String tag)");
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
log.trace("partial size " + finalMFD.size());
    	}

log.trace("final size " + finalMFD.size());
    	if(finalMFD.size() > count)
    		return finalMFD.subList(0, count);    
    	else
    		return finalMFD;
    }    
    
    public List<MediaFeedData> getMedia2(String tag,double lat,double lng, 
    		DateTime start, DateTime end, Long radius, int count) throws InstagramException{

log.trace("entering getMedia2");
log.trace("searching for " + lat + " " + lng);
log.trace("from " + start + " to " + end);

		List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();

		TagMediaFeed tmf = instagram.getRecentMediaTags(tag,  
				String.valueOf(start.getMillis()) + "000",
				String.valueOf(end.getMillis()) + "000", 
				count);
		
		List<MediaFeedData> medias = tmf.getData();
		finalMFD.addAll(checkLocation(medias,lat,lng,radius));

    	while(tmf.getPagination() != null && finalMFD.size() < count){
    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
			medias = tmf.getData();
	     	finalMFD.addAll(checkLocation(medias,lat,lng,radius));
log.trace("partial size " + finalMFD.size());
    	}

log.trace("final size " + finalMFD.size());

		return finalMFD;
    }
    
	public List<MediaFeedData> getMedia3(ArrayList<String> tags,
			double lat, double lng, DateTime start,
			DateTime end, long radius, int countForTag) throws InstagramException {
		
log.trace("entering getMedia3");
log.trace("searching for position (" + lat + "," + lng + ")");
log.trace("from " + start + " to " + end);
log.trace("for doubleTags " + tags);

				int iterationForTag = 7;

				List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
				
				for(String tag : tags){	
					List<MediaFeedData> partialMFD = new ArrayList<MediaFeedData>();
					
log.trace("processing tag " + tag);

					TagMediaFeed tmf = instagram.getRecentMediaTags(tag,  
							String.valueOf(start.getMillis()) + "000",
							String.valueOf(end.getMillis()) + "000", 
							countForTag);
					List<MediaFeedData> medias = tmf.getData();
					
					if(tags.indexOf(tag) == 0)
						partialMFD.addAll(medias);
					else
						partialMFD.addAll(checkDoubleTag(medias,tags,tag));
//log.trace("initial partial size " + partialMFD.size() + " for tag " + tag);

					int iteration = 0;
			    	while(tmf.getPagination() != null 
			    			&& partialMFD.size() < countForTag 
			    			&& iteration < iterationForTag){
			    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
						medias = tmf.getData();
						if(tags.indexOf(tag) == 0)
							partialMFD.addAll(medias);
						else
							partialMFD.addAll(checkDoubleTag(medias,tags,tag));
//log.trace("medium partial size " + partialMFD.size() + " for tag " + tag + " on iteration " + iteration);
						iteration++;
		    		}
			    	if(partialMFD.size() != 0)
			    		finalMFD.addAll(partialMFD);
log.trace(partialMFD.size() + " results for tag " + tag);
				}			
log.trace(finalMFD.size() + " total results");
				return finalMFD;
	}

	private List<MediaFeedData> checkDoubleTag(
			List<MediaFeedData> medias, ArrayList<String> tags, String tagAlreadyChecked) {

//log.trace("checkDoubleTag - tagList initial size " + tags.size());
		if(tagAlreadyChecked.length() > 140)
			return null;
		
		ArrayList<String> tagList = new ArrayList<String>(tags);

		int index = tagList.indexOf(tagAlreadyChecked);
//log.trace("index of tag " + tagAlreadyChecked + " " + index);
		tagList.remove(index);
		
		List<MediaFeedData> MFD = new ArrayList<MediaFeedData>();
		for(MediaFeedData media : medias){
			for(String t : tagList)
				if(media.getTags().contains(t)){
					MFD.add(media);
					break;
				}
		}		
//log.trace("checkDoubleTag - tagList final size " + tags.size());
		return MFD;
	}

	private List<MediaFeedData> checkLocation(List<MediaFeedData> medias, 
			double lat, double lng, Long radius) {

		ArrayList<MediaFeedData> MFD = new ArrayList<MediaFeedData>();
		
		for(MediaFeedData media : medias){
// in assenza di locatiom aggiunge altrimenti controlla la coincidenza
			if(media.getLocation() == null || checkRadius(media.getLocation(), lng, lng, radius))
				MFD.add(media);
		}
		return MFD;
	}

	private List<MediaFeedData> checkMedia(List<MediaFeedData> mediaList, double lat, double lng,
			DateTime end, DateTime start, Long radius) {
//controlla solo lat e lng entro radius
		List<MediaFeedData> finalMediaList = new ArrayList<MediaFeedData>(); 
		
		for(MediaFeedData media : mediaList){
			if(media.getLocation() != null && media.getCreatedTime() != null){
				if(checkRadius(media.getLocation(),lat,lng,radius)){
					finalMediaList.add(media);
//log.trace(media.getTags());
				}
			}
		}
		
		return finalMediaList;
	}

	private boolean checkRadius(Location location, double lat, double lng,Long radius) {

		if(distFrom(location.getLatitude(),location.getLongitude(),lat,lng) <= radius){
log.trace("valid location " + location);
			return true;
		}
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

	    return dist;
	}
	
}
