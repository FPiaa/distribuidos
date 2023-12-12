package client;

import lombok.Getter;
import lombok.Setter;
import protocol.commons.dto.UserDTO;


@Getter
@Setter
public class Session {
    private String token;
    private UserDTO user;
    private static Session session = null;

    private Session() {
    }

    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }

        return session;
    }
}
