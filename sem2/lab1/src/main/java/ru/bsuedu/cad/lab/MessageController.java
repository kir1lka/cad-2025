package ru.bsuedu.cad.lab;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {

    private final List<String> userMessages = new ArrayList<>();

    @GetMapping("/")
    public String helloWorld() {
        return "Привет, мир!";
    }

    @GetMapping("/messages")
    public synchronized List<String> getAllMessages() {
        return new ArrayList<>(userMessages);
    }

    @PostMapping("/messages")
    public synchronized String publishMessage(@RequestBody String message) {
        userMessages.add(message);
        return "Сообщение успешно опубликовано!";
    }

    @PutMapping("/messages/{index}")
    public synchronized String updateMessage(@PathVariable int index,
                                             @RequestBody String message) {
        if (index >= 0 && index < userMessages.size()) {
            userMessages.set(index, message);
            return "Сообщение успешно изменено!";
        }
        return "Сообщение с индексом " + index + " не найдено";
    }

    @DeleteMapping("/messages/{index}")
    public synchronized String deleteMessage(@PathVariable int index) {
        if (index >= 0 && index < userMessages.size()) {
            userMessages.remove(index);
            return "Сообщение успешно удалено!";
        }
        return "Сообщение с индексом " + index + " не найдено";
    }

    @DeleteMapping("/messages")
    public synchronized String clearAllMessages() {
        userMessages.clear();
        return "Все сообщения успешно удалены!";
    }

    @GetMapping("/messages/count")
    public synchronized int countMessages() {
        return userMessages.size();
    }
}
