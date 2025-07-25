package com.EventApp.repository;

import com.EventApp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>
{
    User findByUsername(String username);
}
