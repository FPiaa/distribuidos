package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.commons.dto.UserDTO;

public record CreateUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {
}
