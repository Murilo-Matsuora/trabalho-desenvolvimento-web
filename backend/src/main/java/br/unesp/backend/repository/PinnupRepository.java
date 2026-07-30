package br.unesp.backend.repository;

import br.unesp.backend.model.Pinnup;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.model.Pasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PinnupRepository extends JpaRepository<Pinnup, Long> {
    List<Pinnup> findByUsuario(Usuario usuario);
    Optional<Pinnup> findByUsuarioAndImagemAndPasta(Usuario usuario, br.unesp.backend.model.Imagem imagem, Pasta pasta);
    void deleteByUsuarioAndImagem(Usuario usuario, br.unesp.backend.model.Imagem imagem);
}