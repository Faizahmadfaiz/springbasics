package com.scaler.springbasics;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class TaskController {
    private final List<Task> taskList;
    private AtomicInteger taskId = new AtomicInteger();

    public TaskController() {
        taskList = new ArrayList<>();
        taskList.add(new Task(taskId.incrementAndGet(),  "Task 1", "Description 1", new Date()));
        taskList.add(new Task(taskId.incrementAndGet(),  "Task 2", "Description 2", new Date()));
        taskList.add(new Task(taskId.incrementAndGet(),  "Task 3", "Description 3", new Date()));
    }

    @GetMapping("/tasks")
    List<Task> getTasks() {
        return taskList;
    }

    @PostMapping("/tasks")
    Task createTask(@RequestBody Task task) {
          var newTask = new Task(taskId.incrementAndGet(), task.getTitle(), task.getDescription(), task.getDueDate());
          taskList.add(newTask);
          return newTask;
    }


    @GetMapping("/tasks/{id}")
    Task getTask(@PathVariable("id") Integer id) {
        var filteredTask = taskList.stream()
                .filter(task -> task.getId().equals(id))
                .collect(Collectors.toList());
        if (filteredTask.size() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        return filteredTask.get(0);
    }

    @DeleteMapping("/tasks/{id}")
    Task deleteTask(@PathVariable("id") Integer id) {
        int index = -1;
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        } else {
            var task = taskList.get(index);
            taskList.remove(index);
            return task;
        }
    }

    @PatchMapping("/tasks/{id}")
    Task updateTask(@PathVariable("id") Integer id, @RequestBody Task task) {
        int index = -1;
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        } else {
            var matchedTask = taskList.get(index);
            if (task.getTitle() != null) {
                matchedTask.setTitle(task.getTitle());
            }
            if (task.getDescription() != null) {
                matchedTask.setDescription(task.getDescription());
            }
            if (task.getDueDate() != null) {
                matchedTask.setDueDate(task.getDueDate());
            }
            return matchedTask;
        }
    }
}
