package com.EventApp.repository;

import com.EventApp.model.EventResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventResponseRepository extends MongoRepository<EventResponse, String>
{

}
