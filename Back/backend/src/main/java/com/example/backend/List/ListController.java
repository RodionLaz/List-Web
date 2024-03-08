package com.example.backend.List;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ListController {

    @Autowired
    private ListService listService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/GetList")
    public ResponseEntity<List<String>> postMethodName() {
       System.out.println("the list " + listService.getAllLists());
        return ResponseEntity.ok(listService.getAllLists());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/UpdateList")
    public ResponseEntity<String> updateList(@RequestBody String jsonPayload) {
        System.out.println("Received JSON payload: " + jsonPayload);
        listService.updateLists(jsonPayload);
        return ResponseEntity.ok("List updated successfully");
    }
}