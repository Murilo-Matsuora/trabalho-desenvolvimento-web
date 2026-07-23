package br.unesp.backend.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Imagem extends Anotacao {

    private String url;
    private String descricao;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario autor; 
    
    @ElementCollection
    private List<String> tags = new ArrayList<>();
    
    public Imagem(double x, double y, double altura, double largura, String url, String descricao, Usuario autor) {
        super(x, y, altura, largura);
        this.url = url;
        this.descricao = descricao;
        this.autor = autor;
    }

    public Imagem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}