package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlueprintJpaRepository extends JpaRepository<Blueprint, Long> {
    Optional<Blueprint> findByAuthorAndName(String author, String name);
    List<Blueprint> findByAuthor(String author);
}
