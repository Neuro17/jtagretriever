package app.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.springframework.boot.orm.jpa.EntityScan;

@Embeddable
public class TweetKey implements Serializable {

    private Long id;

    @Column(name = "eventName", nullable = false)
    private String eventName;
    
    
    public TweetKey() {
    	super();
    }

	public TweetKey(Long id, String eventName) {
		super();
		this.id = id;
		this.eventName = eventName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public String toString() {
		return "TweetKey [id=" + id + ", eventName=" + eventName + "]";
	}

}
