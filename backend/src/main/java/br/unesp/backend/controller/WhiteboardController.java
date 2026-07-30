package br.unesp.backend.controller;

import br.unesp.backend.model.Pasta;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.model.Whiteboard;
import br.unesp.backend.model.WhiteboardPasta;
import br.unesp.backend.repository.PastaRepository;
import br.unesp.backend.repository.WhiteboardPastaRepository;
import br.unesp.backend.repository.WhiteboardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/whiteboard")
@CrossOrigin(origins = {"http://localhost:5173", "https://pinnotes-dw.app"})
public class WhiteboardController {

    private final WhiteboardRepository repository;
    private final PastaRepository pastaRepository;
    private final WhiteboardPastaRepository whiteboardPastaRepository;

    public WhiteboardController(WhiteboardRepository repository, PastaRepository pastaRepository, WhiteboardPastaRepository whiteboardPastaRepository) {
        this.repository = repository;
        this.pastaRepository = pastaRepository;
        this.whiteboardPastaRepository = whiteboardPastaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Whiteboard>> getAll(@AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(repository.findByUsuarioIdOrPublicaTrue(usuarioLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        return repository.findById(id).map(board -> {
            Map<String, Object> response = new HashMap<>();
            response.put("id", board.getId());
            response.put("title", board.getTitle());
            response.put("data", board.getData());
            
            // Envia a visibilidade para o frontend
            response.put("publica", board.isPublica());
            
    
            List<Long> pastaIds = whiteboardPastaRepository.findByWhiteboard(board).stream()
                    .filter(wp -> wp.getPasta() != null)
                    .map(wp -> wp.getPasta().getId())
                    .collect(Collectors.toList());
            
            response.put("pastaIds", pastaIds);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Map<String, Object> payload, // Changed to Object to handle arrays
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Whiteboard board = new Whiteboard();
        board.setUsuario(usuarioLogado);
        board.setTitle((String) payload.get("title"));
        board.setData((String) payload.get("data"));

        if (payload.containsKey("publica") && payload.get("publica") != null) {
            board.setPublica(Boolean.parseBoolean(payload.get("publica").toString()));
        } else {
            board.setPublica(false);
        }

        board = repository.save(board);

        // Process multiple folders (WhiteboardPasta)
        saveWhiteboardPastas(board, payload, usuarioLogado);

        Map<String, Object> response = new HashMap<>();
        response.put("id", board.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Whiteboard board = repository.findById(id).orElse(null);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }

        if (!board.getUsuario().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (payload.containsKey("title")) {
            board.setTitle((String) payload.get("title"));
        }
        if (payload.containsKey("data")) {
            board.setData((String) payload.get("data"));
        }
        if (payload.containsKey("publica") && payload.get("publica") != null) {
            board.setPublica(Boolean.parseBoolean(payload.get("publica").toString()));
        }

        board = repository.save(board);

        // Process multiple folders (WhiteboardPasta)
        saveWhiteboardPastas(board, payload, usuarioLogado);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", board.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return repository.findById(id).map(board -> {
            if (!board.getUsuario().getId().equals(usuarioLogado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }
            // Delete relationships first to avoid constraint violations
            whiteboardPastaRepository.deleteByWhiteboard(board);
            repository.delete(board);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Helper Method to manage multiple folder relationships
    private void saveWhiteboardPastas(Whiteboard board, Map<String, Object> payload, Usuario usuarioLogado) {
        // Clear existing folder links for this whiteboard on update
        whiteboardPastaRepository.deleteByWhiteboard(board);

        if (payload.containsKey("pastaIds") && payload.get("pastaIds") != null) {
            @SuppressWarnings("unchecked")
            List<Integer> pastaIds = (List<Integer>) payload.get("pastaIds");
            
            if (!pastaIds.isEmpty()) {
                // NOVO: Define a pasta principal na entidade Whiteboard para o frontend poder ler
                Pasta pastaPrincipal = pastaRepository.findById(pastaIds.get(0).longValue()).orElse(null);
                board.setPasta(pastaPrincipal);
                repository.save(board); // Atualiza o Whiteboard no banco com a pasta principal

                for (Integer pId : pastaIds) {
                    Pasta pasta = pastaRepository.findById(pId.longValue()).orElse(null);
                    if (pasta != null && pasta.getUsuario().getId().equals(usuarioLogado.getId())) {
                        WhiteboardPasta wp = new WhiteboardPasta(usuarioLogado, board, pasta);
                        whiteboardPastaRepository.save(wp);
                    }
                }
                return;
            }
        }
        
        // NOVO: Se vier vazio, garante que a pasta seja nula ("Geral")
        board.setPasta(null);
        repository.save(board);

        WhiteboardPasta wpRoot = new WhiteboardPasta(usuarioLogado, board, null);
        whiteboardPastaRepository.save(wpRoot);
    }
}