package com.example.service;


import com.example.model.TaskDTO;
import com.example.model.UsersDTO;
import com.example.model.Users;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Users> getAllUsers();

    Optional<Users> getUserById(Long id);

    Optional<Users> getUserByUsername(String username);


    Users saveNewUser(UsersDTO usersDTO);

    Users updateUser(Long id, UsersDTO usersDTO);

    void deleteUser(Long id);
    List<TaskDTO> getUserTasks(Long userId);

    TaskDTO addUserTask(Long userId, TaskDTO taskDTO);

//    TaskDTO updateUserTask(Long userId, Long taskId, TaskDTO taskDTO);

//    void deleteUserTask(Long userId, Long taskId);

    Users saveUser(Users user);

//    @Transactional
//    void setTaskLimit(Long userId, int limit);

    @Transactional
    void setUserLimitTask(Long userId, Integer limit);


//    void setTaskLimit(Long userId, int limit);
}
