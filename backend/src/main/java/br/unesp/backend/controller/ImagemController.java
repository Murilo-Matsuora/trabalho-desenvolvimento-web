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
import br.unesp.backend.model.Usuario;
import br.unesp.backend.model.UserRole;
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
        // Create a mock user to assign as the author
        Usuario mockUser = new Usuario("Pinnotes", "pinnotes", "pinnotes@gmail.com", "senha123", UserRole.USER);
        
        // Updated mock data to include the 'autor' as the 7th parameter
        Imagem i1 = new Imagem(0.0, 0.0, 200.0, 200.0, "https://i.pinimg.com/736x/db/0b/89/db0b89cd34a97c70fcb6e3b1d54d2be3.jpg", "chiikawa", mockUser);
        Imagem i2 = new Imagem(50.0, 30.0, 80.0, 80.0, "https://i.pinimg.com/736x/e9/a7/31/e9a7319b80005992aa8221bd229e969b.jpg", "my melody", mockUser);
        
        ArrayList<Imagem> list = new ArrayList<>();
        list.add(i1);
        list.add(i2);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

