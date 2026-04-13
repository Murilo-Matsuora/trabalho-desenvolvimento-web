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

import br.unesp.backend.model.Conexao;
import br.unesp.backend.repository.ConexaoRepository;

@RestController
@RequestMapping("/conexao")
public class ConexaoController {

    @Autowired
    private ConexaoRepository conexaoRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Conexao> getById(@PathVariable(value = "id") Long id) {
        Optional<Conexao> c = conexaoRepository.findById(id);
        return c.map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<Conexao>> init() {
        Conexao c1 = new Conexao("seta", "solido", "fino", null, null);
        Conexao c2 = new Conexao("linha", "tracejado", "grosso", null, null);
        ArrayList<Conexao> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
