package br.unesp.backend.repository;

import org.springframework.data.repository.CrudRepository;

import br.unesp.backend.model.StickyNote;

public interface StickyNoteRepository extends CrudRepository<StickyNote, Long> {

}
