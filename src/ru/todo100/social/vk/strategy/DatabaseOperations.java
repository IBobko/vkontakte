package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.datas.DatabaseData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Bobko
 */
public class DatabaseOperations extends Operations {
    public DatabaseOperations(String accessToken) {
        super(accessToken);
    }

    public List<DatabaseData> getCountries() {
        try {
            StringBuilder urlString = getStringBuilder("database.getCountries");
            String responseBody = getResponse(urlString.toString());
            JSONObject object = new JSONObject(responseBody);
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<DatabaseData> countries = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                DatabaseData country = new DatabaseData();
                country.setId(items.getJSONObject(i).getInt("id"));
                country.setTitle(items.getJSONObject(i).getString("title"));
                countries.add(country);
            }
            return countries;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DatabaseData> getCities(Integer country_id) {
        try {
            StringBuilder urlString = getStringBuilder("database.getCities");
            if (country_id != null) {
                urlString.append("&country_id=").append(country_id);
            }
            String responseBody = getResponse(urlString.toString());
            JSONObject object = new JSONObject(responseBody);
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<DatabaseData> countries = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                DatabaseData country = new DatabaseData();
                country.setId(items.getJSONObject(i).getInt("id"));
                country.setTitle(items.getJSONObject(i).getString("title"));
                countries.add(country);
            }
            return countries;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
