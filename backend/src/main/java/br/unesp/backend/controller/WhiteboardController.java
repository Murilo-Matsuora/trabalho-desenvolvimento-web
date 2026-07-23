package br.unesp.backend.controller;

import br.unesp.backend.model.Whiteboard;
import br.unesp.backend.repository.WhiteboardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/whiteboards")
@CrossOrigin(origins = "*")
public class WhiteboardController {

    private final WhiteboardRepository repository;

    public WhiteboardController(WhiteboardRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Whiteboard> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Whiteboard> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Whiteboard create(@RequestBody Whiteboard whiteboard) {
        if (whiteboard.getTitle() == null || whiteboard.getTitle().isBlank()) {
            whiteboard.setTitle("Novo Whiteboard");
        }
        if (whiteboard.getData() == null) {
            whiteboard.setData("{\"elements\":[],\"arrows\":[],\"drawings\":[]}");
        }
        return repository.save(whiteboard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Whiteboard> update(@PathVariable Long id, @RequestBody Whiteboard updated) {
        return repository.findById(id)
                .map(board -> {
                    if (updated.getTitle() != null) board.setTitle(updated.getTitle());
                    if (updated.getData() != null) board.setData(updated.getData());
                    return ResponseEntity.ok(repository.save(board));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}