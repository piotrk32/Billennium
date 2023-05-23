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
public class TaskDTO {

    private Long kanban_column_id;
    private Long kanban_row_id;
    private String title;
    private String description;
    private String color;
    private List <Long> userIds;
    private List<SubTask> subTasks;
    private Integer limitTask;


}
