package protocol.response;

import protocol.commons.Command;

import java.util.List;

public record FindRouteResponse(List<Command> payload) implements Response<List<Command>>{
}
