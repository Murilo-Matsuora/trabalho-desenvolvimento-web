package br.unesp.backend.model;

import jakarta.persistence.Entity;

@Entity
public class Secao extends Anotacao{

    private String titulo;

    public Secao(double x, double y, double altura, double largura, String titulo) {
        super(x, y, altura, largura);
        this.titulo = titulo;
    }

    public Secao() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    
    
}
