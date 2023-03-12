package com.example.service;

import com.example.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface KanbanService {
    List<Kanban> getAllKanbanBoards();

    Optional<Kanban> getKanbanById(Long id);

    Optional<Kanban> getKanbanByTitle(String title);

    Kanban saveNewKanban(KanbanDTO kanbanDTO);

    Object updateKanban(Kanban oldKanban, KanbanDTO newKanbanDTO);

    void deleteKanban(Kanban kanban);

    Kanban addNewTaskToKanban(Long kanbanId, TaskDTO taskDTO);

    Kanban addNewColumnToKanban(Long kanbanId, ColumnKanbanDTO columnKanbanDTO);

}
