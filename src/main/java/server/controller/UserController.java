package server.controller;

import jwt.JwtHelper;
import protocol.request.LoginRequest;
import server.entity.UserEntity;
import server.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.stream.Stream;

public class UserController {
    private static UserController instance = null;

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    private UserController() {
    }

    public String login(LoginRequest.Payload ignoredUserToLogin) throws ResourceNotFoundException {
        return JwtHelper.createJWT(true, 1);
    }

    public Stream<UserEntity> findUsers() {
        var users = new ArrayList<UserEntity>();
        users.add(new UserEntity("joao", "joao@gamil.com", "asd", false, 1));
        return users.stream();
    }

    public UserEntity findUser(int id) {
        return new UserEntity("joao", "joao@gamil.com", "asd", false, 1);
    }

    public UserEntity createUser(UserEntity user) {
        return user;
    }

}
