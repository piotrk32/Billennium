package com.example.service;

import com.example.model.SubTask;
import com.example.model.SubTaskDTO;
import com.example.model.Task;

import java.util.List;
import java.util.Optional;

public interface SubTaskService {
    List<SubTask> getAllSubTasks();
    Optional<SubTask> getSubTaskById(Long id);
    SubTask saveNewSubTask(SubTaskDTO subTaskDTO);
    Optional<SubTask> getSubTaskByTitle(String title);
    SubTask updateSubTask(SubTask oldSubTask, SubTaskDTO newSubTaskDTO);
    Task assignSubTaskToTask(Long subTaskId, Long taskId);
    Task unassignSubTaskToTask(Long taskId);
    void deleteSubTask(SubTask subTask);

}
