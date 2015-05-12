package app.instagram;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.tags.TagInfoData;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.tags.TagSearchFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.model.QueryParam;
import org.joda.time.DateTime;

public class PhotoRetriever {

	private Instagram instagram = null;
	
    private static String CLIENT_ID = "f72f37aa491541a79412ce319f2e061f";

    public PhotoRetriever(){
    	instagram = new Instagram(CLIENT_ID);
    }
    
    //	ogni metodo senza (int count di default ritorna 20 MediaFeedData
    
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
    	
    	System.out.println(tag);
    	TagMediaFeed tmf = instagram.getRecentMediaTags(tag);
    	
		return tmf.getData();		

    }
    
    public List<MediaFeedData> getMediaByTag(String tag,int count) 
    													throws InstagramException{

    	TagMediaFeed tmf = instagram.getRecentMediaTags(tag);
    	
		return tmf.getData().subList(0, count);		

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
    		mfdList.addAll(getMediaByTag(tag).subList(0, count));
    	}
    	
    	return mfdList;
    }
    
}
