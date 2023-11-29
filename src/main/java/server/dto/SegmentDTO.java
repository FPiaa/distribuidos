package server.dto;

import jakarta.validation.constraints.NotNull;

public record SegmentDTO(@NotNull Long pdi_inicial, @NotNull Long pdi_final, @NotNull Double distancia, String aviso, @NotNull Boolean acessivel) {
}
