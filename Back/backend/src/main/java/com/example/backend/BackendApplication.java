package com.example.backend;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.backend")
public class BackendApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://BOB:WCDPyVDAZ1UiMQos@db1.ujqevn6.mongodb.net/ListDB?retryWrites=true&w=majority&appName=DB1")) {
            System.out.println("Successfully connected to MongoDB Atlas!");
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB Atlas: " + e.getMessage());
        }
    }
}