package br.unesp.backend.model;

public record RegisterDTO(
    String nome,
    String username,
    String email,
    String senha,
    UserRole role
) {}