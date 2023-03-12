package com.example.service;


import com.example.model.*;
import com.example.repository.KanbanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class KanbanServiceImplementation implements KanbanService {

    private final KanbanRepository kanbanRepository;


    @Override
    @Transactional
    public List<Kanban> getAllKanbanBoards() {
        List<Kanban> kanbanList = new ArrayList<>();
        for (Kanban kanban : kanbanRepository.findAll()) {
            kanbanList.add(kanban);
        }
        return kanbanList;
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanById(Long id) {
        return kanbanRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanByTitle(String title) {
        return kanbanRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public Kanban saveNewKanban(KanbanDTO kanbanDTO) {
        return kanbanRepository.save(convertDTOToKanban(kanbanDTO));
    }

    @Override
    @Transactional
    public Object updateKanban(Kanban oldKanban, KanbanDTO newKanbanDTO) {
        oldKanban.setTitle(newKanbanDTO.getTitle());
        return kanbanRepository.save(oldKanban);
    }

    @Override
    @Transactional
    public void deleteKanban(Kanban kanban) {
        kanbanRepository.delete(kanban);
    }

    @Override
    @Transactional
    public Kanban addNewTaskToKanban(Long kanbanId, TaskDTO taskDTO) {
        Kanban kanban = kanbanRepository.findById(kanbanId).get();
        kanban.addTask(convertDTOToTask(taskDTO));
        return kanbanRepository.save(kanban);
    }
    //TOCHECK
    @Override
    @Transactional
    public Kanban addNewColumnToKanban(Long kanbanId, ColumnKanbanDTO columnKanbanDTO) {
        Kanban kanban = kanbanRepository.findById(kanbanId).get();
        kanban.addColumn(convertDTOToColumn(columnKanbanDTO));
        return kanbanRepository.save(kanban);
    }

    private Kanban convertDTOToKanban(KanbanDTO kanbanDTO){
        Kanban kanban = new Kanban();
        kanban.setTitle(kanbanDTO.getTitle());
        return kanban;
    }

    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        return task;
    }
    //TOCHECK
    private ColumnKanban convertDTOToColumn(ColumnKanbanDTO columnKanbanDTO) {
        ColumnKanban columnKanban = new ColumnKanban();
        columnKanban.setColumnTitle(columnKanbanDTO.getColumnTitle());
        columnKanban.setId(columnKanban.getId());
        columnKanban.setTasks(columnKanbanDTO.getTaskList());
        return columnKanban;
    }

}