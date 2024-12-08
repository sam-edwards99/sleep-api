package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Used CrudRepository since the features for users are very basic CRUD operations
@Repository
public interface UserRepository extends CrudRepository<User, Long> { }