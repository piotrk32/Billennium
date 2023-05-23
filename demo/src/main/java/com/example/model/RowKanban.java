package com.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "row_kanban")
public class RowKanban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Long id;

    @Column(name = "row_title")
    private String rowTitle;

    @JsonIgnore
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanbanId;

    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "rowKanban", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> tasks;

    @Column(name = "limit_task")
    private Integer taskLimit;


    public void addTaskToRow(Task task) {

        if (Objects.isNull(tasks)) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
        task.setRowKanban(this);
    }
    public List<Task> getTaskList() {
        return tasks;
    }










}
