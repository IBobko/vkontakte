package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.AntiCaptcha;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Igor Bobko
 */
public class Operations {
    protected String accessToken;

    public Operations(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResponse(String urlString) throws IOException {
        String[] u = urlString.split("\\?");
        Map<String, List<String>> params = getQueryParams(urlString);

        StringBuilder jjj = new StringBuilder(u[0]);
        jjj.append("?");

        for (Map.Entry<String, List<String>> e: params.entrySet()) {
            jjj.append(e.getKey()).append("=").append(e.getValue().get(0)).append("&");
        }

        System.out.println(jjj);
        URL url = new URL(jjj.toString());
        URLConnection connection = url.openConnection();
        Charset charset = Charset.forName("UTF8");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream(),charset));
        String inputLine;
        StringBuilder builder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        System.out.println(builder.toString());

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonResponse.has("error")) {
            return error(jsonResponse);
        }

        return builder.toString();
    }

    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    if (pair.length < 2) continue;
                    String key = URLEncoder.encode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLEncoder.encode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    public StringBuilder getStringBuilder(String methodName) {
        StringBuilder urlString = new StringBuilder("https://api.vk.com/method/" + methodName + "?");
        urlString.append("&v=5.27&access_token=").append(accessToken);
        return urlString;
    }

    public String error(JSONObject response) {
        if (response == null) return null;
        try {
            JSONObject error = response.getJSONObject("error");
            int error_code = error.getInt("error_code");
            if (error_code == 15) {

            }

            if (error_code == 6) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if (error_code == 14) {
                String captchaImg = error.getString("captcha_img");
                String captcha_sid = error.getString("captcha_sid");
                URL url = new URL(captchaImg);
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();

                File f = new File(captcha_sid);
                boolean newFile = f.createNewFile();

                FileOutputStream o =
                        new FileOutputStream(f);

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = in.read(bytes)) != -1) {
                    o.write(bytes, 0, read);
                }
                System.out.println("/home/igor/" + captcha_sid);

                AntiCaptcha antiCaptcha = new AntiCaptcha();


                String key = antiCaptcha.getCaptcha(f);

                f.delete();
                //{"error":{"error_msg":"Captcha needed","request_params":[{"value":"1","key":"oauth"},{"value":"groups.join","key":"method"},{"value":"51288365","key":"group_id"},{"value":"5.27","key":"v"}],"error_code":14,"captcha_sid":"789967439515","captcha_img":"http://api.vk.com/captcha.php?sid=789967439515&s=1"}}
                //Captch neede

                if (key == null) return null;
                JSONArray array = error.getJSONArray("request_params");
                JSONObject captcha_sid_json = new JSONObject();
                captcha_sid_json.put("key","captcha_sid");
                captcha_sid_json.put("value",captcha_sid);

                array.put(captcha_sid_json);

                JSONObject captcha_key_json = new JSONObject();
                captcha_key_json.put("key","captcha_key");
                captcha_key_json.put("value",key);

                array.put(captcha_key_json);


                return repeatRequest(array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{}";
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
