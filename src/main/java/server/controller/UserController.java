package server.controller;

import jwt.JwtHelper;
import protocol.request.LoginRequest;
import server.entity.User;
import server.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

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

    public List<User> findUsers() {
        var users = new ArrayList<User>();
        users.add(new User("joao", "joao@gamil.com", false, 1));
        return users;
    }

    public User findUser(int id) {
        return new User("joao", "joao@gamil.com", false, 1);
    }

}
