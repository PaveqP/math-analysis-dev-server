package it.macgood.jmathanalysisdevserver.demidovich;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAll();
    Optional<Task> findById(Long id);
    Page<Task> findByIdBetween(Long startId, Long endId, Pageable pageable);
}
