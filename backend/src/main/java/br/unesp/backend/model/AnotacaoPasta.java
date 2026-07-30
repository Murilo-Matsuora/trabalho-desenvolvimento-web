package br.unesp.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "anotacao_pasta")
public class AnotacaoPasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "anotacao_id", nullable = false)
    private Anotacao anotacao;

    @ManyToOne
    @JoinColumn(name = "pasta_id")
    private Pasta pasta; // Null seria "Geral"

    public AnotacaoPasta() {}

    public AnotacaoPasta(Usuario usuario, Anotacao anotacao, Pasta pasta) {
        this.usuario = usuario;
        this.anotacao = anotacao;
        this.pasta = pasta;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Anotacao getAnotacao() { return anotacao; }
    public void setAnotacao(Anotacao anotacao) { this.anotacao = anotacao; }
    public Pasta getPasta() { return pasta; }
    public void setPasta(Pasta pasta) { this.pasta = pasta; }
}