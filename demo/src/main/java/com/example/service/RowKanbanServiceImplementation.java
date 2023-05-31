package com.example.service;

import com.example.model.*;
import com.example.repository.RowKanbanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RowKanbanServiceImplementation implements RowKanbanService {
    private final RowKanbanRepository rowKanbanRepository;

    @Override
    @Transactional
    public List<RowKanban> getAllRows() {
        return rowKanbanRepository.findAll();
    }

    @Override
    @Transactional
    public List<Task> getTasksInRow(Long rowId) {
        RowKanban row = rowKanbanRepository.findById(rowId)
                .orElseThrow(() -> new RuntimeException("RowKanban not found with id: " + rowId));

        List<Task> tasks = row.getTaskList();
        List<ColumnKanban> columns = new ArrayList<>();

        for (Task task : tasks) {
            ColumnKanban column = task.getColumnKanban();
            if (column != null) {
                columns.add(column);
            }
        }

        List<Task> tasksInRow = new ArrayList<>();
        for (ColumnKanban column : columns) {
            tasksInRow.addAll(column.getTaskList());
        }

        return tasksInRow;
    }

    @Override
    @Transactional
    public Optional<RowKanban> getRowById(Long id) {
        return rowKanbanRepository.findById(id);
    }

    @Override
    @Transactional
    public RowKanban createRow(RowKanbanDTO rowKanbanDTO) {
        RowKanban rowKanban = convertDTOToRow(rowKanbanDTO);
        return rowKanbanRepository.save(rowKanban);
    }

    @Override
    @Transactional
    public RowKanban updateRow(Long id, RowKanbanDTO updatedRowKanbanDTO) {
        Optional<RowKanban> existingRowKanban = rowKanbanRepository.findById(id);
        if (existingRowKanban.isPresent()) {
            RowKanban rowKanban = existingRowKanban.get();
            rowKanban.setRowTitle(updatedRowKanbanDTO.getRowTitle());
            rowKanban.setTaskLimit(updatedRowKanbanDTO.getTaskLimit());
            // Set other properties of rowKanban from updatedRowKanbanDTO if needed
            return rowKanbanRepository.save(rowKanban);
        } else {
            throw new IllegalArgumentException("RowKanban not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteRow(Long id) {
        Optional<RowKanban> existingRowKanban = rowKanbanRepository.findById(id);
        if (existingRowKanban.isPresent()) {
            rowKanbanRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("RowKanban not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public RowKanban addNewTaskToRow(Long row_id, TaskDTO taskDTO) {
        Optional<RowKanban> optionalRowKanban = rowKanbanRepository.findById(row_id);
        if (optionalRowKanban.isPresent()) {
            RowKanban rowKanban = optionalRowKanban.get();
            rowKanban.addTaskToRow(convertDTOToTask(taskDTO));
            return rowKanbanRepository.save(rowKanban);
        } else {
            throw new NoSuchElementException("RowKanban with ID: " + row_id + " not found.");
        }
    }


    @Override
    @Transactional
    public void setRowLimitTask(Long rowId, Integer limit) {
        RowKanban rowKanban = rowKanbanRepository.findById(rowId)
                .orElseThrow(() -> new IllegalArgumentException("Row not found with id: " + rowId));
//        // null nieskonczonosc
        rowKanban.setTaskLimit(limit);

        rowKanbanRepository.save(rowKanban);
    }

    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setSubTasks(taskDTO.getSubTasks());
        return task;
    }



    private RowKanban convertDTOToRow(RowKanbanDTO rowKanbanDTO) {
        RowKanban rowKanban = new RowKanban();
        rowKanban.setRowTitle(rowKanbanDTO.getRowTitle());
        rowKanban.setTaskLimit(rowKanbanDTO.getTaskLimit());
        rowKanban.setKanbanId(rowKanbanDTO.getKanbanId());
        return rowKanban;
    }
}
