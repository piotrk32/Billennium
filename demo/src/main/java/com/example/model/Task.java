package com.example.model;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "task")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    private Long id;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    private String color;

    @Column(name = "status")
    private String status;


//KOLUMNA
    @JsonIgnore
    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private ColumnKanban columnKanban;

//ROW
    @JsonIgnore
    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "row_id")
    private RowKanban rowKanban;

//USER
//    @JsonIgnore
    @JsonBackReference
    @ToString.Exclude
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Users> users;

//SUBTASK
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTask> subTasks = new ArrayList<>();

    //TASK Progress
    @Transient
    private Integer progress;


    public int taskProgress(Task task) {
        List<SubTask> subtasks = task.getSubTasks();

        if (subtasks.isEmpty()) {
            return 0; // Brak postępu
        }

        int completedSubtasks = 0;
        for (SubTask subtask : subtasks) {
            if (subtask.getStatus() == SubTaskStatus.DONE) {
                completedSubtasks++;
            }
        }

        if (completedSubtasks == subtasks.size()) {
            return 100; // Ukończony postęp
        }

        progress = calculateProgress(completedSubtasks, subtasks.size());
        return progress;
    }

    private int calculateProgress(int completedSubtasks, int totalSubtasks) {
        if (totalSubtasks == 0) {
            return 0; // Brak subtasków, postęp wynosi 0%
        }

        return (completedSubtasks * 100) / totalSubtasks;
    }


}

