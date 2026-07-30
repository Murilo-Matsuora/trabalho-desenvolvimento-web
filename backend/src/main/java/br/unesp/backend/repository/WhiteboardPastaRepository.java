package br.unesp.backend.repository;

import br.unesp.backend.model.Pasta;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.model.Whiteboard;
import br.unesp.backend.model.WhiteboardPasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WhiteboardPastaRepository extends JpaRepository<WhiteboardPasta, Long> {
    
    List<WhiteboardPasta> findByUsuario(Usuario usuario);
    
    List<WhiteboardPasta> findByWhiteboard(Whiteboard whiteboard);
    
    Optional<WhiteboardPasta> findByUsuarioAndWhiteboardAndPasta(Usuario usuario, Whiteboard whiteboard, Pasta pasta);
    
    @Transactional
    void deleteByWhiteboard(Whiteboard whiteboard);
}