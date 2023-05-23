package com.example.service;


import com.example.model.*;
import com.example.model.Users;
import com.example.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class KanbanServiceImplementation implements KanbanService {

    private final TaskRepository taskRepository;
    private final KanbanRepository kanbanRepository;
    private final UserRepository userRepository;
    private final ColumnKanbanRepository columnKanbanRepository;
    private final RowKanbanRepository rowKanbanRepository;

    @Override
    @Transactional
    public List<Kanban> getAllKanbanBoards() {
        return kanbanRepository.findAll();

    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanById(Long id) {
        return kanbanRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanByTitle(String title) {
        return kanbanRepository.findByKanbanTitle(title);
    }

    @Override
    @Transactional
    public Kanban saveNewKanban(KanbanDTO kanbanDTO) {
        Kanban kanban = convertDTOToKanban(kanbanDTO);
        List<Users> users = convertDTOToUser(kanbanDTO.getUsers()); // Konwertuj UserDTO na User
        List<ColumnKanban> columnKanban = convertDTOToColumnList(kanbanDTO.getColumnsKanban());
        List<RowKanban> rowKanbans = convertDTOToRowList(kanbanDTO.getRowsKanban());
        kanban.setUsers(userRepository.saveAll(users)); // Zapisz listę użytkowników do bazy danych
        kanban.setRowsKanban(rowKanbanRepository.saveAll(rowKanbans));
        kanban.setColumnsKanban(columnKanbanRepository.saveAll(columnKanban));
        return kanbanRepository.save(kanban);
    }

    @Override
    @Transactional
    public Kanban updateKanban(Long id, KanbanDTO kanbanDTO) {
        Optional<Kanban> optionalKanban = kanbanRepository.findById(id);
        if (optionalKanban.isPresent()) {
            Kanban kanban = optionalKanban.get();
            kanban.setKanbanTitle(kanbanDTO.getKanbanTitle());

            // Update columns
            List<ColumnKanban> columns = new ArrayList<>();
            for (ColumnKanbanDTO columnDTO : kanbanDTO.getColumnsKanban()) {
                Optional<ColumnKanban> optionalColumn = kanban.getColumnsKanban()
                        .stream()
                        .filter(c -> c.getId().equals(columnDTO.getId()))
                        .findFirst();

                ColumnKanban column;
                if (optionalColumn.isPresent()) {
                    column = optionalColumn.get();
                } else {
                    column = new ColumnKanban();
                    column.setKanban(kanban);
                }

                column.setColumnTitle(columnDTO.getColumnTitle());
                columns.add(column);
            }
            kanban.setColumnsKanban(columns);

            // Update rows
            List<RowKanban> rows = new ArrayList<>();
            for (RowKanbanDTO rowKanban : kanbanDTO.getRowsKanban()) {
                Optional<RowKanban> optionalRow = kanban.getRowsKanban()
                        .stream()
                        .filter(r -> r.getId().equals(rowKanban.getRowId()))
                        .findFirst();

                RowKanban row;
                if (optionalRow.isPresent()) {
                    row = optionalRow.get();
                } else {
                    row = new RowKanban();
                    row.setKanbanId(kanban);
                }

                row.setRowTitle(rowKanban.getRowTitle());
                rows.add(row);
            }
            kanban.setRowsKanban(rows);

            return kanbanRepository.save(kanban);
        } else {
            throw new RuntimeException("Kanban not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteKanban(Kanban kanban) {
        kanbanRepository.delete(kanban);
    }

    //TOCHECK
    @Override
    @Transactional
    public Kanban addNewColumnToKanban(Long kanbanId, ColumnKanbanDTO columnKanbanDTO) {
        Optional<Kanban> optionalKanban = kanbanRepository.findById(kanbanId);
        if (optionalKanban.isPresent()) {
            Kanban kanban = optionalKanban.get();
            String columnTitle = columnKanbanDTO.getColumnTitle();

            // Sprawdzenie, czy kolumna o takiej samej nazwie już nie istnieje
            Optional<ColumnKanban> existingColumn = columnKanbanRepository.findByColumnTitle(columnTitle);
            if (existingColumn.isPresent()) {
                throw new IllegalArgumentException("Column with title '" + columnTitle + "' already exists.");
            }

            kanban.addColumn(convertDTOToColumn(columnKanbanDTO));
            return kanbanRepository.save(kanban);
        } else {
            throw new EntityNotFoundException("Kanban not found with id: " + kanbanId);
        }
    }

    private Kanban convertDTOToKanban(KanbanDTO kanbanDTO){
        Kanban kanban = new Kanban();
        kanban.setKanbanTitle(kanbanDTO.getKanbanTitle());
        kanban.setUsers(convertDTOToUser(kanbanDTO.getUsers()));
        return kanban;
    }

    @Override
    @Transactional
    public Kanban assignUserToKanban(Long kanbanId, Long userId) {
        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new RuntimeException("Kanban not found with id: " + kanbanId));
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (kanban.getUsers() == null) {
            kanban.setUsers(new ArrayList<>());
        }
        kanban.getUsers().add(users);
        return kanbanRepository.save(kanban);
    }

    @Override
    @Transactional
    public Kanban unassignUserFromKanban(Long kanbanId, Long userId) {
        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new RuntimeException("Kanban not found with id: " + kanbanId));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        kanban.getUsers().remove(user);
        return kanbanRepository.save(kanban);
    }


    //CONVERTING
    private ColumnKanban convertDTOToColumn(ColumnKanbanDTO columnKanbanDTO) {
        ColumnKanban columnKanban = new ColumnKanban();
        columnKanban.setColumnTitle(columnKanbanDTO.getColumnTitle());
        columnKanban.setId(columnKanbanDTO.getId());
        return columnKanban;
    }

    private RowKanban convertDTOToRow(RowKanbanDTO rowKanbanDTO) {
        RowKanban rowKanban = new RowKanban();
        rowKanban.setRowTitle(rowKanbanDTO.getRowTitle());
        rowKanban.setId(rowKanbanDTO.getRowId());
        rowKanban.setTaskLimit(rowKanbanDTO.getTaskLimit());
        return rowKanban;
    }

    private List<Users> convertDTOToUser(List<UsersDTO> usersDTOS) {
        List<Users> users = new ArrayList<>();
        for (UsersDTO usersDTO : usersDTOS) {
            Users user = new Users();
            user.setId(usersDTO.getId());
            user.setFirstName(usersDTO.getFirstName());
            user.setLastName(usersDTO.getLastName());
            // Set tasks for the user
            if (usersDTO.getTaskIds() != null) {
                List<Task> tasks = (List<Task>) taskRepository.findAllById(usersDTO.getTaskIds());
                user.setTasks(tasks);
            }
            //tochceck
             //Set kanban for the user
            if (usersDTO.getKanbanIds() != null) {
                Optional<Kanban> kanbanOptional = kanbanRepository.findById(usersDTO.getKanbanIds().get(0));
                kanbanOptional.ifPresent(kanban -> user.setKanban(List.of(kanban)));
            }
            users.add(user);
        }
        return users;
    }

    private List<ColumnKanban> convertDTOToColumnList(List<ColumnKanbanDTO> columnKanbanDTOList) {
        List<ColumnKanban> columnKanbanList = new ArrayList<>();
        for (ColumnKanbanDTO columnKanbanDTO : columnKanbanDTOList) {
            ColumnKanban columnKanban = convertDTOToColumn(columnKanbanDTO);
            columnKanbanList.add(columnKanban);
        }
        return columnKanbanList;
    }

    private List<RowKanban> convertDTOToRowList(List<RowKanbanDTO> rowKanbanDTOList) {
        List<RowKanban> rowKanbanList = new ArrayList<>();
        for (RowKanbanDTO rowKanbanDTO : rowKanbanDTOList) {
            RowKanban rowKanban = convertDTOToRow(rowKanbanDTO);
            rowKanbanList.add(rowKanban);
        }
        return rowKanbanList;
    }





}