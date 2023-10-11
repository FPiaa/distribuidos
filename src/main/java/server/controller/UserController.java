package server.controller;

import jwt.JwtHelper;
import protocol.request.LoginRequest;
import server.dto.CreateUser;
import server.dto.DeleteUser;
import server.dto.UpdateUser;
import server.dto.UserDTO;
import server.entity.User;
import server.exceptions.ResourceNotFoundException;
import server.exceptions.ServerResponseException;
import server.exceptions.UnauthorizedAccessException;
import server.repository.UserRepository;

import java.util.List;

public class UserController {
    private static UserController instance = null;
    private final UserRepository repository = new UserRepository();

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public String login(LoginRequest.Payload login) throws UnauthorizedAccessException {
        var user = repository.login(login.email()).orElseThrow(UnauthorizedAccessException::new);

        if(!user.getSenha().equals(login.password())) {
            throw new UnauthorizedAccessException();
        }

        return JwtHelper.createJWT(user.getIsAdmin(), user.getId());
    }

    public List<UserDTO> findUsers() {
        return repository.findAll()
                .stream()
                .map(UserDTO::of)
                .toList();
    }

    public UserDTO findUser(long id) throws ResourceNotFoundException {
        var entity = repository.find(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserDTO.of(entity);
    }

    public UserDTO createUser(CreateUser user) throws ServerResponseException {
        var entity = User.of(user);
        repository.create(entity);
        return UserDTO.of(entity);
    }

    public UserDTO updateUser(UpdateUser user) throws ServerResponseException {
        var entity = User.of(user);
        repository.update(user.registro(), entity);
        return UserDTO.of(entity);
    }

    public void deleteUser(DeleteUser userToDelete) {
        repository.deleteById(userToDelete.registroToDelete());
    }
}