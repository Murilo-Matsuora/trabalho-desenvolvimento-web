package br.unesp.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tb_pasta")
public class Pasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // Define se a pasta e visivel por todos os usuarios
    @Column(nullable = false)
    private boolean publica = false;

    // Usuario proprietario da pasta
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Auto-relacionamento para suporte a subpastas
    @ManyToOne
    @JoinColumn(name = "pasta_pai_id")
    @JsonIgnoreProperties({"subpastas", "whiteboards"})
    private Pasta pastaPai;

    // Subpastas contidas dentro desta pasta
    @OneToMany(mappedBy = "pastaPai", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Pasta> subpastas = new ArrayList<>();

    // Quadros brancos associados a esta pasta
    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Whiteboard> whiteboards = new ArrayList<>();

    

    public Pasta() {
    }

    public Pasta(String nome, boolean publica, Usuario usuario, Pasta pastaPai) {
        this.nome = nome;
        this.publica = publica;
        this.usuario = usuario;
        this.pastaPai = pastaPai;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pasta getPastaPai() {
        return pastaPai;
    }

    public void setPastaPai(Pasta pastaPai) {
        this.pastaPai = pastaPai;
    }

    public List<Pasta> getSubpastas() {
        return subpastas;
    }

    public void setSubpastas(List<Pasta> subpastas) {
        this.subpastas = subpastas;
    }

    public List<Whiteboard> getWhiteboards() {
        return whiteboards;
    }

    public void setWhiteboards(List<Whiteboard> whiteboards) {
        this.whiteboards = whiteboards;
    }
    

    @Transient
    public String getNomeDono() {
        if (this.usuario != null) {
            return this.usuario.getUsername(); 
        }
        return "Desconhecido";
    }

    @Transient
    public String getDono() {
        if (this.usuario != null) {
            return this.usuario.getUsername(); 
        }
        return "Desconhecido";
    }
}