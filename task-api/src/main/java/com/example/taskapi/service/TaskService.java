package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public Task create(Task task) {
        return repo.save(task);
    }

    public List<Task> getAll() {
        return repo.findAll();
    }

    public Task toggleCompleted(Long id) {
        Task t = repo.findById(id).orElseThrow();
        t.setCompleted(!t.isCompleted());
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
