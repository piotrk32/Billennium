package com.example.service;
import com.example.model.*;
import com.example.repository.ColumnKanbanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColumnKanbanServiceImplementation implements ColumnKanbanService {
    private  ColumnKanbanRepository columnKanbanRepository;
    @Override
    @Transactional
    public List<ColumnKanban> getAllColumns() {
        List<ColumnKanban> columnsList = new ArrayList<>();
        columnKanbanRepository.findAll().forEach(columnsList::add);
        return columnsList;
    }

    @Override
    @Transactional
    public Optional<ColumnKanban> getColumnById(Long id) {
        return columnKanbanRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<ColumnKanban> getColumnByTitle(String title) {
        return columnKanbanRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public ColumnKanban saveNewColumn(ColumnKanbanDTO columnKanbanDTO) {
        return columnKanbanRepository.save(convertDTOToColumn(columnKanbanDTO));
    }

    @Override
    @Transactional
    public ColumnKanban updateColumn(ColumnKanban oldColumn, ColumnKanbanDTO newTColumnDTO) {
        return columnKanbanRepository.save(updateColumnFromDTO(oldColumn, newTColumnDTO));
    }

    @Override
    @Transactional
    public void deleteColumn(ColumnKanban columnKanban) {
        columnKanbanRepository.delete(columnKanban);

    }
    //CHCK
    @Override
    @Transactional
    public ColumnKanban addNewTaskToColumn(Long kanban_column_id, TaskDTO taskDTO) {
        ColumnKanban columnKanban = columnKanbanRepository.findById(kanban_column_id).get();
        columnKanban.addTaskToColumn(convertDTOToTask(taskDTO));
        return columnKanbanRepository.save(columnKanban);
    }
    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        return task;
    }

    private ColumnKanban updateColumnFromDTO(ColumnKanban columnKanban, ColumnKanbanDTO columnKanbanDTO){
        if(Optional.ofNullable(columnKanbanDTO.getId()).isPresent()){
            columnKanban.setId(columnKanbanDTO.getId());
        }

        if (Optional.ofNullable((columnKanbanDTO.getColumnTitle())).isPresent()) {
            columnKanban.setColumnTitle(columnKanbanDTO.getColumnTitle());
        }
        if(Optional.ofNullable(columnKanbanDTO.getTaskList()).isPresent()){
            columnKanban.setTasks(columnKanbanDTO.getTaskList());
        }
        return columnKanban;
    }
    private ColumnKanban convertDTOToColumn(ColumnKanbanDTO columnKanbanDTO) {
        ColumnKanban columnKanban = new ColumnKanban();
        columnKanban.setColumnTitle(columnKanbanDTO.getColumnTitle());
        columnKanban.setId(columnKanban.getId());
        columnKanban.setTasks(columnKanbanDTO.getTaskList());
        return columnKanban;
    }

}
