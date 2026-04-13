package br.unesp.backend.model;

import jakarta.persistence.Entity;

@Entity
public class Imagem extends Anotacao{

    private String url;
    
    public Imagem(double x, double y, double altura, double largura, String url) {
        super(x, y, altura, largura);
        this.url = url;
    }

    public Imagem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
}
