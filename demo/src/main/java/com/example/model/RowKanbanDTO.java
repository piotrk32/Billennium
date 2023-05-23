package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowKanbanDTO {

    private Long rowId;
    private String rowTitle;
    private List<TaskDTO> taskList;
    private Integer taskLimit;
    private Kanban kanbanId;

}
