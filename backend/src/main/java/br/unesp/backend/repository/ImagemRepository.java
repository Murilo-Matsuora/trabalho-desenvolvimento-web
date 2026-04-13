package br.unesp.backend.repository;

import org.springframework.data.repository.CrudRepository;

import br.unesp.backend.model.Imagem;

public interface ImagemRepository extends CrudRepository<Imagem, Long> {

}
