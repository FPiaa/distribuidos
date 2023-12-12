package server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectedUsers {
    private final Set<String> users;
    private final ReentrantLock mutex;

    public ConnectedUsers() {
       users = new HashSet<>();
       mutex = new ReentrantLock();
    }

    public void showConnectedUsers() {
        try {
            mutex.lock();
            System.out.println("Usuários conectados");
            for (String user : users) {
                System.out.println(user);
            }
        } finally {
            mutex.unlock();
        }
    }

    public void addUser(String user) {
        try {
            mutex.lock();
            users.add(user);
        } finally {
            mutex.unlock();
        }
    }

    public void removeUser(String user) {
        try {
            mutex.lock();
            users.remove(user);
        } finally {
            mutex.unlock();
        }
    }
}
