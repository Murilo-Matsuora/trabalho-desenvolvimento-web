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

import br.unesp.backend.model.Usuario;
import br.unesp.backend.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(value="/{id}", produces="application/json")
    public ResponseEntity<Usuario> getUser(
        @PathVariable(value="id") Long id
    )
    {
        Optional<Usuario> user1 = usuarioRepository.findById(id);

        return new ResponseEntity<Usuario>(user1.get(),HttpStatus.OK);
    }
    
    @GetMapping(value="/", produces="application/json")
    public ResponseEntity<ArrayList<Usuario>> init(
    ){
        Usuario user1 = new Usuario(
            "user1@email.com",
            "123"
        );

        Usuario user2 = new Usuario(
            "user2@email.com",
            "456"
        );        

        ArrayList<Usuario> users = new ArrayList<Usuario>();
        users.add(user1);
        users.add(user2);
        
        return new ResponseEntity<ArrayList<Usuario>>(users, HttpStatus.OK);
    }
}