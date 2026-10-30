package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository {

    private final Map<Long, Task> tasks = new LinkedHashMap<>();
    private long nextId = 1;

    public synchronized void addTask(String title, String description) {
        Task task = new Task(nextId++, title, description);
        tasks.put(task.getId(), task);
    }

    public synchronized List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public synchronized boolean updateTask(Long id, boolean completed) {
        Task task = tasks.get(id);
        if (task == null) {
            return false;
        }
        task.setCompleted(completed);
        return true;
    }

    public synchronized boolean deleteTask(Long id) {
        return tasks.remove(id) != null;
    }
}
