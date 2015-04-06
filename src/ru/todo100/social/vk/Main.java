package ru.todo100.social.vk;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.todo100.social.Controller;
import ru.todo100.social.SpringFXMLLoader;

public class Main extends Application {

    final private String capkey = "47a6813d86e034fbf9f74432a2231b0a";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Controller controller = (Controller) SpringFXMLLoader.load("ru/todo100/social/vk/controllers/landing.fxml");
        Scene scene = new Scene((Parent) controller.getView(), 700, 500);

        Engine.application = this;

        primaryStage.setTitle("Вконтакте");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
