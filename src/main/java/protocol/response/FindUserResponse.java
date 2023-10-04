package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.entity.User;

public record FindUserResponse(@NotNull @Valid User payload) implements Response<User> {

}
