package com.example.service;

import com.example.model.*;
import com.example.repository.SubTaskRepository;
import com.example.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubTaskServiceImplementation implements SubTaskService {
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public List<SubTask> getAllSubTasks() {
        List<SubTask> subTasksList = new ArrayList<>();
        subTaskRepository.findAll().forEach(subTasksList::add);
        return subTasksList;
    }
    @Override
    @Transactional
    public Optional<SubTask> getSubTaskByTitle(String title) {
        return subTaskRepository.findBysubTaskTitle(title);
    }

    @Override
    @Transactional
    public Optional<SubTask> getSubTaskById(Long id) {
        return subTaskRepository.findById(id);
    }
    @Override
    @Transactional
    public void deleteSubTask(SubTask subTask) {
        subTaskRepository.delete(subTask);
    }

    @Override
    @Transactional
    public SubTask updateSubTask(SubTask oldSubTask, SubTaskDTO newSubTaskDTO) {
        return subTaskRepository.save(updateSubTaskFromDTO(oldSubTask, newSubTaskDTO));
    }
    private SubTask updateSubTaskFromDTO(SubTask subTask, SubTaskDTO subTaskDTO) throws IllegalArgumentException {
        if (subTaskDTO.getSubtaskTitle() != null) {
            subTask.setSubTaskTitle(subTaskDTO.getSubtaskTitle());
        }

        if (subTaskDTO.getDescription() != null) {
            subTask.setDescription(subTaskDTO.getDescription());
        }

        if (subTaskDTO.getStatus() != null) {
            subTask.setStatus(subTaskDTO.getStatus());
        }

        if (subTaskDTO.getTaskId() != null) {
            Optional<SubTask> optSubTask = subTaskRepository.findById(subTaskDTO.getTaskId());
            if (!optSubTask.isPresent()) {
                return null;
            }
            subTask.setTask(optSubTask.get().getTask());
        }

        return subTask;
    }

    @Override
    @Transactional
    public Task assignSubTaskToTask(Long subTaskId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + taskId));
        List<SubTask> subTask = (List<SubTask>) subTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + subTaskId));

        task.setSubTasks(subTask);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task unassignSubTaskToTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        task.setUsers(null);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public SubTask saveNewSubTask(SubTaskDTO subTaskDTO) {
        Task task = taskRepository.findById(subTaskDTO.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + subTaskDTO.getTaskId()));
        SubTask subTask = new SubTask();
        subTask.setSubTaskTitle(subTaskDTO.getSubtaskTitle());
        subTask.setDescription(subTaskDTO.getDescription());
        subTask.setStatus(subTaskDTO.getStatus());
        subTask.setTask(task);
        return subTaskRepository.save(subTask);
    }



}
