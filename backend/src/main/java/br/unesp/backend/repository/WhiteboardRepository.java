package br.unesp.backend.repository;

import org.springframework.data.repository.CrudRepository;

import br.unesp.backend.model.Whiteboard;

public interface WhiteboardRepository extends CrudRepository<Whiteboard, Long> {

}
