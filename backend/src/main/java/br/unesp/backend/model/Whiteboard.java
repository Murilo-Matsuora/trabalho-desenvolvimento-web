package br.unesp.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Whiteboard {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String titulo;
    private double zoom;
    private double panX;
    private double panY;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Anotacao> anotacoes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conexao> conexoes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Desenho> desenhos = new ArrayList<>();

    public Whiteboard(String titulo, double zoom, double panX, double panY) {
        this.titulo = titulo;
        this.zoom = zoom;
        this.panX = panX;
        this.panY = panY;
    }

    public Whiteboard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getPanX() {
        return panX;
    }

    public void setPanX(double panX) {
        this.panX = panX;
    }

    public double getPanY() {
        return panY;
    }

    public void setPanY(double panY) {
        this.panY = panY;
    }

    public List<Anotacao> getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(List<Anotacao> anotacoes) {
        this.anotacoes = anotacoes;
    }

    public List<Conexao> getConexoes() {
        return conexoes;
    }

    public void setConexoes(List<Conexao> conexoes) {
        this.conexoes = conexoes;
    }

    public List<Desenho> getDesenhos() {
        return desenhos;
    }

    public void setDesenhos(List<Desenho> desenhos) {
        this.desenhos = desenhos;
    }

    

    

}
