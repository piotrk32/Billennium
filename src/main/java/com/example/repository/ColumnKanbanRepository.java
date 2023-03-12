package com.example.repository;

import com.example.model.ColumnKanban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColumnKanbanRepository extends JpaRepository<ColumnKanban, Long> {
    Optional<ColumnKanban> findByTitle(String title);
}
