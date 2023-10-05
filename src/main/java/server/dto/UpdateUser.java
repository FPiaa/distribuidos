package server.dto;

import lombok.Builder;

@Builder
public record UpdateUser(int registro, String email, String nome, String senha, Boolean tipo){
}
