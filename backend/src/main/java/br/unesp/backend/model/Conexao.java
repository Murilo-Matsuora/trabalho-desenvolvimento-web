package br.unesp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Conexao {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String tipo;
    private String estilo;
    private String traco;
    
    @ManyToOne
    @JoinColumn(name = "whiteboard_id", nullable = false)
    @JsonIgnore
    private Whiteboard whiteboard;

    @ManyToOne
    @JoinColumn(name = "origem_id")
    @JsonIgnore
    private Anotacao origem;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    @JsonIgnore
    private Anotacao destino;
    
    public Conexao(String tipo, String estilo, String traco, Anotacao origem, Anotacao destino) {
        this.tipo = tipo;
        this.estilo = estilo;
        this.traco = traco;
        this.origem = origem;
        this.destino = destino;
    }

    public Conexao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getTraco() {
        return traco;
    }

    public void setTraco(String traco) {
        this.traco = traco;
    }
    
    public Whiteboard getWhiteboard() {
        return whiteboard;
    }

    public void setWhiteboard(Whiteboard whiteboard) {
        this.whiteboard = whiteboard;
    }

    public Anotacao getOrigem() {
        return origem;
    }

    public void setOrigem(Anotacao origem) {
        this.origem = origem;
    }

    public Anotacao getDestino() {
        return destino;
    }

    public void setDestino(Anotacao destino) {
        this.destino = destino;
    }

    

    
}
