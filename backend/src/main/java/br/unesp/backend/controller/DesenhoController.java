package br.unesp.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.backend.model.Desenho;
import br.unesp.backend.model.Ponto;
import br.unesp.backend.repository.DesenhoRepository;

@RestController
@RequestMapping("/desenho")
public class DesenhoController {

    @Autowired
    private DesenhoRepository desenhoRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Desenho> getById(@PathVariable(value = "id") Long id) {
        Optional<Desenho> d = desenhoRepository.findById(id);
        return d.map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<Desenho>> init() {
        List<Ponto> pts1 = new ArrayList<>();
        pts1.add(new Ponto(0.0, 0.0));
        pts1.add(new Ponto(10.0, 10.0));
        Desenho d1 = new Desenho(pts1, "#000000", 2.0);

        List<Ponto> pts2 = new ArrayList<>();
        pts2.add(new Ponto(5.0, 5.0));
        Desenho d2 = new Desenho(pts2, "#ff0000", 1.5);

        ArrayList<Desenho> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
