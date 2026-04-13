package br.unesp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Anotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double x;
    private double y;
    private double altura;
    private double largura;

    public Anotacao(double x, double y, double altura, double largura) {
        this.x = x;
        this.y = y;
        this.altura = altura;
        this.largura = largura;
    }

    public Anotacao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    
    
    
    
}
