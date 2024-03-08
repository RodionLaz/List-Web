package com.example.backend.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public abstract class ListModel {
    @Id
    private String _id;
    private String title;
    private String mainText;

    public ListModel(){}
    public ListModel(String title, String mainText) {
        this.title = title;
        this.mainText = mainText;
    }
    
        public String getName() {
            return title;
        }
    
        public void setName(String title) {
            this.title = title;
        }
    
        public String getDescription() {
            return mainText;
        }
    
        public void setDescription(String mainText) {
            this.mainText = mainText;
        }
}
