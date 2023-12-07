package protocol.commons.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SegmentDTO(@NotNull Long pdi_inicial, @NotNull Long pdi_final, Double distancia, String aviso, @NotNull Boolean acessivel) {
    public SegmentDTO update(SegmentDTO segment) {
        long pdi_inicail = segment.pdi_inicial() == null ? pdi_inicial() : segment.pdi_inicial();
        long pdi_final = segment.pdi_final() == null ? pdi_final() : segment.pdi_final();

        double distancia = segment.distancia() == null ? distancia() : segment.distancia();
        String aviso = segment.aviso() == null ? aviso() : segment.aviso();
        boolean acessivel = segment.acessivel() == null ? acessivel() : segment.acessivel();

        return new SegmentDTO(pdi_inicail, pdi_final, distancia, aviso, acessivel);

    }




}
