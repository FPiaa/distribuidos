package protocol.response;

import protocol.commons.Command;

import java.util.List;

public record FindRouteResponse(Payload payload) implements Response<FindRouteResponse.Payload>{
    public FindRouteResponse(List<Command> commands) {
       this(new Payload(commands));
    }

    public record Payload (List<Command> comandos) {}
}
