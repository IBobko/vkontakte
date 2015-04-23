package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by igor on 19.04.15.
 */
public class LikesOperations extends Operations {
    public LikesOperations(String accessToken) {
        super(accessToken);
    }

    public String getList(String type,Integer owner_id,Integer item_id) {
        try {
            final StringBuilder urlString = getStringBuilder("likes.getList");
            urlString.append("&type=").append(type);
            urlString.append("&owner_id=").append(owner_id);
            urlString.append("&item_id=").append(item_id);
            String responseBody = getResponse(urlString.toString());
            System.out.println(responseBody);

            JSONObject object = new JSONObject(responseBody);
            return responseBody;
//            JSONObject response = object.getJSONObject("response");
//            JSONArray items = response.getJSONArray("items");
//            return getGroups(items);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
