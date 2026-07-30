package br.unesp.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_imagem")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String url;
    
    @Column(nullable = false)
    private boolean publica = true;

    private Integer salvos = 0;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario autor;

    private LocalDateTime dataCriacao;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "pasta_id")
    private Pasta pasta;

    @ElementCollection
    private List<String> tags;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Imagem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public boolean isPublica() { return publica; }
    public void setPublica(boolean publica) { this.publica = publica; }

    public Integer getSalvos() { return salvos; }
    public void setSalvos(Integer salvos) { this.salvos = salvos; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pasta getPasta() {
        return pasta;
    }

    public void setPasta(Pasta pasta) {
        this.pasta = pasta;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}