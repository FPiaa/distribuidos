package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.UserDTO;
import server.entity.UserEntity;

public record AdminCreateUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {
    public static AdminCreateUserResponse of(UserEntity user) {
        return new AdminCreateUserResponse(UserDTO.of(user));
    }
}
