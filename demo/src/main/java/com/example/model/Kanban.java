package com.example.model;

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
@Table(name = "kanban")
public class Kanban {

    public Kanban(int id) {
        this.id = (long) id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id", nullable = false)
    private Long id;

    @Column(name = "kanban_title", nullable = false, unique = true)
    private String kanbanTitle;

    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "kanban", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ColumnKanban> columnsKanban;


    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "kanbanId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RowKanban> rowsKanban;




     //Dodanie listy użytkowników powiązanych z kanbanem
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_kanban",
            joinColumns = @JoinColumn(name = "kanban_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private List<Users> users;


    public void addColumn(ColumnKanban columnKanban) {
        if (Objects.isNull(columnsKanban)) {
            columnsKanban = new ArrayList<>();
        }
        columnsKanban.add(columnKanban);
        columnKanban.setKanban(this);
    }

    //addUser do dodawania użytkowników do kanbanu
    public void addUser(Users users) {
        if (Objects.isNull(this.users)) {
            this.users = new ArrayList<>();
        }
        this.users.add(users);
    }


}

