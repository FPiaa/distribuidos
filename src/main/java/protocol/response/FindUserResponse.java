package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.commons.dto.UserDTO;

public record FindUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {

}
