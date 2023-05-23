package com.example.service;

import com.example.model.*;
import com.example.repository.ColumnKanbanRepository;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final ColumnKanbanRepository columnKanbanRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<Task> getAllTasks() {
        List<Task> tasksList = new ArrayList<>();
        taskRepository.findAll().forEach(tasksList::add);
        return tasksList;
    }

    @Override
    @Transactional
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Task> getTaskByTitle(String title) {
        return taskRepository.findByTaskTitle(title);
    }

    @Override
    @Transactional
    public Task saveNewTask(TaskDTO taskDTO) {
        return taskRepository.save(convertDTOToTask(taskDTO));
    }

    @Override
    @Transactional
    public Task updateTask(Task oldTask, TaskDTO newTaskDTO) {
        return taskRepository.save(updateTaskFromDTO(oldTask, newTaskDTO));
    }

    @Override
    @Transactional
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }



    private Task updateTaskFromDTO(Task task, TaskDTO taskDTO) throws IllegalArgumentException {
        if (taskDTO.getTitle() != null) {
            task.setTaskTitle(taskDTO.getTitle());
        }

        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }

        if (taskDTO.getColor() != null) {
            task.setColor(taskDTO.getColor());
        }
//        if (taskDTO.getUserIds() != null){
//            task.setUsers(taskDTO.getUserIds());
//        }


        if (taskDTO.getKanban_column_id() != null) {
            Optional<ColumnKanban> optKanbanColumn = columnKanbanRepository.findById(taskDTO.getKanban_column_id());
            if (!optKanbanColumn.isPresent()) {
                return null;
            }
            task.setColumnKanban(optKanbanColumn.get());
        }

        return task;
    }

    @Override
    @Transactional
    public Task assignUserToTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        task.getUsers().add(user);
//        task.setUsers(user);
        // sprawdzic czy zapisuje sie task w userze
        taskRepository.save(task);
        user.getTasks().add(task);
        userRepository.save(user);

        Integer limitTask = user.getTaskLimit();
        if (limitTask != null && user.getTasks().size() > limitTask) {
            ColumnKanban column = task.getColumnKanban();
            if (column != null) {
                column.setColorColumn("RED");
                columnKanbanRepository.save(column);
            }
        }

        return task;
    }

    @Override
    @Transactional
    public Task unassignUserFromTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        Users users = (Users) task.getUsers();
        task.setUsers(null);
        taskRepository.save(task);

        if (users != null) {
            Integer limitTask = users.getTaskLimit();
            if (limitTask != null && users.getTasks().size() <= limitTask) {
                ColumnKanban column = task.getColumnKanban();
                if (column != null) {
                    column.setColorColumn("DEFAULT");
                    columnKanbanRepository.save(column);
                }
            }
        }

        return task;
    }


    //CONVERTING
    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());

        // do walidacji
//        Optional<Users> optUser = userRepository.findById(taskDTO.getUserIds());
//        if (!optUser.isPresent()) {
//            throw new IllegalArgumentException("User with given id not exists.");
//        }

        //
//        task.setUsers((List<Users>) optUser.get());
        Optional<ColumnKanban> optKanbanColumn = columnKanbanRepository.findById(taskDTO.getKanban_column_id());
        if (!optKanbanColumn.isPresent())
            throw new IllegalArgumentException("Column with given id not exists.");
        task.setColumnKanban(optKanbanColumn.get());
        return task;
    }
}

