package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.UserDTO;

import java.util.List;

public record FindUsersResponse(@NotNull @Valid Payload payload) implements Response<FindUsersResponse.Payload> {

    public FindUsersResponse(List<UserDTO> users) {
        this(new Payload(users));
    }

    public record Payload(List<@NotNull @Valid UserDTO> users) {
    }
}
