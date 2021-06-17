package com.example.restservice.dataaccess.dal;

import com.example.restservice.dataaccess.daos.NameDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends MongoRepository<NameDAO, String> {
    NameDAO getByName(String name);
}