package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.datas.PostData;

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

    public Integer post(Integer owner_id, Integer friends_only, Integer from_group, String message, String attachments) {
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
            return object.getJSONObject("response").getInt("post_id");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
