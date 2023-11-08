package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.UserDTO;
import server.entity.User;

public record CreateUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {
    public static CreateUserResponse of(User user) {
        return new CreateUserResponse(UserDTO.of(user));
    }
}
