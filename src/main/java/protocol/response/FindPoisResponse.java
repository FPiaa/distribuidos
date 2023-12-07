package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.commons.dto.PoiDTO;

import java.util.List;

public record FindPoisResponse(@Valid @NotNull Payload payload) implements Response<FindPoisResponse.Payload> {

    public FindPoisResponse(List<PoiDTO> pois) {
        this(new Payload(pois));
    }
    public record Payload(@NotNull List<@Valid  PoiDTO> pdis){}
}
