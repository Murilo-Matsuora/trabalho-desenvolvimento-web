package br.unesp.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> acharUsuario(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(usuario);
    }

    @PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> atualizar(
            @RequestBody Usuario dados,
            @AuthenticationPrincipal Usuario usuario) {
        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }
        usuario.setEmail(dados.getEmail());
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> deletar(@AuthenticationPrincipal Usuario usuario) {
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}
