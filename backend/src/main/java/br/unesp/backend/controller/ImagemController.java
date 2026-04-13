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

import br.unesp.backend.model.Imagem;
import br.unesp.backend.repository.ImagemRepository;

@RestController
@RequestMapping("/imagem")
public class ImagemController {

    @Autowired
    private ImagemRepository imagemRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Imagem> getById(@PathVariable(value = "id") Long id) {
        Optional<Imagem> img = imagemRepository.findById(id);
        return img.map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ArrayList<Imagem>> init() {
        Imagem i1 = new Imagem(0.0, 0.0, 120.0, 200.0, "https://exemplo.com/img1.png");
        Imagem i2 = new Imagem(50.0, 30.0, 80.0, 80.0, "https://exemplo.com/img2.png");
        ArrayList<Imagem> list = new ArrayList<>();
        list.add(i1);
        list.add(i2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
