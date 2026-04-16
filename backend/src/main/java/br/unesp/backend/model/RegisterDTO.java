package br.unesp.backend.model;

public record RegisterDTO(String email, String senha, UserRole role) {

}