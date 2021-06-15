package com.example.restservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.restservice.dataaccess.dal.NameRepository;
import com.example.restservice.dataaccess.daos.NameDAO;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerMVCTests {

    @MockBean
    private NameRepository nameRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void when_GetNameCalled_WithValidID_Then_ResponseIsOK_RepoIsCalled_And_IdAndNameAreReturned() throws Exception {
        doReturn((java.util.Optional.of(new NameDAO("id", "name")))).when(nameRepository).findById(any());
        MvcResult result = this.mockMvc
                .perform(get("/getNameById?id=5"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":\"id\",\"name\":\"name\"}"))
                .andReturn();

        verify(nameRepository, Mockito.times(1)).findById(any());
    }

    @Test
    public void when_GetNameCalled_WithInvalidID_Then_ResponseIsNotFound_RepoIsCalled_And_NothingIsReturned() throws Exception {
        doReturn(Optional.empty()).when(nameRepository).findById(any());
        MvcResult result = this.mockMvc
                .perform(get("/getNameById?id=5"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
                .andReturn();

        verify(nameRepository, Mockito.times(1)).findById(any());
    }

    @Test
    public void when_GetIdCalled_WithValidName_Then_ResponseIsOK_RepoIsCalled_And_IdAndNameAreReturned() throws Exception {
        doReturn(new NameDAO("id", "name")).when(nameRepository).getByName(any());
        MvcResult result = this.mockMvc
                .perform(get("/getIdByName?name=5"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":\"id\",\"name\":\"name\"}"))
                .andReturn();

        verify(nameRepository, Mockito.times(1)).getByName(any());
    }

    @Test
    public void when_GetIdCalled_WithInvalidName_Then_ResponseIsNotFound_And_RepoIsCalled() throws Exception {
        doReturn(null).when(nameRepository).getByName(any());
        MvcResult result = this.mockMvc
                .perform(get("/getIdByName?name=5"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
                .andReturn();

        verify(nameRepository, Mockito.times(1)).getByName(any());
    }


    @Test
    public void when_SetNameCalled_AndNameExists_Then_ResponseIsConflict_And_SaveIsNotCalled() throws Exception {
        NameDAO existingName = new NameDAO("Some", "Thing");
        doReturn(existingName).when(nameRepository).getByName(any());
        MvcResult result = this.mockMvc
                .perform(post("/setName?name=5"))
                .andExpect(status().isConflict())
                .andExpect(content().string(""))
                .andReturn();

        verify(nameRepository, Mockito.times(0)).save(any());
    }

    @Test
    public void when_SetNameCalled_And_NameIsUnique_Then_ResponseIsOK_And_SaveIsCalled() throws Exception {
        NameDAO savedName = new NameDAO("Some", "Thing");
        doReturn(null).when(nameRepository).getByName(any());
        doReturn(savedName).when(nameRepository).save(any());
        MvcResult result = this.mockMvc
                .perform(post("/setName?name=5"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":\"Some\",\"name\":\"Thing\"}"))
                .andReturn();

        verify(nameRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void when_SetNameCalled_And_SaveFails_Then_ResponseIsNotModified() throws Exception {
        doReturn(null).when(nameRepository).getByName(any());
        doReturn(null).when(nameRepository).save(any());
        MvcResult result = this.mockMvc
                .perform(post("/setName?name=5"))
                .andExpect(status().isNotModified())
                .andExpect(content().string(""))
                .andReturn();
    }
}
