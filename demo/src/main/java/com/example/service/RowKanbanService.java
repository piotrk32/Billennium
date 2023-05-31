package com.example.service;

import com.example.model.RowKanban;
import com.example.model.RowKanbanDTO;
import com.example.model.Task;
import com.example.model.TaskDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RowKanbanService {
    @Transactional
    List<RowKanban> getAllRows();

    @Transactional
    List<Task> getTasksInRow(Long rowId);

    @Transactional
    Optional<RowKanban> getRowById(Long id);

    @Transactional
    RowKanban createRow(RowKanbanDTO rowKanbanDTO);

    @Transactional
    RowKanban updateRow(Long id, RowKanbanDTO updatedRowKanbanDTO);

    @Transactional
    void deleteRow(Long id);

    @Transactional
    RowKanban addNewTaskToRow(Long row_id, TaskDTO taskDTO);

    @Transactional
    void setRowLimitTask(Long rowId, Integer limit);
}
