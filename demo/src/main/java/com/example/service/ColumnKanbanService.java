package com.example.service;

import com.example.model.*;

import java.util.List;
import java.util.Optional;

public interface ColumnKanbanService {
    List<ColumnKanban> getAllColumns();
    Optional<ColumnKanban> getColumnById(Long id);

    Optional<ColumnKanban> getColumnByTitle(String title);

    ColumnKanban saveNewColumn(ColumnKanbanDTO columnKanbanDTO);

    ColumnKanban updateColumn(ColumnKanban oldColumn, ColumnKanbanDTO newTColumnDTO);

    void deleteColumn(ColumnKanban columnKanban);

    ColumnKanban addNewTaskToColumn(Long columnId, TaskDTO taskDTO);


}
