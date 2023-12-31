package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.commons.dto.SegmentDTO;

import java.util.List;

public record FindSegmentsResponse(@NotNull @Valid Payload payload) implements Response<FindSegmentsResponse.Payload> {
    public FindSegmentsResponse(List<SegmentDTO> segments) {
        this(new Payload(segments));
    }

    public record Payload(@NotNull List<@Valid SegmentDTO> segmentos) {}
}
