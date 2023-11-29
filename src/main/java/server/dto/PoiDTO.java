package server.dto;

import commons.Position;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PoiDTO(@NotNull Long id, @NotNull @NotBlank String nome, @Valid @NotNull Position posicao, String aviso,
                     @NotNull Boolean acessivel) {
}
