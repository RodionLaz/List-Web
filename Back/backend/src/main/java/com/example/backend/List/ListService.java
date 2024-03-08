package com.example.backend.List;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

@Service
public class ListService {

    private final ListInterface listInterface;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Autowired
    public ListService(ListInterface listInterface) {
        this.listInterface = listInterface;
    }


public void updateLists(String jsonPayload) {
    try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
        ObjectMapper objectMapper = new ObjectMapper();
        Document[] documents = objectMapper.readValue(jsonPayload, Document[].class);
        
        MongoDatabase database = mongoClient.getDatabase("ListDB");
        MongoCollection<Document> collection = database.getCollection("ListCollection");
        
        for (Document document : documents) {

            Object idObject = document.get("_id");
            String idString = idObject.toString();

            int startIndex = idString.indexOf("=") + 1;
            int endIndex = idString.length() - 1;
            String objectIdString = idString.substring(startIndex, endIndex);

            ObjectId id = new ObjectId(objectIdString);

            String title = document.getString("title");
            String mainText = document.getString("mainText");
            if(id!= null){
                UpdateResult updateResult = collection.updateOne(Filters.eq("_id", id),
                        Updates.combine(
                                Updates.set("title", title),
                                Updates.set("mainText", mainText)
                        ),
                        new UpdateOptions().upsert(false) 
                );
            } 
        }
        } catch (Exception e) {
        System.err.println("Error updating documents: " + e.getMessage());
    }
}
public void addToLists(String jsonPayload) {
    try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
        ObjectMapper objectMapper = new ObjectMapper();
        Document[] documents = objectMapper.readValue(jsonPayload, Document[].class);
        
        MongoDatabase database = mongoClient.getDatabase("ListDB");
        MongoCollection<Document> collection = database.getCollection("ListCollection");
        
        for (Document document : documents) {

            Object idObject = document.get("_id");
            String idString = idObject.toString();

            int startIndex = idString.indexOf("=") + 1;
            int endIndex = idString.length() - 1;
            String objectIdString = idString.substring(startIndex, endIndex);

            ObjectId id = new ObjectId(objectIdString);

            String title = document.getString("title");
            String mainText = document.getString("mainText");
            if(id!= null){
                UpdateResult updateResult = collection.updateOne(Filters.eq("_id", id),
                        Updates.combine(
                                Updates.set("title", title),
                                Updates.set("mainText", mainText)
                        ),
                        new UpdateOptions().upsert(false) 
                );
            } 
        }
        } catch (Exception e) {
        System.err.println("Error updating documents: " + e.getMessage());
    }
}
public List<String> getAllLists() {

    List<String> jsonDocuments = new ArrayList<>();

    try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
        
        MongoDatabase database = mongoClient.getDatabase("ListDB");


        MongoCollection<Document> collection = database.getCollection("ListCollection");

       
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                jsonDocuments.add(document.toJson());
               
            }
        } finally {
            cursor.close(); 
        }
    }
    return jsonDocuments;
}


}
