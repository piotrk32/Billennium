package com.example.controller;

import com.example.model.*;
import com.example.service.TaskService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
    private final UserService userService;
    private final TaskService taskService;

//    @GetMapping("/")
//    public ResponseEntity<List<Users>> getAllUsers() {
//        List<Users> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
    @GetMapping("/")
    public ResponseEntity<List<UsersDTO>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        List<UsersDTO> usersDTO = users.stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usersDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {
            Users users = optionalUser.get();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        Optional<Users> optionalUser = userService.getUserByUsername(username);
        if (optionalUser.isPresent()) {
            Users users = optionalUser.get();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Users> saveNewUser(@RequestBody UsersDTO usersDTO) {
        Users newUsers = userService.saveNewUser(usersDTO);
        return ResponseEntity.ok(newUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody UsersDTO usersDTO) {
        Users updatedUsers = userService.updateUser(id, usersDTO);
        return ResponseEntity.ok(updatedUsers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks(@PathVariable Long userId) {
        List<TaskDTO> userTasks = userService.getUserTasks(userId);
        return ResponseEntity.ok(userTasks);
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<TaskDTO> addUserTask(@PathVariable Long userId, @RequestBody TaskDTO taskDTO) {
        TaskDTO addedTask = userService.addUserTask(userId, taskDTO);
        return ResponseEntity.ok(addedTask);
    }

//    @PutMapping("/{userId}/tasks/{taskId}")
//    public ResponseEntity<TaskDTO> updateUserTask(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
//        TaskDTO updatedTask = userService.updateUser(userId, taskId, taskDTO);
//        return ResponseEntity.ok(updatedTask);
//    }

//    @DeleteMapping("/{userId}/tasks/{taskId}")
//    public ResponseEntity<Void> deleteUserTask(@PathVariable Long userId, @PathVariable Long taskId) {
//        userService.deleteUserTask(userId, taskId);
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping("/{userId}/taskLimit")
    public ResponseEntity<?> setTaskLimit(@PathVariable Long userId, @RequestParam int limit) {
        try {
            userService.setUserLimitTask(userId, limit);
            return ResponseEntity.ok("Task limit set successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set task limit.");
        }
    }
    private UsersDTO convertUserToUserDTO(Users user) {
        UsersDTO userDTO = new UsersDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setTaskIds(user.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList()));
        userDTO.setKanbanIds(user.getKanban().stream()
                .map(Kanban::getId)
                .collect(Collectors.toList()));
        userDTO.setTaskLimit(user.getTaskLimit());

        return userDTO;
    }




}
