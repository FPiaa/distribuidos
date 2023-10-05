package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.UserDTO;

public record AdminCreateUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {
}
