package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.commons.dto.PoiDTO;

public record UpdatePoiResponse(@NotNull @Valid PoiDTO payload) implements Response<PoiDTO> {
}
