package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.SegmentDTO;

import java.util.List;

public record FindSegmentsResponse(@NotNull @Valid Payload payload) implements Response<FindSegmentsResponse.Payload> {

    public record Payload(@NotNull List<@Valid @NotNull SegmentDTO> segmentos) {}
}