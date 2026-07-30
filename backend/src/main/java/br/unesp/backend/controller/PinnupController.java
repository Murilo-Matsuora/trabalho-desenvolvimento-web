package br.unesp.backend.controller;

import br.unesp.backend.model.Imagem;
import br.unesp.backend.model.Pasta;
import br.unesp.backend.model.Pinnup;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.repository.ImagemRepository;
import br.unesp.backend.repository.PastaRepository;
import br.unesp.backend.repository.PinnupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pinnup")
@CrossOrigin(origins = {"http://localhost:5173", "https://pinnotes-dw.app"})
public class PinnupController {

    @Autowired
    private PinnupRepository pinnupRepository; // Note: Changed to lowercase 'p'

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private PastaRepository pastaRepository;

    @GetMapping
    public ResponseEntity<List<Pinnup>> listarSalvos(@AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(pinnupRepository.findByUsuario(usuarioLogado));
    }

    @PostMapping
    public ResponseEntity<?> salvarPinnup(@RequestBody Map<String, Long> payload, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long imagemId = payload.get("imagemId");
        Long pastaId = payload.get("pastaId"); // Pode ser null se salvar na raiz

        Imagem imagem = imagemRepository.findById(imagemId).orElse(null);
        if (imagem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagem não encontrada.");
        }

        Pasta pasta = null;
        if (pastaId != null) {
            pasta = pastaRepository.findById(pastaId).orElse(null);
            if (pasta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pasta não encontrada.");
            }

            // ADD THIS SECURITY CHECK: Ensure the folder belongs to the logged-in user
            if (!pasta.getUsuario().getId().equals(usuarioLogado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não é permitido salvar em uma pasta de outro usuário.");
            }

            if (!imagem.isPublica() && pasta.isPublica()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Não é permitido salvar um Pinnup privado em uma pasta pública.");
            }
        }

        // Verifica se já existe esse registro exato (mesmo usuário, mesma imagem, mesma pasta)
        boolean jaSalvo = pinnupRepository.findByUsuarioAndImagemAndPasta(usuarioLogado, imagem, pasta).isPresent();
        if (jaSalvo) {
            return ResponseEntity.badRequest().body("Este Pinnup já está salvo nesta pasta.");
        }

        Pinnup novoSalvo = new Pinnup(usuarioLogado, imagem, pasta);
        Pinnup salvoSalvo = pinnupRepository.save(novoSalvo);

        // Incrementa o contador de salvos na imagem original se desejado
        if (imagem.getSalvos() == null) {
            imagem.setSalvos(1);
        } else {
            imagem.setSalvos(imagem.getSalvos() + 1);
        }
        imagemRepository.save(imagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(salvoSalvo);
    }


    @PostMapping(value = "/{id}/salvar", produces = "application/json")
    public ResponseEntity<?> salvarPinnupPorId(
            @PathVariable("id") Long imagemId,
            @RequestBody(required = false) Map<String, Long> payload, // Assuming the frontend sends { "folderId": 123 }
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

   
        Optional<Imagem> imagemOpt = imagemRepository.findById(imagemId);
        if (imagemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagem não encontrada");
        }
        Imagem imagem = imagemOpt.get();

    
        Pasta pastaDestino = null;
        if (payload != null && payload.containsKey("folderId") && payload.get("folderId") != null) {
            Long folderId = payload.get("folderId");
            Optional<Pasta> pastaOpt = pastaRepository.findById(folderId);
            
            if (pastaOpt.isPresent()) {
                pastaDestino = pastaOpt.get();
                
                if (!pastaDestino.getUsuario().getId().equals(usuarioLogado.getId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Pasta não pertence ao usuário");
                }

          
                if (!imagem.isPublica() && pastaDestino.isPublica()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pinnup privado não vai para pasta pública");
                }
            }
        }

        // Verifica se já existe esse registro exato (mesmo usuário, mesma imagem, mesma pasta)
        boolean jaSalvo = pinnupRepository.findByUsuarioAndImagemAndPasta(usuarioLogado, imagem, pastaDestino).isPresent();
        if (jaSalvo) {
            return ResponseEntity.badRequest().body("Este Pinnup já está salvo nesta pasta.");
        }

        Pinnup novoPinnup = new Pinnup(usuarioLogado, imagem, pastaDestino);
        Pinnup salvoSalvo = pinnupRepository.save(novoPinnup);

        // Incrementa o contador de salvos na imagem original
        if (imagem.getSalvos() == null) {
            imagem.setSalvos(1);
        } else {
            imagem.setSalvos(imagem.getSalvos() + 1);
        }
        imagemRepository.save(imagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(salvoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerSalvo(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return pinnupRepository.findById(id).map(salvo -> {
            if (!salvo.getUsuario().getId().equals(usuarioLogado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }
            
            Imagem img = salvo.getImagem();
            if (img.getSalvos() != null && img.getSalvos() > 0) {
                img.setSalvos(img.getSalvos() - 1);
                imagemRepository.save(img);
            }

            pinnupRepository.delete(salvo);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}