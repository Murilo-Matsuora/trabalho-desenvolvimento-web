package br.unesp.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.unesp.backend.model.Imagem;
import br.unesp.backend.model.Pasta;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.repository.ImagemRepository;
import br.unesp.backend.repository.PastaRepository;

@RestController
@RequestMapping("/imagem")
@CrossOrigin(origins = {"http://localhost:5173", "https://pinnotes-dw.app"})
public class ImagemController {

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private PastaRepository pastaRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Imagem> getById(@PathVariable(value = "id") Long id) {
        return imagemRepository.findById(id)
                .map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Imagem>> listarImagens(@AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Retorna imagens públicas ou criadas pelo próprio usuário
        List<Imagem> list = imagemRepository.findAll();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Imagem> salvarImagem(@RequestBody Imagem imagem, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Define o autor real da criação
        imagem.setAutor(usuarioLogado);
        if (imagem.getSalvos() == null) {
            imagem.setSalvos(1);
        }

        // Validação de Pasta e Visibilidade
        if (imagem.getPasta() != null && imagem.getPasta().getId() != null) {
            Optional<Pasta> pastaOpt = pastaRepository.findById(imagem.getPasta().getId());
            if (pastaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Pasta pastaDestino = pastaOpt.get();
            
            // Garante que a pasta pertence ao usuário logado
            if (!pastaDestino.getUsuario().getId().equals(usuarioLogado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // REGRA: Não permitir pinnup privado em pasta pública
            if (!imagem.isPublica() && pastaDestino.isPublica()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            imagem.setPasta(pastaDestino);
        }

        Imagem imagemSalva = imagemRepository.save(imagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(imagemSalva);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Imagem> atualizarImagem(
            @PathVariable("id") Long id,
            @RequestBody Imagem dadosAtualizados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return imagemRepository.findById(id).map(imagemExistente -> {
            if (!imagemExistente.getAutor().getId().equals(usuarioLogado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Imagem>build();
            }
            if (dadosAtualizados.getTitulo() != null) imagemExistente.setTitulo(dadosAtualizados.getTitulo());
            if (dadosAtualizados.getDescricao() != null) imagemExistente.setDescricao(dadosAtualizados.getDescricao());
            if (dadosAtualizados.getUrl() != null) imagemExistente.setUrl(dadosAtualizados.getUrl());
            if (dadosAtualizados.getTags() != null) imagemExistente.setTags(dadosAtualizados.getTags());
            
            boolean ehPublica = dadosAtualizados.isPublica();
            imagemExistente.setPublica(ehPublica); 
            
            if (dadosAtualizados.getPasta() != null && dadosAtualizados.getPasta().getId() != null) {
                Optional<Pasta> pastaOpt = pastaRepository.findById(dadosAtualizados.getPasta().getId());
                if(pastaOpt.isPresent()) {
                    Pasta pastaDestino = pastaOpt.get();
                    
                    // Validação de segurança na edição
                    if (!ehPublica && pastaDestino.isPublica()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).<Imagem>build();
                    }
                    imagemExistente.setPasta(pastaDestino);
                }
            } else if (dadosAtualizados.getPasta() == null) {
                imagemExistente.setPasta(null);
            }

            return ResponseEntity.ok(imagemRepository.save(imagemExistente));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarImagem(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return imagemRepository.findById(id).map(imagem -> {
            if (!imagem.getAutor().getId().equals(usuarioLogado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }
            imagemRepository.delete(imagem);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}