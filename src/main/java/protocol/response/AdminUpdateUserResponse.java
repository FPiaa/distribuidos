package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.UserDTO;
import server.entity.UserEntity;

public record AdminUpdateUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {
    public static AdminUpdateUserResponse of(UserEntity user) {
        return new AdminUpdateUserResponse(UserDTO.of(user));
    }
}
