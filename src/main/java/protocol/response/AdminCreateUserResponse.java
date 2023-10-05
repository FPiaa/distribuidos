package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.dto.UserDTO;
import server.entity.UserEntity;

public record AdminCreateUserResponse(@NotNull @Valid UserDTO payload) implements Response<UserDTO> {
    public static AdminCreateUserResponse of(UserEntity user) {
        var dto = new UserDTO(user.nome(), user.email(), user.isAdmin(), user.id());
        return new AdminCreateUserResponse(dto);
    }
}
