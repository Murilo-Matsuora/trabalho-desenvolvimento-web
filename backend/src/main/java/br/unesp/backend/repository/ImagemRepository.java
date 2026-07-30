package br.unesp.backend.repository;

import br.unesp.backend.model.Imagem;
import br.unesp.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {
    List<Imagem> findByPublicaTrueOrAutor(Usuario autor);
}