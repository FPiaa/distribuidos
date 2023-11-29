package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.PoiDTO;

import java.util.List;

public record FindPoisResponse(@Valid @NotNull Payload payload) implements Response<FindPoisResponse.Payload> {

    public record Payload(@NotNull List<@Valid @NotNull PoiDTO> pdis){}
}
