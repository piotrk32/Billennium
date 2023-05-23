package com.example.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KanbanDTO {
    private Long id;
    private String kanbanTitle;
    private List<ColumnKanbanDTO> columnsKanban;
    private List<UsersDTO> users;
    private List<RowKanbanDTO> rowsKanban;
}
