package com.EventApp.repository;

import com.EventApp.model.EventQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EventQuestionRepository extends MongoRepository<EventQuestion, String>
{
    List<EventQuestion> findByEventName(String eventName);

    @Query(value = "{}", fields = "{ 'eventName' : 1 }")
    List<EventQuestion> findAllEventNamesOnly();
}
