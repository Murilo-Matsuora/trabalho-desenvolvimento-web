package br.unesp.backend.repository;

import br.unesp.backend.model.Anotacao;
import br.unesp.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnotacaoRepository extends JpaRepository<Anotacao, Long> {
    List<Anotacao> findByUsuario(Usuario usuario);
}