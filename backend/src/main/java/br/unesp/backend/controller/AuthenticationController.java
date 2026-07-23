package br.unesp.backend.controller;

import br.unesp.backend.model.AuthenticationDTO;
import br.unesp.backend.model.LoginResponseDTO;
import br.unesp.backend.model.RegisterDTO;
import br.unesp.backend.model.Usuario;
import br.unesp.backend.repository.UsuarioRepository;
import br.unesp.backend.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

        try {
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (BadCredentialsException e) {
            // erro por parametros
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            // erros 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
   
        if (this.usuarioRepository.findByLogin(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }


        String encryptedPassword = passwordEncoder.encode(data.senha());

  
        Usuario newUser = new Usuario(
            data.nome(),
            data.username(),
            data.email(),
            encryptedPassword,
            data.role()
        );

        this.usuarioRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}