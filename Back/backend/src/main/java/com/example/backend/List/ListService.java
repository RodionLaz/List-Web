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
import com.mongodb.client.model.Sorts;
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

            String title = document.getString("title");
            String mainText = document.getString("mainText");
            Object idObject = document.get("_id");
            String idString = idObject.toString();
            if(idString != ""){

           

            int startIndex = idString.indexOf("=") + 1;
            int endIndex = idString.length() - 1;
            String objectIdString = idString.substring(startIndex, endIndex);

            ObjectId id = new ObjectId(objectIdString);

           
                UpdateResult updateResult = collection.updateOne(Filters.eq("_id", id),
                        Updates.combine(
                                Updates.set("title", title),
                                Updates.set("mainText", mainText)
                        ),
                        new UpdateOptions().upsert(false) 
                );
            }else{
                ObjectId newId = new ObjectId();
                Document newDocument = new Document("_id", newId)
                                            .append("title", title)
                                            .append("mainText", mainText);

                collection.insertOne(newDocument);
            }


        }
        } catch (Exception e) {
        System.err.println("Error updating documents: " + e.getMessage());
    }
}

public void removeLast(){
    try(MongoClient mongoClient = MongoClients.create(mongoUri)){
        MongoDatabase database = mongoClient.getDatabase("ListDb");
        MongoCollection<Document> collection = database.getCollection("ListCollection");
        Document lastDocument = collection.find()
        .sort(Sorts.descending("_id"))
        .first();
        if (lastDocument != null) {
            Object lastDocumentId = lastDocument.getObjectId("_id");
        
            collection.deleteOne(Filters.eq("_id", lastDocumentId));
            System.out.println("Last document removed successfully.");
        } else {
            System.out.println("No documents found in the collection.");
        }
    } catch (Exception e) {
        System.err.println("Error removing last document: " + e.getMessage());
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
