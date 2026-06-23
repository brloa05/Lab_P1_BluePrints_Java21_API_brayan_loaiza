package edu.eci.arsw.blueprints.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlueprintsAPIControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllRetorna200ConApiResponse() throws Exception {
        mvc.perform(get("/api/v1/blueprints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("execute ok"));
    }

    @Test
    void getByAuthorExistenteRetorna200() throws Exception {
        mvc.perform(get("/api/v1/blueprints/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void getByAuthorInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/v1/blueprints/nobody"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void getBlueprintEspecificoRetorna200() throws Exception {
        mvc.perform(get("/api/v1/blueprints/john/house"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.author").value("john"))
                .andExpect(jsonPath("$.data.name").value("house"));
    }

    @Test
    void getBlueprintInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/v1/blueprints/john/nothing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void postValidoRetorna201() throws Exception {
        mvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"test\",\"name\":\"nuevo\",\"points\":[{\"x\":1,\"y\":1}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201));
    }

    @Test
    void postConAutorVacioRetorna400() throws Exception {
        mvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"\",\"name\":\"house\",\"points\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void putEnBlueprintExistenteRetorna202() throws Exception {
        mvc.perform(put("/api/v1/blueprints/john/house/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"x\":99,\"y\":99}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.code").value(202));
    }

    @Test
    void putEnBlueprintInexistenteRetorna404() throws Exception {
        mvc.perform(put("/api/v1/blueprints/nobody/nothing/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"x\":1,\"y\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}
