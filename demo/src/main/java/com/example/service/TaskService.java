package com.example.service;

import com.example.model.Task;
import com.example.model.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAllTasks();

    Optional<Task> getTaskById(Long id);

    Optional<Task> getTaskByTitle(String title);

    Task saveNewTask(TaskDTO taskDTO);

    Task updateTask(Task oldTask, TaskDTO newTaskDTO);

    void deleteTask(Task task);

    Task assignUserToTask(Long taskId, Long userId);

    Task unassignUserFromTask(Long taskId);

    int getTaskProgress(Long taskId);
}

