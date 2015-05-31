package dataBaseService;

public class Photo {
	
	private int eventId;
	private String mediaId;
	private String urlLinkLow;
    private String urlLinkStd;
    
    public Photo(){
    }
    
	public Photo(int eventId, String mediaId, String urlLinkLow,
			String urlLinkStd) {
		super();
		this.eventId = eventId;
		this.mediaId = mediaId;
		this.urlLinkLow = urlLinkLow;
		this.urlLinkStd = urlLinkStd;
	}
	
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getUrlLinkLow() {
		return urlLinkLow;
	}
	public void setUrlLinkLow(String urlLinkLow) {
		this.urlLinkLow = urlLinkLow;
	}
	public String getUrlLinkStd() {
		return urlLinkStd;
	}
	public void setUrlLinkStd(String urlLinkStd) {
		this.urlLinkStd = urlLinkStd;
	}

	@Override
	public String toString() {
		return "Photo [eventId=" + eventId + ", mediaId=" + mediaId
				+ ", urlLinkLow=" + urlLinkLow + ", urlLinkStd=" + urlLinkStd
				+ "]";
	}
    
}
