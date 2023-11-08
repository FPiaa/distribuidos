package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.UserDTO;
import server.entity.User;

public record UpdateUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {
    public static UpdateUserResponse of(User user) {
        return new UpdateUserResponse(UserDTO.of(user));
    }
}
