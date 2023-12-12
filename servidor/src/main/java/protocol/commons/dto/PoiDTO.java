package protocol.commons.dto;

import commons.Position;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PoiDTO(@NotNull Long id, @NotNull @NotBlank String nome, @Valid @NotNull Position posicao, String aviso,
                     @NotNull Boolean acessivel) {

    public PoiDTO update(PoiDTO poi) {
        long id = poi.id();
        String nome = poi.nome() == null ? nome() : poi.nome();
        Position posicao = poi.posicao() == null ? posicao() : poi.posicao();
        String aviso = poi.aviso() == null ? aviso() : poi.aviso();
        boolean acessivel = poi.acessivel() == null ? acessivel() : poi.acessivel();

        return new PoiDTO(id, nome, posicao, aviso, acessivel);

    }
}
