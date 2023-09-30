package request.requisition;

import json.annotation.JsonOptional;
import org.checkerframework.checker.nullness.qual.NonNull;
import request.Request;
import request.header.Header;

public record EmptyRequest(@NonNull Header header, @JsonOptional EmptyRequest.Payload payload) implements Request<EmptyRequest.Payload> {
    public static class Payload { }
}
