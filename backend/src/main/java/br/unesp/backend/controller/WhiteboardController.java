package br.unesp.backend.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.backend.model.Whiteboard;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.repository.WhiteboardRepository;

@RestController
@RequestMapping("/whiteboard")
public class WhiteboardController {

    @Autowired
    private WhiteboardRepository whiteboardRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Whiteboard> getById(@PathVariable(value = "id") Long id,
            @AuthenticationPrincipal Usuario usuario) {
        Optional<Whiteboard> wb = whiteboardRepository.findById(id);
        return wb.filter(w -> w.getUsuario() != null && w.getUsuario().getId().equals(usuario.getId()))
                .map(w -> new ResponseEntity<>(w, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Whiteboard> cadastrar(@RequestBody Whiteboard whiteboard,
            @AuthenticationPrincipal Usuario usuario) {
        whiteboard.setId(null);
        whiteboard.setUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(whiteboardRepository.save(whiteboard));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<Whiteboard>> init(@AuthenticationPrincipal Usuario usuario) {
        Whiteboard w1 = new Whiteboard("Quadro 1", 1.0, 0.0, 0.0);
        Whiteboard w2 = new Whiteboard("Quadro 2", 1.0, 10.0, 20.0);
        ArrayList<Whiteboard> list = new ArrayList<>();
        list.add(w1);
        list.add(w2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
