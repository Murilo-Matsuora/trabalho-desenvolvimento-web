package br.unesp.backend.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.backend.model.Whiteboard;
import br.unesp.backend.repository.WhiteboardRepository;

@RestController
@RequestMapping("/whiteboard")
public class WhiteboardController {

    @Autowired
    private WhiteboardRepository whiteboardRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Whiteboard> getById(@PathVariable(value = "id") Long id) {
        Optional<Whiteboard> wb = whiteboardRepository.findById(id);
        return wb.map(w -> new ResponseEntity<>(w, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<Whiteboard>> init() {
        Whiteboard w1 = new Whiteboard("Quadro 1", 1.0, 0.0, 0.0);
        Whiteboard w2 = new Whiteboard("Quadro 2", 1.0, 10.0, 20.0);
        ArrayList<Whiteboard> list = new ArrayList<>();
        list.add(w1);
        list.add(w2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
