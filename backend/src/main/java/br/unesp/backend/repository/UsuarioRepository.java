package br.unesp.backend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.unesp.backend.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    @Query("select u from Usuario u where u.email = ?1")
	Usuario findByLogin(String email);
}