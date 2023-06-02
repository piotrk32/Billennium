package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnKanbanDTO {
    private Long id;
    private Long kanbanId;
    private String columnTitle;
    private String columnColor;
//    @JsonIgnore
//    private List<TaskDTO> taskDTOList;
//
//    public List<TaskDTO> getTaskList() {
//        return taskDTOList;
//    }

}

