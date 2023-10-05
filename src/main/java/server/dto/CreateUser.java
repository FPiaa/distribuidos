package server.dto;

import lombok.Builder;

@Builder
public record CreateUser(String email, String senha, String nome, Boolean tipo) {
}
