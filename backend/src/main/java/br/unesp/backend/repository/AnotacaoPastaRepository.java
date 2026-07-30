package br.unesp.backend.repository;

import br.unesp.backend.model.Anotacao;
import br.unesp.backend.model.AnotacaoPasta;
import br.unesp.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface AnotacaoPastaRepository extends JpaRepository<AnotacaoPasta, Long> {
    List<AnotacaoPasta> findByAnotacao(Anotacao anotacao);
    
    @Transactional
    void deleteByAnotacao(Anotacao anotacao);
}