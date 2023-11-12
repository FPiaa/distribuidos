package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginScreen.fxml"));
        Scene scene = new Scene(loader.load(), 300, 750);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.setTitle("My application");
        stage.setScene(scene);
        stage.show();
    }
}
