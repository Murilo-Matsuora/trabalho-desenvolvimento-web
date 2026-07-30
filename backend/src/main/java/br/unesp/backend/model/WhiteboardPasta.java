package br.unesp.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "whiteboard_pasta")
public class WhiteboardPasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "whiteboard_id", nullable = false)
    private Whiteboard whiteboard;

    // Null seria "Geral"
    @ManyToOne
    @JoinColumn(name = "pasta_id")
    private Pasta pasta;

    public WhiteboardPasta() {}

    public WhiteboardPasta(Usuario usuario, Whiteboard whiteboard, Pasta pasta) {
        this.usuario = usuario;
        this.whiteboard = whiteboard;
        this.pasta = pasta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Whiteboard getWhiteboard() { return whiteboard; }
    public void setWhiteboard(Whiteboard whiteboard) { this.whiteboard = whiteboard; }
    public Pasta getPasta() { return pasta; }
    public void setPasta(Pasta pasta) { this.pasta = pasta; }
}