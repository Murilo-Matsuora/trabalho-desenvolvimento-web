package br.unesp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.unesp.backend.model.Pasta;

// Interface do repositorio Spring Data JPA para operacoes do recurso Pasta
@Repository
public interface PastaRepository extends JpaRepository<Pasta, Long> {

    List<Pasta> findByUsuarioIdOrPublicaTrue(Long usuarioId);

    // Retorna todas as pastas de um usuário específico
    List<Pasta> findByUsuarioId(Long usuarioId);

    // Retorna apenas subpastas diretas de uma pasta principal
    List<Pasta> findByPastaPaiId(Long pastaPaiId);

    List<Pasta> findByUsuarioIdAndPastaPaiIsNull(Long usuarioId);
}