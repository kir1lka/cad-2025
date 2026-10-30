package ru.bsuedu.cad.lab;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(String title, String description) {
        String cleanTitle = title.strip();
        String cleanDescription = description.strip();
        if (cleanTitle.isEmpty()) {
            throw new IllegalArgumentException("введите название задачи");
        }
        if (cleanTitle.length() > 100 || cleanDescription.length() > 1000) {
            throw new IllegalArgumentException("название до 100 символов, описание до 1000");
        }
        taskRepository.addTask(cleanTitle, cleanDescription);
    }

    public List<Task> getTasks(String query) {
        String search = query.strip().toLowerCase(Locale.ROOT);
        return taskRepository.getAllTasks().stream()
                .filter(task -> task.getTitle().toLowerCase(Locale.ROOT).contains(search)
                        || task.getDescription().toLowerCase(Locale.ROOT).contains(search))
                .toList();
    }

    public void updateTask(Long id, boolean completed) {
        if (!taskRepository.updateTask(id, completed)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "задача не найдена");
        }
    }

    public void deleteTask(Long id) {
        if (!taskRepository.deleteTask(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "задача не найдена");
        }
    }
}
