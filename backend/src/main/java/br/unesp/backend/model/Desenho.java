package br.unesp.backend.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Desenho {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @ElementCollection
    private List<Ponto> pontos;
    private String cor;
    private double espessura;
    
    public Desenho(List<Ponto> pontos, String cor, double espessura) {
        this.pontos = pontos;
        this.cor = cor;
        this.espessura = espessura;
    }

    public Desenho() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ponto> getPontos() {
        return pontos;
    }

    public void setPontos(List<Ponto> pontos) {
        this.pontos = pontos;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public double getEspessura() {
        return espessura;
    }

    public void setEspessura(double espessura) {
        this.espessura = espessura;
    }

    

    
}
