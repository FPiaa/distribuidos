package server.controller;

import jwt.JwtHelper;
import protocol.request.LoginRequest;
import server.dto.CreateUser;
import server.dto.DeleteUser;
import server.dto.UpdateUser;
import server.dto.UserDTO;
import server.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.stream.Stream;

public class UserController {
    private static UserController instance = null;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public String login(LoginRequest.Payload ignoredUserToLogin) throws ResourceNotFoundException {
        return JwtHelper.createJWT(true, 1);
    }

    public Stream<UserDTO> findUsers() {
        var users = new ArrayList<UserDTO>();
        users.add(new UserDTO("joao", "joao@gamil.com", false, 1));
        return users.stream();
    }

    public UserDTO findUser(int id) {
        return new UserDTO("joao", "joao@gamil.com", false, 1);
    }

    public UserDTO createUser(CreateUser user) {
        return null;
    }

    public UserDTO updateUser(UpdateUser user) {
        return null;
    }

    public void deleteUser(DeleteUser user) {

    }
}
