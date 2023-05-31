package com.example.controller;

import com.example.model.RowKanban;
import com.example.model.RowKanbanDTO;
import com.example.model.TaskDTO;
import com.example.service.RowKanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/row-kanban")
@RequiredArgsConstructor
public class RowKanbanController {
    private final RowKanbanService rowKanbanService;

    @GetMapping("/")
    public ResponseEntity<List<RowKanban>> getAllRowsKanban() {
        List<RowKanban> rowsKanban = rowKanbanService.getAllRows();
        return new ResponseEntity<>(rowsKanban, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RowKanban> getRowKanbanById(@PathVariable Long id) {
        Optional<RowKanban> rowKanban = rowKanbanService.getRowById(id);
        return rowKanban.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<RowKanban> createRowKanban(@RequestBody RowKanbanDTO rowKanbanDTO) {
        RowKanban createdRowKanban = rowKanbanService.createRow(rowKanbanDTO);
        return new ResponseEntity<>(createdRowKanban, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RowKanban> updateRowKanban(@PathVariable Long id, @RequestBody RowKanbanDTO rowKanbanDTO) {
        Optional<RowKanban> rowKanban = rowKanbanService.getRowById(id);
        if (rowKanban.isPresent()) {
            RowKanban updatedRowKanban = rowKanbanService.updateRow(id, rowKanbanDTO);
            return new ResponseEntity<>(updatedRowKanban, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRowKanban(@PathVariable Long id) {
        Optional<RowKanban> rowKanban = rowKanbanService.getRowById(id);
        if (rowKanban.isPresent()) {
            rowKanbanService.deleteRow(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{rowId}/addTask")
    public ResponseEntity<?> addTaskToRow(@PathVariable Long rowId, @RequestBody TaskDTO taskDTO) {
        try {
            RowKanban rowKanban = rowKanbanService.addNewTaskToRow(rowId, taskDTO);
            if (rowKanban != null) {
                return new ResponseEntity<>(rowKanban, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Could not add task to row with id: " + rowId, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
