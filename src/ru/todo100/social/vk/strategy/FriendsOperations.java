package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 19.04.15.
 */
public class FriendsOperations extends Operations {
    public FriendsOperations(String accessToken) {
        super(accessToken);
    }

    public List<Integer> get(Integer user_id) {
        try {
            final StringBuilder urlString = getStringBuilder("friends.get");
            urlString.append("&user_id=").append(user_id);
            String responseBody = getResponse(urlString.toString());
            System.out.println(responseBody);
            JSONObject object = new JSONObject(responseBody);
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<Integer> users = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                users.add(items.getInt(i));

            }
            return users;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
