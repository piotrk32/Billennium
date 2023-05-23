package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubTaskDTO {
    private Long id;
    private String subtaskTitle;
    private String description;
    private SubTaskStatus status;
    private Long taskId;
}
