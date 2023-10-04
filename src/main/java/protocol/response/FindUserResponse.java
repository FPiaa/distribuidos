package protocol.response;

import server.entity.User;

public record FindUserResponse(User payload) implements Response<User> {

}
