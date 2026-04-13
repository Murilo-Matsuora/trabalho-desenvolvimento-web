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

import br.unesp.backend.model.Texto;
import br.unesp.backend.repository.TextoRepository;

@RestController
@RequestMapping("/texto")
public class TextoController {

    @Autowired
    private TextoRepository textoRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Texto> getById(@PathVariable(value = "id") Long id) {
        Optional<Texto> t = textoRepository.findById(id);
        return t.map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<Texto>> init() {
        Texto t1 = new Texto(0.0, 0.0, 200.0, 40.0, "Título do quadro", 16, true, false);
        Texto t2 = new Texto(0.0, 50.0, 300.0, 60.0, "Descrição em itálico", 12, false, true);
        ArrayList<Texto> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
