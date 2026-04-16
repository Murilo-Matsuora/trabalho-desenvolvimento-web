package br.unesp.backend.model;

public class Pessoa extends Usuario{
    private String nome;
    private String sobrenome;

    public Pessoa(){
        
    }

    public Pessoa(String email, String senha, UserRole role, String nome, String sobrenome) {
        super.setEmail(email);
        super.setSenha(senha);
        super.setRole(role);
        this.nome = nome;
        this.sobrenome = sobrenome;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    

}
