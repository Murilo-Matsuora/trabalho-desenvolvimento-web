package br.unesp.backend.repository;

import org.springframework.data.repository.CrudRepository;

import br.unesp.backend.model.Usuario;


public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
    
}