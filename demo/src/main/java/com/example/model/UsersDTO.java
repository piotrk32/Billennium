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
public class UsersDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Long> taskIds;
    private List<Long> kanbanIds;
    private Integer taskLimit;

}
