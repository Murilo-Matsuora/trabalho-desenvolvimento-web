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

import br.unesp.backend.model.Secao;
import br.unesp.backend.repository.SecaoRepository;

@RestController
@RequestMapping("/secao")
public class SecaoController {

    @Autowired
    private SecaoRepository secaoRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Secao> getById(@PathVariable(value = "id") Long id) {
        Optional<Secao> s = secaoRepository.findById(id);
        return s.map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<Secao>> init() {
        Secao s1 = new Secao(0.0, 0.0, 400.0, 300.0, "Seção A");
        Secao s2 = new Secao(100.0, 50.0, 200.0, 150.0, "Seção B");
        ArrayList<Secao> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
