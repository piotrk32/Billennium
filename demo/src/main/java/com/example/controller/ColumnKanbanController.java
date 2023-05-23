package com.example.controller;

import com.example.model.ColumnKanban;
import com.example.model.ColumnKanbanDTO;
import com.example.model.TaskDTO;
import com.example.service.ColumnKanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/col")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class ColumnKanbanController  {

    private final ColumnKanbanService columnKanbanService;

    @GetMapping("/")
    public ResponseEntity<?> getAllColumns(){
        try {
            return new ResponseEntity<>(
                    columnKanbanService.getAllColumns(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColumn(@PathVariable Long id){
        try {
            Optional<ColumnKanban> optColumn = columnKanbanService.getColumnById(id);
            if (optColumn.isPresent()) {
                return new ResponseEntity<>(
                        optColumn.get(),
                        HttpStatus.OK);
            } else {
                return noColumnFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }
    @GetMapping("/title")
    public ResponseEntity<?> getColumnByTitle(@RequestParam String title){
        try {
            Optional<ColumnKanban> optColumn = columnKanbanService.getColumnByTitle(title);
            if (optColumn.isPresent()) {
                return new ResponseEntity<>(
                        optColumn.get(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No column found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> createColumn(@RequestBody ColumnKanbanDTO columnKanbanDTO){
        try {
            return new ResponseEntity<>(
                    columnKanbanService.saveNewColumn(columnKanbanDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }
    //CHECK
    @PostMapping("/{columnId}/addTask")
    public ResponseEntity<?> addTaskToColumn(@PathVariable Long columnId, @RequestBody TaskDTO taskDTO) {
        try {
            ColumnKanban columnKanban = columnKanbanService.addNewTaskToColumn(columnId, taskDTO);
            if (columnKanban != null) {
                return new ResponseEntity<>(columnKanban, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Could not add task to column with id: " + columnId, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColumn(@PathVariable Long id, @RequestBody ColumnKanbanDTO columnKanbanDTO){
        try {
            Optional<ColumnKanban> oldColumn = columnKanbanService.getColumnById(id);
            if (oldColumn.isPresent()) {
                return new ResponseEntity<>(
                        columnKanbanService.updateColumn(oldColumn.get(), columnKanbanDTO),
                        HttpStatus.OK);
            } else {
                return noColumnFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColumn(@PathVariable Long id){
        try {
            Optional<ColumnKanban> optColumn = columnKanbanService.getColumnById(id);
            if (optColumn.isPresent()) {
                columnKanbanService.deleteColumn(optColumn.get());
                return new ResponseEntity<>(
                        String.format("Kanban with id: %d was deleted", id),
                        HttpStatus.OK);
            } else {
                return noColumnFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }


    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noColumnFoundResponse(Long id){
        return new ResponseEntity<>("No column found with id: " + id, HttpStatus.NOT_FOUND);
    }

}
