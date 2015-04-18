package ru.todo100.social.vk.strategy;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.AntiCaptcha;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Igor Bobko
 */
@SuppressWarnings("StatementWithEmptyBody")
public class Operations {
    private Logger LOG = Logger.getLogger(Operations.class);
    public static AntiCaptcha antiCaptcha;
    protected String accessToken;

    public Operations(String accessToken) {
        this.accessToken = accessToken;

    }

    /**
     * This method encode string url to http format
     *
     * @param url Url String for encoded
     * @return encodedString
     */
     public String getEncodedUrlString(String url) {
        StringBuilder encodedString = new StringBuilder();
        try {
            final String[] urlParts = url.split("\\?");
            encodedString.append(urlParts[0]);
            if (urlParts.length > 1) {
                encodedString.append("?");
                final String query = urlParts[1];
                List<String> pairsList = new ArrayList<>();
                for (final String param : query.split("&")) {
                    final String[] pair = param.split("=");
                    if (pair.length < 2) continue;
                    final String key = URLEncoder.encode(pair[0], "UTF-8");
                    final String value = URLEncoder.encode(pair[1], "UTF-8");
                    pairsList.add(key + "=" + value);
                }
                encodedString.append(String.join("&",pairsList));
            }
            return encodedString.toString();
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    public String getResponse(String urlString) throws IOException {
        String encodedUrl = getEncodedUrlString(urlString);
        String siteResponse =  getSiteResponse(encodedUrl);
        LOG.info(siteResponse);
        try {
            JSONObject jsonResponse = new JSONObject(siteResponse);
            if (jsonResponse.has("error")) {
                try {
                    return error(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return siteResponse;
    }

    /**
     * Этот метод получает ответ по указанному адресу и в случае ConnectionException делает коннект ровно пять раз.
     * Если соединение после пяти раз не удается сделать, выбрасывает окончательный ConnectException
     * Пришедший URL должен быть закодирован
     *
     * @param urlString адрес
     * @return Результат ответа сервера
     * @throws IOException
     */

    public String getSiteResponse(String urlString) throws IOException {
        final Integer MAX_CONNECTION_COUNT = 5;
        Integer counter = 0;
        boolean connected = false;
        StringBuilder builder = new StringBuilder();
        do {
            builder.setLength(0);
            if (counter >= MAX_CONNECTION_COUNT) {
                throw new ConnectException("Error connection to:" + urlString);
            }
            counter++;
            try {
                LOG.info(urlString);
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();
                Charset charset = Charset.forName("UTF8");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), charset));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    builder.append(inputLine);
                }
                connected = true;
            } catch (ConnectException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        } while (!connected);
        return builder.toString();
    }

    public StringBuilder getStringBuilder(String methodName) {
        StringBuilder urlString = new StringBuilder("https://api.vk.com/method/" + methodName + "?");
        urlString.append("&v=5.29&access_token=").append(accessToken);
        return urlString;
    }

    public String error(JSONObject response) throws Exception {
        if (response == null) return null;
        try {
            JSONObject error = response.getJSONObject("error");
            int error_code = error.getInt("error_code");
            if (error_code == 15) {
                return "{response: {post_id:0}}";
            }

            if (error_code == 6) {
                try {
                    Thread.sleep(3000);
                    JSONArray array = error.getJSONArray("request_params");
                    return repeatRequest(array);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (error_code == 14) {
                String captchaImg = error.getString("captcha_img");
                String captcha_sid = error.getString("captcha_sid");
                URL url = new URL(captchaImg);

                boolean wasConnected = false;
                InputStream in;
                File f = null;
                do {
                    try {
                        URLConnection connection = url.openConnection();


                        in = connection.getInputStream();


                        f = new File(captcha_sid);
                        boolean newFile = f.createNewFile();
                        if (!newFile) {
                            throw new Exception("Can't create file");
                        }

                        FileOutputStream o =
                                new FileOutputStream(f);

                        int read;
                        byte[] bytes = new byte[1024];

                        while ((read = in.read(bytes)) != -1) {
                            o.write(bytes, 0, read);
                        }
                    } catch(ConnectException e) {
                        continue;
                    }
                    wasConnected = true;
                } while(!wasConnected);


                String key = antiCaptcha.getCaptcha(f);

                boolean deleted = f.delete();
                if (!deleted) {
                    throw new Exception("Can't delete file");
                }


                if (key == null) return "{response: {post_id:0}}";
                JSONArray array = error.getJSONArray("request_params");
                JSONObject captcha_sid_json = new JSONObject();
                captcha_sid_json.put("key", "captcha_sid");
                captcha_sid_json.put("value", captcha_sid);

                array.put(captcha_sid_json);

                JSONObject captcha_key_json = new JSONObject();
                captcha_key_json.put("key", "captcha_key");
                captcha_key_json.put("value", key);
                array.put(captcha_key_json);
                return repeatRequest(array);
            }

            if (error_code == 220) {
                try {
                    Thread.sleep(3000);
                    JSONArray array = error.getJSONArray("request_params");
                    return repeatRequest(array);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return "{response: {post_id:0}}";
    }

    public String repeatRequest(JSONArray params) {
        String methodName = findValue(params, "method");

        StringBuilder request = getStringBuilder(methodName);
        System.out.println(params.toString());
        for (int i = 0; i < params.length(); i++) {
            try {
                if (!params.getJSONObject(i).has("key")) continue;
                if (!params.getJSONObject(i).getString("key").equals("method")) {
                    request.append("&").append(params.getJSONObject(i).getString("key")).append("=").append(params.getJSONObject(i).getString("value"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            return getResponse(request.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findValue(JSONArray array, String name) {
        for (int i = 0; i < array.length(); i++) {
            try {
                if (array.getJSONObject(i).getString("key").equals(name)) {
                    return array.getJSONObject(i).getString("value");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
