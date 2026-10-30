package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String q, Model model) {
        model.addAttribute("tasks", taskService.getTasks(q));
        model.addAttribute("query", q.strip());
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam String title,
                          @RequestParam(defaultValue = "") String description,
                          RedirectAttributes attributes) {
        try {
            taskService.addTask(title, description);
            attributes.addFlashAttribute("message", "задача добавлена");
        } catch (IllegalArgumentException exception) {
            attributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable Long id, @RequestParam boolean completed,
                             RedirectAttributes attributes) {
        taskService.updateTask(id, completed);
        attributes.addFlashAttribute("message", "статус изменён");
        return "redirect:/";
    }

    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id, RedirectAttributes attributes) {
        taskService.deleteTask(id);
        attributes.addFlashAttribute("message", "задача удалена");
        return "redirect:/";
    }
}
