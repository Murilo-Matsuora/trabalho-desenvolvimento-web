package br.unesp.backend.model;

import jakarta.persistence.Entity;

@Entity
public class Texto extends Anotacao{
    
    private String texto;
    private int tamanho;
    private boolean negrito;
    private boolean italico;
    
    public Texto(double x, double y, double altura, double largura, String texto, int tamanho, boolean negrito,
            boolean italico) {
        super(x, y, altura, largura);
        this.texto = texto;
        this.tamanho = tamanho;
        this.negrito = negrito;
        this.italico = italico;
    }

    public Texto() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public boolean isNegrito() {
        return negrito;
    }

    public void setNegrito(boolean negrito) {
        this.negrito = negrito;
    }

    public boolean isItalico() {
        return italico;
    }

    public void setItalico(boolean italico) {
        this.italico = italico;
    }

    
}
