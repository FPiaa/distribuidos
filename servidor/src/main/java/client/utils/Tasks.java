package client.utils;

import javafx.concurrent.Task;

public class Tasks {
    public static void run(Task<?> task) {
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
