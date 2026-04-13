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

import br.unesp.backend.model.StickyNote;
import br.unesp.backend.repository.StickyNoteRepository;

@RestController
@RequestMapping("/stickynote")
public class StickyNoteController {

    @Autowired
    private StickyNoteRepository stickyNoteRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<StickyNote> getById(@PathVariable(value = "id") Long id) {
        Optional<StickyNote> sn = stickyNoteRepository.findById(id);
        return sn.map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<StickyNote>> init() {
        StickyNote n1 = new StickyNote(10.0, 10.0, 100.0, 100.0, "Lembrete 1", "#ffeb3b");
        StickyNote n2 = new StickyNote(150.0, 20.0, 120.0, 90.0, "Lembrete 2", "#a5d6a7");
        ArrayList<StickyNote> list = new ArrayList<>();
        list.add(n1);
        list.add(n2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
