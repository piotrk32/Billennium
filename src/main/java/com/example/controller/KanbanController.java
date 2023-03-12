package com.example.controller;

import com.example.model.Kanban;
import com.example.model.KanbanDTO;
import com.example.model.TaskDTO;
import com.example.service.KanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/test/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class KanbanController {
    private final KanbanService kanbanService;

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
    public ResponseEntity<?> getKanban(@PathVariable Long id){
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
                        kanbanService.updateKanban(optKanban.get(), kanbanDTO),
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

    @GetMapping("/{kanbanId}/tasks/")
    public ResponseEntity<?> getAllTasksInKanban(@PathVariable Long kanbanId){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(kanbanId);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        optKanban.get().getTasks(),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(kanbanId);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/{kanbanId}/tasks/")
    public ResponseEntity<?> createTaskAssignedToKanban(@PathVariable Long kanbanId, @RequestBody TaskDTO taskDTO){
        try {
            return new ResponseEntity<>(
                    kanbanService.addNewTaskToKanban(kanbanId, taskDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/hehe")
    public String hello() {
        return "Hello, World!";
    }

    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noKanbanFoundResponse(Long id){
        return new ResponseEntity<>("No kanban found with id: " + id, HttpStatus.NOT_FOUND);
    }

}
