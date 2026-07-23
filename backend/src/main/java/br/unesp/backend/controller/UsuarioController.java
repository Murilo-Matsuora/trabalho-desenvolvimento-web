package br.unesp.backend.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/me", produces = "application/json")
    public ResponseEntity<Usuario> me(@AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usuarioLogado);
    }

    //PUT /usuario/me
    // Atualiza o perfil do usuário logado mantendo a integridade dos dados e da senha.
     
    @PutMapping(value = "/me", produces = "application/json")
    public ResponseEntity<Usuario> atualizarPerfil(
            @RequestBody Usuario dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Busca o usuário gerenciado diretamente do banco para evitar conflitos de JPA
        Usuario usuarioBD = usuarioRepository.findById(usuarioLogado.getId()).orElse(null);
        if (usuarioBD == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza campos de texto apenas se forem informados
        if (dados.getNome() != null && !dados.getNome().isBlank()) {
            usuarioBD.setNome(dados.getNome());
        }
        if (dados.getUsername() != null && !dados.getUsername().isBlank()) {
            usuarioBD.setUsername(dados.getUsername());
        }
        if (dados.getEmail() != null && !dados.getEmail().isBlank()) {
            usuarioBD.setEmail(dados.getEmail());
        }

        // Criptografa e altera a senha apenas se uma nova for fornecida
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            usuarioBD.setSenha(passwordEncoder.encode(dados.getSenha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioBD);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping(value = "/me")
    public ResponseEntity<Void> deletarMinhaConta(@AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!usuarioRepository.existsById(usuarioLogado.getId())) {
            return ResponseEntity.notFound().build();
        }

        usuarioRepository.deleteById(usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

 
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = StreamSupport
                .stream(usuarioRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}