package com.example.backend.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ListInterface extends MongoRepository<ListModel, String> {
    

}

