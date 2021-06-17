package com.example.restservice.managers;

import com.example.restservice.dataaccess.dal.NameRepository;
import com.example.restservice.dataaccess.daos.NameDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NamesManager {

    @Autowired
    private NameRepository nameRepository;

    public NameDAO getByName(String name){
        return nameRepository.getByName(name);
    }

    public NameDAO getNameById(String id){
        Optional<NameDAO> name = nameRepository.findById(id);
        if(name.isPresent()){
            return name.get();
        }
        return null;
    }

    public NameDAO save(NameDAO nameToBeSaved){
        NameDAO savedName = nameRepository.save(nameToBeSaved);
        return savedName; //can be null on save failure
    }
}
