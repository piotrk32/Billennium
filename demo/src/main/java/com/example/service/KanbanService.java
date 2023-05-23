package com.example.service;

import com.example.model.*;

import java.util.List;
import java.util.Optional;

public interface KanbanService {
    List<Kanban> getAllKanbanBoards();

    Optional<Kanban> getKanbanById(Long id);

    Optional<Kanban> getKanbanByTitle(String title);

    Kanban saveNewKanban(KanbanDTO kanbanDTO);

    Object updateKanban(Long id, KanbanDTO kanbanDTO);

    void deleteKanban(Kanban kanban);

    Kanban addNewColumnToKanban(Long kanbanId, ColumnKanbanDTO columnKanbanDTO);

    Kanban assignUserToKanban(Long kanbanId, Long userId);

    Kanban unassignUserFromKanban(Long kanbanId, Long userId);


}
