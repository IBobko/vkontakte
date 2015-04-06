package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import ru.todo100.social.vk.datas.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Bobko
 */
public class UserOperations extends Operations {
    public UserOperations(String accessToken) {
        super(accessToken);
    }

    public UserData get() {
        return get("").get(0);
    }

    public List<UserData> get(String user_ids) {
        try {
            StringBuilder urlString = getStringBuilder("users.get");
            if (!StringUtils.isEmpty(user_ids)) {
                urlString.append("user_ids=").append(user_ids);
            }
            JSONObject object = new JSONObject(getResponse(urlString.toString()));
            JSONArray response = object.getJSONArray("response");
            List<UserData> users = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                UserData user = new UserData();
                user.setId(response.getJSONObject(i).getInt("id"));
                user.setFirstName(response.getJSONObject(i).getString("first_name"));
                user.setLastName(response.getJSONObject(i).getString("last_name"));
                users.add(user);
            }
            return users;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
