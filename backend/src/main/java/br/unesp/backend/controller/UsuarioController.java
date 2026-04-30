package br.unesp.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.backend.model.Usuario;
import br.unesp.backend.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // GET /usuario/{id}
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> acharUsuario(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return usuario
                .map(u -> ResponseEntity.ok(u))
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /usuario/all
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Usuario>> acharTodos() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    // POST /usuario/
    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        usuario.setId(null);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    // PUT /usuario/{id}
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        if (id == null || id <= 0 || usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        usuario.setId(id);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    // DELETE /usuario/{id}
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}