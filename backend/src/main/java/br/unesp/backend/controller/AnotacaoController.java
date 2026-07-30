package br.unesp.backend.controller;

import br.unesp.backend.model.Anotacao;
import br.unesp.backend.model.AnotacaoPasta;
import br.unesp.backend.model.Pasta;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.repository.AnotacaoPastaRepository;
import br.unesp.backend.repository.AnotacaoRepository;
import br.unesp.backend.repository.PastaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/anotacao")
@CrossOrigin(origins = {"http://localhost:5173", "https://pinnotes-dw.app"})
public class AnotacaoController {

    private final AnotacaoRepository repository;
    private final PastaRepository pastaRepository;
    private final AnotacaoPastaRepository anotacaoPastaRepository;

    public AnotacaoController(AnotacaoRepository repository, PastaRepository pastaRepository, AnotacaoPastaRepository anotacaoPastaRepository) {
        this.repository = repository;
        this.pastaRepository = pastaRepository;
        this.anotacaoPastaRepository = anotacaoPastaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Anotacao>> getAll(@AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(repository.findByUsuarioIdOrPublicaTrue(usuarioLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        return repository.findById(id).map(nota -> {
            
            // NOVO: Verifica se o usuário é dono e barra o acesso se for privado
            boolean isOwner = usuarioLogado != null && nota.getUsuario().getId().equals(usuarioLogado.getId());
            if (!isOwner && !nota.isPublica()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Map<String, Object>>build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", nota.getId());
            response.put("title", nota.getTitle());
            response.put("conteudo", nota.getConteudo());
            response.put("publica", nota.isPublica());
            response.put("isOwner", isOwner); // NOVO: Flag para o frontend bloquear edição/ferramentas
            
            List<Long> pastaIds = anotacaoPastaRepository.findByAnotacao(nota).stream()
                    .filter(ap -> ap.getPasta() != null)
                    .map(ap -> ap.getPasta().getId())
                    .collect(Collectors.toList());
            
            response.put("pastaIds", pastaIds);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> payload, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Anotacao nota = new Anotacao();
        nota.setUsuario(usuarioLogado);
        nota.setTitle((String) payload.get("title"));
        nota.setConteudo((String) payload.get("conteudo"));
        nota.setPublica(payload.containsKey("publica") && Boolean.parseBoolean(payload.get("publica").toString()));

        nota = repository.save(nota);
        saveAnotacaoPastas(nota, payload, usuarioLogado);

        Map<String, Object> response = new HashMap<>();
        response.put("id", nota.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> payload, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Anotacao nota = repository.findById(id).orElse(null);
        if (nota == null) return ResponseEntity.notFound().build();
        if (!nota.getUsuario().getId().equals(usuarioLogado.getId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (payload.containsKey("title")) nota.setTitle((String) payload.get("title"));
        if (payload.containsKey("conteudo")) nota.setConteudo((String) payload.get("conteudo"));
        if (payload.containsKey("publica")) nota.setPublica(Boolean.parseBoolean(payload.get("publica").toString()));

        nota = repository.save(nota);
        saveAnotacaoPastas(nota, payload, usuarioLogado);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", nota.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return repository.findById(id).map(nota -> {
            if (!nota.getUsuario().getId().equals(usuarioLogado.getId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            anotacaoPastaRepository.deleteByAnotacao(nota);
            repository.delete(nota);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void saveAnotacaoPastas(Anotacao nota, Map<String, Object> payload, Usuario usuarioLogado) {
        anotacaoPastaRepository.deleteByAnotacao(nota);

        if (payload.containsKey("pastaIds") && payload.get("pastaIds") != null) {
            @SuppressWarnings("unchecked")
            List<Integer> pastaIds = (List<Integer>) payload.get("pastaIds");
            
            if (!pastaIds.isEmpty()) {
                Pasta pastaPrincipal = pastaRepository.findById(pastaIds.get(0).longValue()).orElse(null);
                nota.setPasta(pastaPrincipal);
                repository.save(nota);

                for (Integer pId : pastaIds) {
                    Pasta pasta = pastaRepository.findById(pId.longValue()).orElse(null);
                    if (pasta != null && pasta.getUsuario().getId().equals(usuarioLogado.getId())) {
                        AnotacaoPasta ap = new AnotacaoPasta(usuarioLogado, nota, pasta);
                        anotacaoPastaRepository.save(ap);
                    }
                }
                return;
            }
        }
        
        nota.setPasta(null);
        repository.save(nota);
        AnotacaoPasta apRoot = new AnotacaoPasta(usuarioLogado, nota, null);
        anotacaoPastaRepository.save(apRoot);
    }
}