package ru.todo100.social;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Igor Bobko
 */

public class AntiCaptcha {
    final String KEY = "47a6813d86e034fbf9f74432a2231b0a";

    public String getCaptcha(String id) {
        try {
            while (true) {
                URL url = new URL("http://anti-captcha.com/res.php?key=" + KEY + "&action=get&id=" + id);
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

        HttpPost httppost = new HttpPost("http://antigate.com/in.php");

        FileBody bin = new FileBody(file);
        StringBody key = new StringBody(KEY, ContentType.TEXT_PLAIN);

        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.addPart("file", bin);
        reqEntity.addPart("key", key);
        httppost.setEntity(reqEntity.build());
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httppost);

            HttpEntity resEntity = response.getEntity();


            BufferedReader in = new BufferedReader(new InputStreamReader(
                    resEntity.getContent()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }

            return builder.toString().split("\\|")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum Response {
        CAPCHA_NOT_READY,
        OK,
        ERROR_KEY_DOES_NOT_EXIST,
        ERROR_WRONG_ID_FORMAT,
        ERROR_NO_SUCH_CAPCHA_ID,
        ERROR_CAPTCHA_UNSOLVABLE,
        ERROR_WRONG_USER_KEY
    }
}
