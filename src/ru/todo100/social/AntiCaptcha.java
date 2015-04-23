package ru.todo100.social;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.util.StringUtils;
import ru.todo100.social.vk.controllers.Controller;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Igor Bobko
 */

public class AntiCaptcha {
    private Settings settings;

    public String getCaptcha(String id) {
        try {
            while (true) {
                URL url = new URL("http://anti-captcha.com/res.php?key=" + getKey() + "&action=get&id=" + id);
                URLConnection connection = url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuilder builder = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    builder.append(inputLine);
                }
                String[] answer = builder.toString().split("\\|");
                if (answer[0].equals(Response.CAPCHA_NOT_READY.toString())) {
                    System.out.println("Captcha not ready. Waiting for 1s");
                    Thread.sleep(1000);
                    continue;
                }

                if (answer[0].equals(Response.ERROR_NO_SLOT_AVAILABLE.toString())) {
                    System.out.println("No slot available");
                    Thread.sleep(1000);
                    continue;
                }

                if (answer[0].equals(Response.ERROR_CAPTCHA_UNSOLVABLE.toString())) {
                    System.out.println("Error captcha unsolvable");
                    break;
                }

                if (answer[0].equals(Response.ERROR_KEY_DOES_NOT_EXIST.toString())) {
                    System.out.println("Error key does not exists");
                    break;
                }

                if (answer[0].equals(Response.ERROR_NO_SUCH_CAPCHA_ID.toString())) {
                    System.out.println("Error no such captcha id");
                    break;
                }

                if (answer[0].equals(Response.ERROR_WRONG_ID_FORMAT.toString())) {
                    System.out.println("Error wrong id format");
                    break;
                }

                if (answer[0].equals(Response.ERROR_WRONG_USER_KEY.toString())) {
                    System.out.println("Error wrong user key");
                    break;
                }

                if (answer[0].equals(Response.OK.toString())) {
                    System.out.println("Captcha ok: " + answer[1]);
                    return answer[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCaptcha(File file) {
        String id = sendFileAndGetId(file);
        if (id!=null) {
            return getCaptcha(id);
        }
        return null;
    }

    public String sendFileAndGetId(File file) {
        final HttpPost httppost = new HttpPost("http://antigate.com/in.php");
        final FileBody sentFile = new FileBody(file);
        final StringBody key = new StringBody(getKey(), ContentType.TEXT_PLAIN);
        final MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.addPart("file", sentFile);
        reqEntity.addPart("key", key);
        httppost.setEntity(reqEntity.build());
        try {
            final DefaultHttpClient httpClient = new DefaultHttpClient();
            final HttpResponse response = httpClient.execute(httppost);


            final HttpEntity resEntity = response.getEntity();




            BufferedReader in = new BufferedReader(new InputStreamReader(
                    resEntity.getContent()));
            String inputLine;


            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }


            System.out.println(builder.toString());

            if (builder.toString().equalsIgnoreCase(Response.ERROR_NO_SLOT_AVAILABLE.toString())) {
                Thread.sleep(1000);
                return sendFileAndGetId(file);
            }
            if (builder.toString().equalsIgnoreCase(Response.ERROR_WRONG_USER_KEY.toString())) {

//                new Thread(() -> {
//                    Platform.runLater(()->
//                    {
//                        Controller controller = SpringFXMLLoader.load("ru/todo100/social/vk/controllers/confirmAntiCaptchaKey.fxml");
//                        Scene scene = new Scene((Parent) controller.getView(), 700, 500);
//                        Stage stage = new Stage();
//                        stage.setTitle("Подтвердите акнтикапчу");
//                        stage.setScene(scene);
//                        stage.initModality(Modality.WINDOW_MODAL);
//                        stage.show();

//                    });
//                }).start();



                System.out.println("AFTER");

                System.out.println(Response.ERROR_WRONG_USER_KEY.toString());

                System.out.println("Anti Captcha key:" + getKey());
                Thread.sleep(1000);
                return sendFileAndGetId(file);

            }
            if (StringUtils.isEmpty(builder.toString())) {
//                Controller controller = SpringFXMLLoader.load("ru/todo100/social/vk/controllers/confirmAntiCaptchaKey.fxml");
//                Scene scene = new Scene((Parent) controller.getView(), 700, 500);
//                Stage stage = new Stage();
//                stage.setTitle("Подтвердите акнтикапчу");
//                stage.setScene(scene);
//                stage.initModality(Modality.WINDOW_MODAL);
//                stage.show();
//
//
//
//                System.out.println("Anti Captcha key:" + getKey());
//                Thread.sleep(1000);
                return sendFileAndGetId(file);
//
            }

            System.out.println(builder.toString());

            return builder.toString().split("\\|")[1];
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }

    public String getKey() {
        return getSettings().getAntiCaptchaKey();
    }

    public enum Response {
        CAPCHA_NOT_READY,
        OK,
        ERROR_KEY_DOES_NOT_EXIST,
        ERROR_WRONG_ID_FORMAT,
        ERROR_NO_SUCH_CAPCHA_ID,
        ERROR_CAPTCHA_UNSOLVABLE,
        ERROR_WRONG_USER_KEY,
        ERROR_NO_SLOT_AVAILABLE,

    }
}
