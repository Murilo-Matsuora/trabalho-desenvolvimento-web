
package br.unesp.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class Usuario{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String email;
    private String senha;

    @OneToMany
    private List<Whiteboard> whiteboards = new ArrayList<>();
    
    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Whiteboard> getWhiteboards() {
        return whiteboards;
    }

    public void setWhiteboards(List<Whiteboard> whiteboards) {
        this.whiteboards = whiteboards;
    }

    

    
    
    
} 