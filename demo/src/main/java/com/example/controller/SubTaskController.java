package com.example.controller;

import com.example.model.SubTask;
import com.example.model.SubTaskDTO;
import com.example.service.SubTaskService;
import com.example.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subtask")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class SubTaskController {
    private final SubTaskService subTaskService;
    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping("/")
    public ResponseEntity<?> getAllTasks() {
        try {
            List<SubTask> subTasks = subTaskService.getAllSubTasks();
            logger.info("Found {} tasks", subTasks.size());
            return new ResponseEntity<>(subTasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while fetching subtasks: {}", e.getMessage());
            return errorResponse();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubTaskById(@PathVariable Long id){
        try {
            Optional<SubTask> optSubTask = subTaskService.getSubTaskById(id);
            if (optSubTask.isPresent()) {
                return new ResponseEntity<>(
                        optSubTask.get(),
                        HttpStatus.OK);
            } else {
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }
    @GetMapping("/title")
    public ResponseEntity<?> getSubTaskByTitle(@RequestParam String title){
        try {
            Optional<SubTask> optSubTask = subTaskService.getSubTaskByTitle(title);
            if (optSubTask.isPresent()) {
                return new ResponseEntity<>(
                        optSubTask.get(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No task found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> createSubTask(@RequestBody SubTaskDTO subTaskDTO){
        try {
            return new ResponseEntity<>(
                    subTaskService.saveNewSubTask(subTaskDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubTask(@PathVariable Long id, @RequestBody SubTaskDTO subTaskDTO){
        try {
            Optional<SubTask> oldSubTask = subTaskService.getSubTaskById(id);
            if (oldSubTask.isPresent()) {
                return new ResponseEntity<>(
                        subTaskService.updateSubTask(oldSubTask.get(), subTaskDTO),
                        HttpStatus.OK);
            } else {
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubTask(@PathVariable Long id){
        try {
            Optional<SubTask> optSubTask = subTaskService.getSubTaskById(id);
            if (optSubTask.isPresent()) {
                subTaskService.deleteSubTask(optSubTask.get());
                return new ResponseEntity<>(String.format("Task with id: %d was deleted", id), HttpStatus.OK);
            } else {
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }







    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noTaskFoundResponse(Long id){
        return new ResponseEntity<>("No subtask found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
