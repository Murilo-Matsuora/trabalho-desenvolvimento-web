package br.unesp.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
    @NotBlank String nome,
    @NotBlank String username,
    @NotBlank @Email String email,
    @NotBlank String senha,
    UserRole role
) {
}