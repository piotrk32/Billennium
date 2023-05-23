package com.example.repository;

import com.example.model.RowKanban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RowKanbanRepository extends JpaRepository<RowKanban, Long> {
    Optional<RowKanban> findById(Long id);

}
