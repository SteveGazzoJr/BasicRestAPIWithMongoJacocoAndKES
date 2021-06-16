package com.example.restservice.controllers;

import java.util.Optional;

import com.example.restservice.dataaccess.dal.NameRepository;
import com.example.restservice.dataaccess.daos.NameDAO;
import com.example.restservice.exceptions.NameConflictException;
import com.example.restservice.exceptions.NotFoundException;
import com.example.restservice.exceptions.SaveFailedException;
import com.example.restservice.managers.NamesManager;
import com.example.restservice.models.GetNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ComponentScan("com.example.restservice.dataaccess.repositories")
public class GreetingController {

    @Autowired
    private NamesManager namesManager;

    public GreetingController(NamesManager namesManager) {
        this.namesManager = namesManager;
    }

    @GetMapping("/getNameById")
    public @ResponseBody GetNameDTO getNameById(@RequestParam(value = "id") String id) {
        NameDAO nameDAO = namesManager.getNameById(id);
        if (nameDAO != null) {
            return new GetNameDTO(nameDAO.getId(), nameDAO.getName());
        }

       throw new NotFoundException();
    }

    @GetMapping("/getIdByName")
    public @ResponseBody GetNameDTO getIdByName(@RequestParam(value = "name") String name) {
        final NameDAO nameDAO = namesManager.getByName(name);
        if (nameDAO != null) {
            return new GetNameDTO(nameDAO.getId(), nameDAO.getName());
        }

        throw new NotFoundException();
    }

    @PostMapping("/setName")
    public @ResponseBody GetNameDTO setName(@RequestParam String name){

        final NameDAO existingName = namesManager.getByName(name);
        if(existingName != null) throw new NameConflictException();

        NameDAO newName = new NameDAO();
        newName.setName(name);

        final NameDAO savedName = namesManager.save(newName);
        if(savedName != null) {
            return new GetNameDTO(savedName.getId(), savedName.getName());
        }

        throw new SaveFailedException();
    }

}