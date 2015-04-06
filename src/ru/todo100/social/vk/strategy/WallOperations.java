package ru.todo100.social.vk.strategy;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLInputElement;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.PostData;
import ru.todo100.social.vk.datas.UserData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Bobko
 */
@SuppressWarnings("UnusedDeclaration")
public class WallOperations extends Operations {
    public WallOperations(String accessToken) {
        super(accessToken);
    }

    public List<PostData> get(Integer owner_id, String domain, Integer offset, Integer count, String filter, Short extended) {
        try {
            StringBuilder urlString = getStringBuilder("wall.get");
            if (owner_id != null) {
                urlString.append("&owner_id=").append(owner_id);
            }
            if (domain != null) {
                urlString.append("&domain=").append(domain);
            }

            if (offset != null) {
                urlString.append("&offset=").append(offset);
            }
            if (count != null) {
                urlString.append("&count=").append(count);
            }

            if (filter != null) {
                urlString.append("&filter=").append(filter);
            }

            if (extended != null) {
                urlString.append("&extended=").append(extended);
            }

            String responseBody = getResponse(urlString.toString());

            JSONObject object = new JSONObject(responseBody);
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<PostData> posts = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                PostData post = new PostData();
                post.setId(items.getJSONObject(i).getLong("id"));
                post.setText(items.getJSONObject(i).getString("text"));
                posts.add(post);
            }
            return posts;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean delete(int owner_id, Long post_id) {
        try {
            URL url = new URL("https://api.vk.com/method/wall.delete?owner_id=" + owner_id
                    + "&post_id=" + post_id
                    + "&v=5.29&access_token=" + accessToken);

            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            JSONObject object = new JSONObject(builder.toString());
            return !object.has("error");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean post(Long owner_id, Integer friends_only, Integer from_group, String message, String attachments) {
        try {
            StringBuilder urlString = getStringBuilder("wall.post");
            if (owner_id != null) {
                urlString.append("&owner_id=").append(owner_id);
            }
            if (friends_only != null) {
                urlString.append("&friends_only=").append(friends_only);
            }

            if (from_group != null) {
                urlString.append("&from_group=").append(from_group);
            }
            if (message != null) {
                urlString.append("&message=").append(message);
            }

            if (attachments != null) {
                urlString.append("&attachments=").append(attachments);
            }
            String responseBody = getResponse(urlString.toString());
            JSONObject object = new JSONObject(responseBody);
//            if (object.has("error")) {
//                validate(object);
//            }
            return !object.has("error");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

//    void validate(JSONObject jo) {
////        Bounds parentBounds = new BoundingBox(0,0,0,0) {
////        }
//
//        VBox root = new VBox();
//
//
//        final WebView webView = new WebView();
//        root.getChildren().addAll(webView);
//        Scene scene = new Scene(root, 300, 250);
//
//        webView.getEngine().setConfirmHandler(new Callback<String, Boolean>() {
//            @Override public Boolean call(String msg) {
//                return true;
//            }
//        });
//        webView.getEngine().load("http://www.w3schools.com/js/tryit.asp?filename=tryjs_confirm");
//
//
//
//        String captchaUrl;
//        try {
//            captchaUrl = jo.getJSONObject("error").getString("redirect_uri");
//
//            System.out.println(jo.getJSONObject("error").getString("redirect_uri"));
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//            return;
//        }
//
//        webView.getEngine().load(captchaUrl);
//        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
//            @Override
//            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
////                if (newValue == Worker.State.SUCCEEDED) {
////                    System.out.println(webView.getEngine().getDocument().toString());
//////                    if (webView.getEngine().getLocation().contains("#")) {
//////                        webView.setVisible(false);
//////                        String[] splitLocation = webView.getEngine().getLocation().split("#access_token=");
//////                        String[] temp = splitLocation[1].split("&");
//////                        Engine.accessToken = temp[0];
//////                        UserOperations user = new UserOperations(Engine.accessToken);
//////                        UserData userData = user.get();
//////                        yourNameLabel.setText(yourNameText.replace("#YOUR_VK_NAME", userData.getFirstName() + " " + userData.getLastName()));
//////                    }
//////
//////                    NodeList nodes = webView.getEngine().getDocument().getElementsByTagName("input");
//////                    for (int i = 0; i < nodes.getLength(); i++) {
//////                        Node element = nodes.item(i).getAttributes().getNamedItem("name");
//////                        if (element.getNodeValue().equals("email")) {
//////                            nodes.item(i).getAttributes().getNamedItem("value").setNodeValue(login);
//////                        }
//////                        if (element.getNodeValue().equals("pass")) {
//////                            HTMLInputElement passwordElement = (HTMLInputElement) nodes.item(i);
//////                            passwordElement.setValue(password);
//////                        }
//////                        if (element.getNodeValue().equals("submit")) {
//////                            HTMLInputElement submitElement = (HTMLInputElement) nodes.item(i);
//////                            submitElement.click();
//////                        }
//////                    }
////                }
//            }
//        });
//
//    }
}
