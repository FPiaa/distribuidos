package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.SegmentDTO;

public record CreateSegmentResponse(@NotNull @Valid SegmentDTO payload) implements Response<SegmentDTO> {
}
