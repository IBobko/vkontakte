package ru.todo100.social.vk.controllers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLInputElement;
import ru.todo100.social.Install;
import ru.todo100.social.SpringFXMLLoader;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.UserData;
import ru.todo100.social.vk.strategy.UserOperations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

@SuppressWarnings({"FieldCanBeLocal", "UnusedParameters", "SpellCheckingInspection"})
public class LandingController extends AbstractController implements Initializable {
    final String yourNameText = "Вы вошли как: #YOUR_VK_NAME";
    private final String clientId = "4742608";
    private final String login = "79686398038";
    private final String password = "kjfry4hu575gt5hy";
    public GridPane gridPane;
    public Menu groupsMenu;
    public ImageView userAvatar;

    @FXML
    private Label yourNameLabel;

    @FXML
    private WebView webView;

    @FXML
    private TableView groupsInfo;

    private Stage userGroupsWindow;

    public void initialize(URL location, ResourceBundle resources) {
        new Install();
        this.init();
    }

    protected void init() {


        groupsMenu.setDisable(true);
        webView.getEngine().load("https://oauth.vk.com/authorize?client_id=" + this.clientId + "&scope=friends,messages,wall,groups,video&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.27N&response_type=token");
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    if (webView.getEngine().getLocation().contains("#")) {
                        webView.setVisible(false);
                        String[] splitLocation = webView.getEngine().getLocation().split("#access_token=");
                        String[] temp = splitLocation[1].split("&");
                        Engine.accessToken = temp[0];
                        UserOperations user = new UserOperations(Engine.accessToken);
                        UserData userData = user.get();
                        groupsMenu.setDisable(false);
                        yourNameLabel.setText(yourNameText.replace("#YOUR_VK_NAME", userData.getFirstName() + " " + userData.getLastName()));

                        userAvatar.setVisible(true);
                        try {
                            URL url = new URL("http://cs623727.vk.me/v623727991/2414b/L4EcNPHuoXY.jpg");
                            URLConnection connection = url.openConnection();
                            InputStream in = connection.getInputStream();

                            Image image = new Image(in);


                            userAvatar.setImage(image);
                            userAvatar.setFitHeight(image.getHeight());
                            userAvatar.setFitWidth(image.getWidth());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    NodeList nodes = webView.getEngine().getDocument().getElementsByTagName("input");
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node element = nodes.item(i).getAttributes().getNamedItem("name");
                        if (element.getNodeValue().equals("email")) {
                            nodes.item(i).getAttributes().getNamedItem("value").setNodeValue(login);
                        }
                        if (element.getNodeValue().equals("pass")) {
                            HTMLInputElement passwordElement = (HTMLInputElement) nodes.item(i);
                            passwordElement.setValue(password);
                        }
                        if (element.getNodeValue().equals("submit")) {
                            HTMLInputElement submitElement = (HTMLInputElement) nodes.item(i);
                            submitElement.click();
                        }
                    }
                }
            }
        });
    }

    public void showUserGroups(ActionEvent actionEvent) {
        Controller controller = SpringFXMLLoader.load("ru/todo100/social/vk/controllers/userGroups.fxml");
        Scene scene = new Scene((Parent) controller.getView(), 700, 500);
        userGroupsWindow = new Stage();
        userGroupsWindow.setTitle("Группы пользователя");
        userGroupsWindow.setScene(scene);
        userGroupsWindow.show();

    }


    public void searchGroups(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    this.getClass().getResource("searchGroups.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("Поиск групп");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitAction(ActionEvent actionEvent) {
        accountExit();
    }

    public void accountExit() {
        java.net.CookieManager manager = new java.net.CookieManager();
        java.net.CookieHandler.setDefault(manager);
        manager.getCookieStore().removeAll();
        webView.setVisible(true);
        init();
        Engine.accessToken = null;
        groupsMenu.setDisable(true);
        userAvatar.setVisible(false);
        yourNameLabel.setText("Вход не выполнен");
    }

    public void onContactClicked(javafx.scene.input.MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (Engine.accessToken != null) {
                UserOperations userOperations = new UserOperations(Engine.accessToken);
                UserData user = userOperations.get();

                HostServicesDelegate hostServices = HostServicesFactory.getInstance(Engine.application);
                String url = "http://vk.com/id" + user.getId();
                hostServices.showDocument(url);
            }
        }
    }

    public void closeWindowAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onActionSettings(ActionEvent actionEvent) {
        Controller controller = SpringFXMLLoader.load("ru/todo100/social/vk/controllers/settings.fxml");
        Scene scene = new Scene((Parent) controller.getView());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Настройки");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void onActionLogs(ActionEvent actionEvent) {
        Controller controller = SpringFXMLLoader.load("ru/todo100/social/vk/controllers/logs.fxml");
        Scene scene = new Scene((Parent) controller.getView(), 900, 500);
        Stage stage = new Stage();
        stage.setTitle("Логи отправленных сообщений");
        stage.setScene(scene);
        stage.show();
    }
}
