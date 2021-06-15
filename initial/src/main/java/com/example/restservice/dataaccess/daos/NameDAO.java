package com.example.restservice.dataaccess.daos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "names")
public class NameDAO {
    @Id
    private String id;
    private String name;

    public NameDAO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public NameDAO() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
