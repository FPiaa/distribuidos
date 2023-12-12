package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.commons.dto.SegmentDTO;

public record UpdateSegmentResponse(@NotNull @Valid SegmentDTO payload) implements Response<SegmentDTO> {
}
