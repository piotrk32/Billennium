package com.example.repository;

import com.example.model.Kanban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface KanbanRepository extends JpaRepository<Kanban, Long> {
    Optional<Kanban> findByTitle(String title);
}
