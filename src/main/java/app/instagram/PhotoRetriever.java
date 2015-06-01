package app.instagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
// search count photos after maxIteration iterations for each tag, time and geolocalized
// BAD
    public List<MediaFeedData> getMedia(String tag,double lat,double lng, 
    		DateTime start, DateTime end, Long radius, int count) throws InstagramException{
log.trace("[getMedia] processing tag " + tag);
    	List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
    	  	
    	TagMediaFeed tmf = instagram.getRecentMediaTags(tag);
    	List<MediaFeedData> mediaList = tmf.getData();		
    
    	finalMFD.addAll(checkMedia(mediaList,lat,lng,start,end,radius));

    	TagMediaFeed searchT = instagram.getRecentMediaTags(tag);

    	List<MediaFeedData> listofmedia = searchT.getData();
    	finalMFD.addAll(checkMedia(listofmedia,lat,lng,start,end,radius));
    	
    	int iteration = 1;
    	int maxIteration = 11;
		while(searchT.getPagination() != null 
    			&& finalMFD.size() < count
    			&& iteration < maxIteration ){
    		searchT = instagram.getTagMediaInfoNextPage(searchT.getPagination());
	    	listofmedia = searchT.getData();
	     	finalMFD.addAll(checkMedia(listofmedia,lat,lng,start,end,radius));
log.trace("partial size " + finalMFD.size() + " iteration " + iteration);
			iteration++;
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
log.trace(String.valueOf(start.getMillis()) + "000");
log.trace(String.valueOf(end.getMillis()) + "000");
		TagMediaFeed tmf = instagram.getRecentMediaTags(tag,
				String.valueOf(start.getMillis() + "000"),
				String.valueOf(end.getMillis() + "000"),   

				count);
		List<MediaFeedData> medias = tmf.getData();
log.debug("time of first result " + (new DateTime(Long.parseLong(medias.get(0).getCreatedTime())*1000)).toString());
		finalMFD.addAll(checkLocation(medias,lat,lng,radius));

		int iteration = 1;
    	while(tmf.getPagination() != null && finalMFD.size() < count){
    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
			medias = tmf.getData();
	     	finalMFD.addAll(checkLocation(medias,lat,lng,radius));
//log.trace("partial size " + finalMFD.size() + " iteration " + iteration);
			iteration++;
    	}
//log.trace("final size " + finalMFD.size());
		return finalMFD;
    }

//	each photo have two of the tags taken from the tags
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
log.debug("time of first result " + (new DateTime(Long.parseLong(medias.get(0).getCreatedTime())*1000)).toString());
					
					if(tags.indexOf(tag) == 0)
						partialMFD.addAll(medias);
					else
						partialMFD.addAll(checkDoubleTag(medias,tags,tag));
//log.trace("initial partial size " + partialMFD.size() + " for tag " + tag);

					int iteration = 1;
			    	while(tmf.getPagination() != null 
			    			&& partialMFD.size() < countForTag * 3 
			    			&& iteration < iterationForTag){
			    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
						medias = tmf.getData();
						if(tags.indexOf(tag) == 0)
							partialMFD.addAll(medias);
						else
							partialMFD.addAll(checkDoubleTag(medias,tags,tag));
log.trace("medium partial size " + partialMFD.size() + " for tag " + tag + " on iteration " + iteration);
						iteration++;
		    		}
			    	if(partialMFD.size() != 0)
			    		finalMFD.addAll(partialMFD);
log.trace(partialMFD.size() + " results for tag " + tag);
				}			
log.trace(finalMFD.size() + " total results");
				return finalMFD;
	}


//	each photo has two of the tags taken from the tags and is geolocalized
    public List<MediaFeedData> getMedia3bis(ArrayList<String> tags,
			double lat, double lng, DateTime start,
			DateTime end, long radius, int countForTag) throws InstagramException {
		
log.trace("entering getMedia3bis");
log.trace("searching for position (" + lat + "," + lng + ")");
log.trace("from " + start + " to " + end);
log.trace("for doubleTags " + tags);

				int iterationForTag = 5;

				List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
				
				for(String tag : tags){	
					List<MediaFeedData> partialMFD = new ArrayList<MediaFeedData>();
					
log.trace("processing tag " + tag);

					TagMediaFeed tmf = instagram.getRecentMediaTags(tag,  
							String.valueOf(start.getMillis()) + "000",
							String.valueOf(end.getMillis()) + "000",
							33);
					List<MediaFeedData> medias = tmf.getData();
					finalMFD.addAll(checkDoubleTag(checkLocation(medias,lat,lng,radius), tags, tag));

					int iteration = 1;
			    	while(tmf.getPagination() != null 
			    			&& partialMFD.size() < countForTag 
			    			&& iteration < iterationForTag){
			    		
			    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
						medias = tmf.getData();
						
						finalMFD.addAll(checkDoubleTag(checkLocation(medias,lat,lng,radius), tags, tag));
log.trace("medium partial size " + partialMFD.size() + " for tag " + tag + " on iteration " + iteration);
						iteration++;
		    		}
			    	if(partialMFD.size() != 0)
			    		finalMFD.addAll(partialMFD);
log.trace(partialMFD.size() + " results for tag " + tag);
				}			
log.trace(finalMFD.size() + " total results");
				return finalMFD;
	}
    
//	considers a mainTag and each photo must contain it and another from the tags
	public List<MediaFeedData> getMedia4(ArrayList<String> tags,
			double lat, double lng, DateTime start,
			DateTime end, long radius, int count) throws InstagramException {
		
log.trace("entering getMedia4");

				List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
				Set<MediaFeedData> finalMFDset = new HashSet<MediaFeedData>(); 
				String mainTag = tags.get(0);
				tags.remove(mainTag);
log.trace("checking mainTag " + mainTag);
log.trace("and remaining in " + tags);

				TagMediaFeed tmf = instagram.getRecentMediaTags(mainTag,  
						String.valueOf(start.getMillis()) + "000",
						String.valueOf(end.getMillis()) + "000", 
						count * 5);
				List<MediaFeedData> medias = tmf.getData();

				finalMFDset.addAll(checkTag(medias,tags));
			
				int iteration = 1;
		    	while(tmf.getPagination() != null 
		    			&& finalMFDset.size() < count){
		    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
					medias = tmf.getData();
log.debug("time of result " + (new DateTime(Long.parseLong(medias.get(0).getCreatedTime())*1000)).toString());
					finalMFDset.addAll(checkTag(medias,tags));
log.trace("["+ mainTag + "] partial size " + finalMFDset.size() + " iteration " + iteration);
					iteration++;
		    	}
		    	
		    	finalMFD.addAll(finalMFDset);

				return finalMFD;
}

//	considers a mainTag and each photo must contain it and another from the tags
//	and geolocalized
	public List<MediaFeedData> getMedia4bis(ArrayList<String> tags,
			double lat, double lng, DateTime start,
			DateTime end, long radius, int count) throws InstagramException {
		
log.trace("entering getMedia4bis");
log.trace("searching for position (" + lat + "," + lng + ")");
				List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
				Set<MediaFeedData> finalMFDset = new HashSet<MediaFeedData>(); 
				String mainTag = tags.get(0);
				tags.remove(mainTag);
log.trace("for mainTag " + mainTag);
log.trace("and remaining in " + tags);

				TagMediaFeed tmf = instagram.getRecentMediaTags(mainTag,  
						String.valueOf(start.getMillis()) + "000",
						String.valueOf(end.getMillis()) + "000", 
						count * 5);
				List<MediaFeedData> medias = tmf.getData();
log.debug("time of first result " + (new DateTime(Long.parseLong(medias.get(0).getCreatedTime())*1000)).toString());
				finalMFDset.addAll(checkTag(checkLocation(medias,lat,lng,radius),tags));
			
				int iteration = 1 ;
		    	while(tmf.getPagination() != null 
		    			&& finalMFDset.size() < count){
		    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
					medias = tmf.getData();
					finalMFDset.addAll(checkTag(checkLocation(medias,lat,lng,radius),tags));
log.trace("["+ mainTag + "] partial size " + finalMFDset.size() + " iteration " + iteration);
					iteration++;
		    	}
		    	
		    	finalMFD.addAll(finalMFDset);

				return finalMFD;
}
	
//	considers a mainTag and each photo must contain it and another from the tags,
//	geolocalized and in the time range
	public List<MediaFeedData> getMedia4tris(ArrayList<String> tags,
			double lat, double lng, DateTime start,
			DateTime end, long radius) throws InstagramException {
		
log.trace("entering getMedia4tris");
log.trace("searching for position (" + lat + "," + lng + ")");
log.trace("from " + start + " to " + end);
				List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
				Set<MediaFeedData> finalMFDset = new HashSet<MediaFeedData>(); 
				String mainTag = tags.get(0).replaceAll("\\?", "Q");
				tags.remove(mainTag);
log.trace("for mainTag " + mainTag);
log.trace("and remaining in " + tags);
				TagMediaFeed tmf = instagram.getRecentMediaTags(mainTag,  
						String.valueOf(start.getMillis()) + "000",
						String.valueOf(end.getMillis()) + "000", 
						33);
				List<MediaFeedData> medias = tmf.getData();

				finalMFDset.addAll(
						checkDoubleTag(
								checkDate(
										checkLocation(medias, lat, lng, radius),
										start,end)
								,tags,mainTag));

				while(tmf.getPagination() != null 
		    			&& medias.size() > 0	
		    			&& (new DateTime(
		    					Long.parseLong(
		    							medias.get(
		    									medias.size()-1)
		    									.getCreatedTime())*1000))
		    									.isAfter(start.getMillis())){
		    		tmf = instagram.getTagMediaInfoNextPage(tmf.getPagination());    		
					medias = tmf.getData();
log.trace("[" + mainTag + "] last media time " + new DateTime(
									Long.parseLong(
											medias.get(
													medias.size()-1)
													.getCreatedTime())*1000));
					finalMFDset.addAll(
							checkDoubleTag(
									checkDate(
											checkLocation(medias, lat, lng, radius),
											start,end)
									,tags,mainTag));
log.trace(finalMFDset.size());
		    	}
		    	
		    	finalMFD.addAll(finalMFDset);

				return finalMFD;
}

	private List<MediaFeedData> checkTag(List<MediaFeedData> medias, 
			ArrayList<String> tags) {
		if(medias.size() != 0){
			List<MediaFeedData> MFD = new ArrayList<MediaFeedData>();
	
			for(MediaFeedData media : medias){
				for(String t : tags){
					if(media.getTags().contains(t)){
						MFD.add(media);
						break;
					}
				}
			}		
log.trace(" +" + MFD.size());
			return MFD;
		}
		else
			return null;
	}

	private List<MediaFeedData> checkDoubleTag(
			List<MediaFeedData> medias, ArrayList<String> tags, 
			String tagAlreadyChecked) {

		if(tagAlreadyChecked.length() > 140)
			return null;
		
		ArrayList<String> tagList = new ArrayList<String>(tags);
//log.trace(tagAlreadyChecked);
//log.trace(tags);
		
		List<MediaFeedData> MFD = new ArrayList<MediaFeedData>();
		for(MediaFeedData media : medias){
			for(String t : tagList)
				if(media.getTags().contains(t)){
					MFD.add(media);
					break;
				}
		}		
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
		List<MediaFeedData> finalMediaList = new ArrayList<MediaFeedData>(); 
		
		for(MediaFeedData media : mediaList){
			if(media.getLocation() != null && media.getCreatedTime() != null){
				if(checkRadius(media.getLocation(),lat,lng,radius)
						&& checkDate(media.getCreatedTime(),start,end)){
					finalMediaList.add(media);
//log.trace(media.getTags());
				}
			}
		}
		
		return finalMediaList;
	}

	private List<MediaFeedData> checkDate(List<MediaFeedData> mediaList, 
			DateTime start, DateTime end) {
		List<MediaFeedData> finalMFD = new ArrayList<MediaFeedData>();
		
		for(MediaFeedData mfd : mediaList){
			DateTime time = new DateTime(Long.parseLong(mfd.getCreatedTime())*1000);
			if(time.isAfter(start)
					&& time.isBefore(end))
				finalMFD.add(mfd);
		}
		return finalMFD;
	}

	private boolean checkDate(String createdTime, DateTime start, DateTime end) {
		DateTime time = new DateTime(Long.parseLong(createdTime)*1000);
		if(time.isAfter(start)
				&& time.isBefore(end))
			return true;
		else 
			return false;
	}

	private boolean checkRadius(Location location, double lat, double lng,Long radius) {

		if(distFrom(location.getLatitude(),location.getLongitude(),lat,lng) <= radius){
//log.trace("valid location " + location);
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
