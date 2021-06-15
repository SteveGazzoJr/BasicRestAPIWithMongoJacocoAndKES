package com.example.restservice.controllers;

import java.util.Optional;

import com.example.restservice.dataaccess.dal.NameRepository;
import com.example.restservice.dataaccess.daos.NameDAO;
import com.example.restservice.exceptions.NameConflictException;
import com.example.restservice.exceptions.NotFoundException;
import com.example.restservice.exceptions.SaveFailedException;
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
    private NameRepository nameRepository;

    public GreetingController(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    @GetMapping("/getNameById")
    public @ResponseBody GetNameDTO getNameById(@RequestParam(value = "id") String id) {
        Optional<NameDAO> nameDAO = nameRepository.findById(id);
        if (nameDAO.isPresent()) {
            return new GetNameDTO(nameDAO.get().getId(), nameDAO.get().getName());
        }

       throw new NotFoundException();
    }

    @GetMapping("/getIdByName")
    public @ResponseBody GetNameDTO getIdByName(@RequestParam(value = "name") String name) {
        final NameDAO nameDAO = nameRepository.getByName(name);
        if (nameDAO != null) {
            return new GetNameDTO(nameDAO.getId(), nameDAO.getName());
        }

        throw new NotFoundException();
    }

    @PostMapping("/setName")
    public @ResponseBody GetNameDTO setName(@RequestParam String name){

        final NameDAO get = nameRepository.getByName(name);
        if(get != null) throw new NameConflictException();

        NameDAO nameDAO = new NameDAO();
        nameDAO.setName(name);

        final NameDAO save = nameRepository.save(nameDAO);
        if(save != null) {
            return new GetNameDTO(save.getId(), save.getName());
        }

        throw new SaveFailedException();
    }

}