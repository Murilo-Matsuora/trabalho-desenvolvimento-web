package br.unesp.backend.model;

import jakarta.persistence.Entity;

@Entity
public class StickyNote extends Anotacao{
    
    private String texto;
    private String cor;

    public StickyNote(double x, double y, double altura, double largura, String texto, String cor) {
        super(x, y, altura, largura);
        this.texto = texto;
        this.cor = cor;
    }

    public StickyNote() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    
    
}
