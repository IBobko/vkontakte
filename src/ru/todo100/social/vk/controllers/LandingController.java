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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLInputElement;
import ru.todo100.social.Install;
import ru.todo100.social.SpringFXMLLoader;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.PostData;
import ru.todo100.social.vk.datas.UserData;
import ru.todo100.social.vk.strategy.*;
import ru.todo100.social.vk.strategy.impl.VkontakteFactoryApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings({"FieldCanBeLocal", "UnusedParameters", "SpellCheckingInspection"})
public class LandingController extends AbstractController implements Initializable {
    final String yourNameText = "Вы вошли как: #YOUR_VK_NAME";
    private final String clientId = "4742608";
//    private final String login = "79686398038";
//    private final String password = "kjfry4hu575gt5hy";

    private final String login = "79686387967";
    private final String password = "hjtiuertruh4rriy";

    public GridPane gridPane;
    public Menu groupsMenu;
    public ImageView userAvatar;
    private VkontakteFactoryApi vkontakteFactoryApi;
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
        webView.getEngine().getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
            @Override
            public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
                System.out.println("Hello");
            }
        });

        webView.getEngine().getLoadWorker().messageProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Hello");
        });

        webView.getEngine().getLoadWorker().progressProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Hello");
        });

        webView.getEngine().getLoadWorker().runningProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Hello");
        });

        webView.getEngine().getLoadWorker().workDoneProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Hello");
        });

        webView.getEngine().getLoadWorker().valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Hello");
        });

        webView.getEngine().getLoadWorker().valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Hello");
        });

        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                System.out.println("Clicked");
                if (newValue == Worker.State.SUCCEEDED) {
                    if (webView.getEngine().getLocation().contains("#")) {
                        webView.setVisible(false);
                        String[] splitLocation = webView.getEngine().getLocation().split("#access_token=");
                        String[] temp = splitLocation[1].split("&");
                        getVkontakteFactoryApi().addVkontakteApi(login, temp[0]);
                        Engine.accessToken = temp[0];
                        UserOperations user = getVkontakteFactoryApi().getDefaultVkontakteApi().getUserOperations();
                        UserData userData = user.get();
                        groupsMenu.setDisable(false);
                        yourNameLabel.setText(yourNameText.replace("#YOUR_VK_NAME", userData.getFirstName() + " " + userData.getLastName()));


                        userAvatar.setVisible(true);
                        try {
                            URL url = new URL(userData.getPhoto200_Orig());
                            URLConnection connection = url.openConnection();
                            InputStream in = connection.getInputStream();
                            Image image = new Image(in);
                            userAvatar.setImage(image);
                            userAvatar.setFitHeight(image.getHeight());
                            userAvatar.setFitWidth(image.getWidth());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        //likes();
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
                            //submitElement.
                            //ЫрщцsubmitElement.click();
                        }
                    }
                }
            }
        });
    }

    public void likes() {

        File file = new File("/home/igor/log.txt");
        File file2 = new File("/home/igor/Юля.txt");

        try {

            if (!file.exists()) {
                file.createNewFile();
            }

            if (!file2.exists()) {
                file2.createNewFile();
            }

            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            PrintWriter out2 = new PrintWriter(file2.getAbsoluteFile());


            //LikesOperations likes = new LikesOperations(Engine.accessToken);
            //likes.getList("post",99991,4749);

            FriendsOperations friendsOperations = new FriendsOperations(Engine.accessToken);
            List<Integer> friends = friendsOperations.get(172038204);
            WallOperations wall = new WallOperations(Engine.accessToken);

            //кузнаString join = String.join(",", friends.);



            for (Integer id : friends) {


                //List<Integer> users10 = friends.get(id);

//                            for (Integer id2: users10) {
//                                List<PostData> w = wall.get(id2, null, 0, 50, null, null);
//                                UserOperations user1 = new UserOperations(Engine.accessToken);
//                                List<UserData> u = user1.get(""+id2);
//                                UserData uu2 = u.get(0);
//                                out2.println("Friends of friends" + uu2.getFirstName() + " " + uu2.getLastName());
//                                out2.flush();
//                                for (PostData po: w) {
//                                    LikesOperations likesOperations = new LikesOperations(Engine.accessToken);
//                                    String likes = likesOperations.getList("post", id, po.getId().intValue());
//                                    out2.println("--- #" + " --- " + po.getText() + " likes ");
//                                    out2.flush();
//
//                                    if (likes == null) continue;
//
//                                    JSONObject response = null;
//                                    try {
//                                        response = new JSONObject(likes);
//                                        JSONObject o = response.getJSONObject("response");
//
//                                        JSONArray r = o.getJSONArray("items");
//                                        for (int i = 0; i < r.length(); i++) {
//                                            out2.println("---------- likes: " + r.get(i));
//                                            if (r.get(i).equals("172038204") || r.get(i).equals(172038204)) {
//                                                out2.println(uu2.getId() + " " + uu2.getFirstName() + " " + uu2.getLastName() + " " + po.getText());
//                                                out2.flush();
//                                            }
//                                            System.out.println(r.getInt(i));
//                                        }
//                                        out.flush();
//
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                }
//
//                            }


                List<PostData> w = wall.get(id, null, 0, 10, null, null);
                UserOperations user1 = new UserOperations(Engine.accessToken);
                List<UserData> u = user1.get("" + id);
                UserData uu = u.get(0);
                out.println("##" + uu.getFullName());
                out.flush();
                int i2 = 1;
                for (PostData post : w) {
                    Date date = new Date();
                    date.setTime(post.getDate());
                    Date now = new Date();
                    now.setDate(01);

                    if (date.after(now)) {
                        out.println("### Пропускаем, слишком рано");
                        continue;
                    }


                    LikesOperations likesOperations = new LikesOperations(Engine.accessToken);
                    String likes = likesOperations.getList("post", id, post.getId().intValue());
                    out.println("--- #" + i2 + " --- " + post.getText() + " likes ");
                    out.flush();

                    System.out.println("LIKES!!! GO GO GO!!!" + likes);

                    if (likes == null) continue;
                    try {
                        JSONObject response = new JSONObject(likes);
                        JSONObject o = response.getJSONObject("response");
                        JSONArray r = o.getJSONArray("items");
                        for (int i = 0; i < r.length(); i++) {
                            out.println("---------- likes: " + r.get(i));
                            if (r.get(i).equals("172038204") || r.get(i).equals(172038204)) {
                                out2.println(uu.getId() + " " + uu.getFirstName() + " " + uu.getLastName() + " " + post.getText());
                                out2.flush();
                            }
                            System.out.println(r.getInt(i));
                        }
                        out.flush();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println(likes);
                    i2++;
                }
                System.out.println(id);
            }

//                            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


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
                UserOperations userOperations = getVkontakteFactoryApi().getDefaultVkontakteApi().getUserOperations();
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

    public VkontakteFactoryApi getVkontakteFactoryApi() {
        return vkontakteFactoryApi;
    }

    public void setVkontakteFactoryApi(VkontakteFactoryApi vkontakteFactoryApi) {
        this.vkontakteFactoryApi = vkontakteFactoryApi;
    }

    public void showFriendsByUserId(ActionEvent actionEvent) {
        Controller controller = SpringFXMLLoader.load("ru/todo100/social/vk/controllers/userFriends.fxml");
        Scene scene = new Scene((Parent) controller.getView(), 900, 500);
        Stage stage = new Stage();
        stage.setTitle("Друзья");
        stage.setScene(scene);
        stage.show();
    }
}
