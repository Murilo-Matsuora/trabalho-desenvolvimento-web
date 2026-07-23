package br.unesp.backend.repository;

import br.unesp.backend.model.Whiteboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteboardRepository extends JpaRepository<Whiteboard, Long> {
}