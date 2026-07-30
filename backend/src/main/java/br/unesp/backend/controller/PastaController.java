package br.unesp.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.backend.model.Pasta;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.model.Whiteboard;
import br.unesp.backend.repository.PastaRepository;


@RestController
@RequestMapping("/pasta")
@CrossOrigin
public class PastaController {

    private final PastaRepository pastaRepository;

    public PastaController(PastaRepository pastaRepository) {
        this.pastaRepository = pastaRepository;
    }

    // Retorna as pastas do usuario autenticado e pastas publicas, com opção de filtrar
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Pasta>> listarPastas(
            @RequestParam(required = false, defaultValue = "false") boolean userPasta,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Pasta> pastas;
        
        // Se o frontend solicitar apenas as pastas do dono (para o dropdown)
        if (userPasta) {
            pastas = pastaRepository.findByUsuarioId(usuarioLogado.getId());
        } else {
            // Mantém o comportamento original para outras partes do sistema
            pastas = pastaRepository.findByUsuarioIdOrPublicaTrue(usuarioLogado.getId());
        }

        if (pastas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pastas);
    }

    // Busca uma pasta especifica verificando as permissoes de acesso
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Pasta> buscarPorId(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return pastaRepository.findById(id)
                .map(pasta -> {
                    // Permite acesso se for publica ou pertencer ao usuario logado
                    if (pasta.isPublica() || (usuarioLogado != null && pasta.getUsuario().getId().equals(usuarioLogado.getId()))) {
                        return ResponseEntity.ok(pasta);
                    }
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).<Pasta>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cria uma nova pasta ou subpasta associada ao usuario logado
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Pasta> criarPasta(@RequestBody Pasta pasta, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        pasta.setUsuario(usuarioLogado);
        Pasta novaPasta = pastaRepository.save(pasta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPasta);
    }

    // Atualiza dados da pasta (nome, visibilidade publica/privada, ou pasta pai)
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Pasta> atualizarPasta(
            @PathVariable("id") Long id,
            @RequestBody Pasta dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return pastaRepository.findById(id)
                .map(pastaExistente -> {
                    if (!pastaExistente.getUsuario().getId().equals(usuarioLogado.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Pasta>build();
                    }

                    if (dados.getNome() != null && !dados.getNome().isBlank()) {
                        pastaExistente.setNome(dados.getNome());
                    }
                    pastaExistente.setPublica(dados.isPublica());
                    
                    // Atualiza a referencia para subpasta
                    if (dados.getPastaPai() != null) {
                        pastaExistente.setPastaPai(dados.getPastaPai());
                    }

                    Pasta pastaAtualizada = pastaRepository.save(pastaExistente);
                    return ResponseEntity.ok(pastaAtualizada);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deleta uma pasta pelo ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarPasta(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return pastaRepository.findById(id)
                .map(pasta -> {
                    if (!pasta.getUsuario().getId().equals(usuarioLogado.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
                    }
                    pastaRepository.delete(pasta);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}