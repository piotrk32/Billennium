package com.example.repository;

import java.util.List;
import java.util.Optional;

import com.example.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
      select t from Token t join Users u
      on t.user_id = u.id
          where u.id = :id and (t.expired = false or t.revoked = false)
      """, nativeQuery = true)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
