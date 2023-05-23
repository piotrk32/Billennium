package com.example.repository;

import com.example.model.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    Optional<SubTask> findBysubTaskTitle(String subTaskTitle);
}
