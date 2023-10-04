package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import server.entity.User;

import java.util.List;

public record FindUsersResponse(Payload payload) implements Response<FindUsersResponse.Payload> {

    public FindUsersResponse(List<User> users) {
        this(new Payload(users));
    }

    public record Payload(List<@NotNull @Valid User> users) {
    }
}
