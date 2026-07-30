package br.unesp.backend.repository;

import br.unesp.backend.model.Whiteboard;
import br.unesp.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WhiteboardRepository extends JpaRepository<Whiteboard, Long> {
    List<Whiteboard> findByUsuario(Usuario usuario);
    List<Whiteboard> findByUsuarioIdOrPublicaTrue(Long usuarioId);
}