package br.unesp.backend.repository;

import org.springframework.data.repository.CrudRepository;

import br.unesp.backend.model.Texto;

public interface TextoRepository extends CrudRepository<Texto, Long> {

}
