package app.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import app.models.Tweet;

@Transactional
public interface TweetRepository extends CrudRepository<Tweet, Long> {
	
	@Query("SELECT t FROM Tweet t WHERE t.myKey.eventName = :eventName")
    List<Tweet> findByEventName(@Param("eventName") String eventName);
	
	List<Tweet> findByLat(double lat);
}