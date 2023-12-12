package server;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectedUsers {
    @Getter
    private final Set<ConnectedUser> users;
    private final ReentrantLock mutex;
    @Getter
    private final ObservableList<ConnectedUser> usersProperty = FXCollections.observableArrayList();

    public ConnectedUsers() {
       users = new HashSet<>();
       mutex = new ReentrantLock();
    }

    public void addUser( ConnectedUser user) {
        try {
            mutex.lock();
            users.add(user);
        } finally {
            Platform.runLater(() -> {
                usersProperty.setAll(users);
            });
            mutex.unlock();
        }
    }

    public void removeUser(ConnectedUser user) {
        try {
            mutex.lock();
            users.remove(user);
        } finally {
            Platform.runLater(() -> {
                usersProperty.setAll(users);
            });
            mutex.unlock();
        }
    }
}
