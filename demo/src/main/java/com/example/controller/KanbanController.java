package com.example.controller;

import com.example.model.*;
import com.example.model.Users;
import com.example.service.KanbanService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test/")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")// do autoryzacji
@CrossOrigin(origins = "http://localhost:8080")
public class KanbanController {
    private final KanbanService kanbanService;
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getAllKanbans(){
        try {
            return new ResponseEntity<>(
                    kanbanService.getAllKanbanBoards(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getKanbanById(@PathVariable Long id){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        optKanban.get(),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }


    @GetMapping("/title")
    public ResponseEntity<?> getKanbanByTitle(@RequestParam String title){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanByTitle(title);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        optKanban.get(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No kanban found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createKanban(@RequestBody KanbanDTO kanbanDTO){
        try {
            return new ResponseEntity<>(
                    kanbanService.saveNewKanban(kanbanDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateKanban(@PathVariable Long id, @RequestBody KanbanDTO kanbanDTO){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        kanbanService.updateKanban(optKanban.get().getId(), kanbanDTO),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKanban(@PathVariable Long id){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                kanbanService.deleteKanban(optKanban.get());
                return new ResponseEntity<>(
                        String.format("Kanban with id: %d was deleted", id),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }
    @GetMapping("/{kanbanId}/col/")
    public ResponseEntity<?> getAllColumnsInKanban(@PathVariable Long kanbanId){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(kanbanId);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        optKanban.get().getColumnsKanban(),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(kanbanId);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }


    @PostMapping("/{kanbanId}/addColumn")
    public ResponseEntity<Kanban> addColumnToKanban(@PathVariable Long kanbanId, @RequestBody ColumnKanbanDTO columnKanbanDTO) {
        Kanban kanban = kanbanService.addNewColumnToKanban(kanbanId, columnKanbanDTO);
        if (kanban.getId() != null) {
            return ResponseEntity.ok(kanban);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{kanbanId}/addUser/{userId}")
    public ResponseEntity<?> addUserToKanban(@PathVariable Long kanbanId, @PathVariable Long userId) {
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(kanbanId);
            Optional<Users> optUser = userService.getUserById(userId);

            if (optKanban.isPresent() && optUser.isPresent()) {
                Kanban kanban = optKanban.get();
                Users user = optUser.get();

                kanban.getUsers().add(user);
                user.setKanban((List<Kanban>) kanban);

                UsersDTO usersDTO = convertUserToUserDTO(user);
                userService.updateUser(user.getId(), usersDTO);

                KanbanDTO kanbanDTO = convertKanbanToKanbanDTO(kanban);
                kanbanService.updateKanban(kanban.getId(), kanbanDTO);

                return new ResponseEntity<>(kanban, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Kanban or user not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/{kanbanId}/assignUser/{userId}")
    public ResponseEntity<?> assignUserToKanban(@PathVariable Long kanbanId, @PathVariable Long userId) {
        try {
            Kanban kanban = kanbanService.assignUserToKanban(kanbanId, userId);
            return ResponseEntity.ok(kanban);
        } catch (KanbanNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PostMapping("/{kanbanId}/unassignUser/{userId}")
    public ResponseEntity<?> unassignUserFromKanban(@PathVariable Long kanbanId, @PathVariable Long userId) {
        try {
            Kanban kanban = kanbanService.unassignUserFromKanban(kanbanId, userId);
            return ResponseEntity.ok(kanban);
        } catch (KanbanNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }


    @PostMapping("/{kanbanId}/removeUser/{userId}")
    public ResponseEntity<?> removeUserFromKanban(@PathVariable Long kanbanId, @PathVariable Long userId) {
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(kanbanId);
            Optional<Users> optUser = userService.getUserById(userId);

            if (optKanban.isPresent() && optUser.isPresent()) {
                Kanban kanban = optKanban.get();
                Users user = optUser.get();

                if (kanban.getUsers().contains(user)) {
                    kanban.getUsers().remove(user);
                    user.setKanban(null);

                    userService.updateUser(user.getId(), convertUserToUserDTO(user));
                    kanbanService.updateKanban(kanban.getId(), convertKanbanToKanbanDTO(kanban));

                    return new ResponseEntity<>(kanban, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("User not found in the Kanban.", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Kanban or user not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }





    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noKanbanFoundResponse(Long id){
        return new ResponseEntity<>("No kanban found with id: " + id, HttpStatus.NOT_FOUND);
    }

    public class KanbanNotFoundException extends RuntimeException {
        public KanbanNotFoundException(String message) {
            super(message);
        }
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    private UsersDTO convertUserToUserDTO(Users users) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setId(users.getId());
        usersDTO.setFirstName(users.getUsername());
//        usersDTO.setPassword(users.getPassword());
        // Set other properties from User to UserDTO
        return usersDTO;
    }

    private KanbanDTO convertKanbanToKanbanDTO(Kanban kanban) {
        KanbanDTO kanbanDTO = new KanbanDTO();
        kanbanDTO.setId(kanban.getId());
        kanbanDTO.setKanbanTitle(kanban.getKanbanTitle());
        // Set other properties from Kanban to KanbanDTO, including lists of columns and users
        return kanbanDTO;
    }

}
