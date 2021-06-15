package com.example.restservice.models;

import java.util.UUID;

public class GetNameDTO {

    private String id;
    private String name;

    public GetNameDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}