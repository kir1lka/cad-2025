package ru.bsuedu.cad.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void clearMessages() throws Exception {
        mvc.perform(delete("/messages")).andExpect(status().isOk());
    }

    @Test
    void returnsGreeting() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Привет, мир!"));
    }

    @Test
    void returnsEmptyList() throws Exception {
        mvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createsUpdatesAndDeletesMessage() throws Exception {
        mvc.perform(post("/messages").contentType(MediaType.TEXT_PLAIN).content("First note"))
                .andExpect(status().isOk())
                .andExpect(content().string("Сообщение успешно опубликовано!"));
        mvc.perform(get("/messages")).andExpect(content().json("[\"First note\"]"));
        mvc.perform(put("/messages/0").contentType(MediaType.TEXT_PLAIN).content("Updated note"))
                .andExpect(status().isOk())
                .andExpect(content().string("Сообщение успешно изменено!"));
        mvc.perform(get("/messages")).andExpect(content().json("[\"Updated note\"]"));
        mvc.perform(delete("/messages/0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Сообщение успешно удалено!"));
        mvc.perform(get("/messages")).andExpect(content().json("[]"));
    }

    @Test
    void countsAndClearsAllMessages() throws Exception {
        mvc.perform(get("/messages/count")).andExpect(content().string("0"));
        mvc.perform(post("/messages").contentType(MediaType.TEXT_PLAIN).content("One"));
        mvc.perform(post("/messages").contentType(MediaType.TEXT_PLAIN).content("Two"));
        mvc.perform(get("/messages/count")).andExpect(content().string("2"));
        mvc.perform(delete("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().string("Все сообщения успешно удалены!"));
        mvc.perform(get("/messages")).andExpect(content().json("[]"));
        mvc.perform(get("/messages/count")).andExpect(content().string("0"));
        mvc.perform(delete("/messages")).andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 10})
    void rejectsUpdateOfMissingMessage(int index) throws Exception {
        mvc.perform(put("/messages/{index}", index)
                        .contentType(MediaType.TEXT_PLAIN).content("Updated note"))
                .andExpect(status().isOk())
                .andExpect(content().string("Сообщение с индексом " + index + " не найдено"));
        mvc.perform(get("/messages")).andExpect(content().json("[]"));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 10})
    void rejectsDeleteOfMissingMessage(int index) throws Exception {
        mvc.perform(delete("/messages/{index}", index))
                .andExpect(status().isOk())
                .andExpect(content().string("Сообщение с индексом " + index + " не найдено"));
        mvc.perform(get("/messages")).andExpect(content().json("[]"));
    }
}
