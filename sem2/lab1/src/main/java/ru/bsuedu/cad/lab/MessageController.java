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
        return "Hello, World!";
    }

    @GetMapping("/messages")
    public synchronized List<String> getAllMessages() {
        return new ArrayList<>(userMessages);
    }

    @PostMapping("/messages")
    public synchronized String publishMessage(@RequestBody String message) {
        userMessages.add(message);
        return "Message published successfully!";
    }

    @PutMapping("/messages/{index}")
    public synchronized String updateMessage(@PathVariable int index,
                                             @RequestBody String message) {
        if (index >= 0 && index < userMessages.size()) {
            userMessages.set(index, message);
            return "Message updated successfully!";
        }
        return "Message not found at index " + index;
    }

    @DeleteMapping("/messages/{index}")
    public synchronized String deleteMessage(@PathVariable int index) {
        if (index >= 0 && index < userMessages.size()) {
            userMessages.remove(index);
            return "Message deleted successfully!";
        }
        return "Message not found at index " + index;
    }

    @DeleteMapping("/messages")
    public synchronized String clearAllMessages() {
        userMessages.clear();
        return "All messages deleted successfully!";
    }

    @GetMapping("/messages/count")
    public synchronized int countMessages() {
        return userMessages.size();
    }
}
