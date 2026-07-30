package br.unesp.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pinnup_salvo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "imagem_id", "pasta_id"})
})
public class Pinnup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "imagem_id")
    private Imagem imagem;

    @ManyToOne
    @JoinColumn(name = "pasta_id")
    private Pasta pasta;

    private LocalDateTime dataSalvamento;

    @PrePersist
    public void prePersist() {
        this.dataSalvamento = LocalDateTime.now();
    }

    public Pinnup() {}

    public Pinnup(Usuario usuario, Imagem imagem, Pasta pasta) {
        this.usuario = usuario;
        this.imagem = imagem;
        this.pasta = pasta;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Imagem getImagem() { return imagem; }
    public void setImagem(Imagem imagem) { this.imagem = imagem; }

    public Pasta getPasta() { return pasta; }
    public void setPasta(Pasta pasta) { this.pasta = pasta; }

    public LocalDateTime getDataSalvamento() { return dataSalvamento; }
    public void setDataSalvamento(LocalDateTime dataSalvamento) { this.dataSalvamento = dataSalvamento; }
}