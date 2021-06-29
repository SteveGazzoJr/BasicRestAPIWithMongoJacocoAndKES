package com.example.restservice.controllers;

import com.example.restservice.dataaccess.daos.NameDAO;
import com.example.restservice.exceptions.NameConflictException;
import com.example.restservice.exceptions.NotFoundException;
import com.example.restservice.exceptions.SaveFailedException;
import com.example.restservice.managers.NamesManager;
import com.example.restservice.models.GetIdByNameDTO;
import com.example.restservice.models.GetNameByIdDTO;
import com.example.restservice.models.ReturnNameAndIDDTO;
import com.example.restservice.models.SetNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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

    @PostMapping("/getNameById")
    public @ResponseBody
    ReturnNameAndIDDTO getNameById(@RequestBody GetNameByIdDTO getNameByIdDTO) {
        NameDAO nameDAO = namesManager.getNameById(getNameByIdDTO.getId());
        if (nameDAO != null) {
            return new ReturnNameAndIDDTO(nameDAO.getId(), nameDAO.getName());
        }

       throw new NotFoundException();
    }

    @PostMapping("/getIdByName")
    public @ResponseBody
    ReturnNameAndIDDTO getIdByName(@RequestBody GetIdByNameDTO getIdByNameDTO) {
        final NameDAO nameDAO = namesManager.getByName(getIdByNameDTO.getName());
        if (nameDAO != null) {
            return new ReturnNameAndIDDTO(nameDAO.getId(), nameDAO.getName());
        }

        throw new NotFoundException();
    }

    @PostMapping("/setName")
    @CrossOrigin(origins = "*")
    public @ResponseBody
    ReturnNameAndIDDTO setName(@RequestBody SetNameDTO setNameDTO){

        final NameDAO existingName = namesManager.getByName(setNameDTO.getName());
        if(existingName != null) throw new NameConflictException();

        NameDAO newName = new NameDAO(null, setNameDTO.getName());

        final NameDAO savedName = namesManager.save(newName);
        if(savedName != null) {
            return new ReturnNameAndIDDTO(savedName.getId(), savedName.getName());
        }

        throw new SaveFailedException();
    }

}