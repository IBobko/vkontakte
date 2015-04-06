package ru.todo100.social.vk.strategy;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.datas.GroupData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;

/**
 * @author Igor Bobko on 08.02.15.
 * GroupsOperations
 */
public class GroupsOperations extends Operations {
   // private static Logger LOG = Logger.getLogger(GroupsOperations.class);
    @SuppressWarnings("UnusedDeclaration")
    enum Type {
        event,
        group,
        page
    }
    public GroupsOperations(String accessToken) {
        super(accessToken);
    }

    public int join(Long group_id) {

        try {
            final StringBuilder urlString = getStringBuilder("groups.join");
            urlString.append("&group_id").append("=").append(group_id);

            String responseBody = getResponse(urlString.toString());


            JSONObject object = new JSONObject(responseBody);
            System.out.println(object.toString());
            if (object.has("response") && object.getInt("response") == 1) {
                return 1;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public List<GroupData> get() {
        try {
            final StringBuilder urlString = getStringBuilder("groups.get");
            urlString.append("&extended=1");
            urlString.append("&fields=").append(getFields());
            String responseBody = getResponse(urlString.toString());
            System.out.println(responseBody);
            JSONObject object = new JSONObject(responseBody);
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            return getGroups(items);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GroupData> search(String search, Integer offset, Integer count, Integer country_id, Integer city_id,String type) {
        try {
            final StringBuilder urlString = getStringBuilder("groups.search");
            urlString.append("&q=").append(search);
            urlString.append("&fields=").append(getFields());
            if (offset != null) {
                urlString.append("&offset=").append(offset);
            }
            if (count != null) {
                urlString.append("&count=").append(count);
            }

            if (country_id != null) {
                urlString.append("&country_id=").append(country_id);
            }

            if (city_id != null) {
                urlString.append("&city_id=").append(city_id);
            }
         //   LOG.info(urlString.toString());
            final String responseBody = getResponse(urlString.toString());
            JSONObject o = new JSONObject(responseBody);
            JSONObject response = o.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            return getGroups(items);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GroupData> getGroups(JSONArray items) throws JSONException {
        List<GroupData> groups = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            if (items.get(i) instanceof JSONObject) {
                GroupData group = getGroupData(items.getJSONObject(i));
                groups.add(group);
            }
        }
        return groups;
    }

    public GroupData getGroupData(JSONObject json) throws JSONException {
        GroupData group = new GroupData();
        group.setId(json.getLong("id"));
        group.setName(json.getString("name"));
        if (json.has("can_post")) {
            group.setCanPost(json.getInt("can_post"));
        }
        if (json.has("type")) {
            group.setType(json.getString("type"));
        }
        if (json.has("members_count")) {
            group.setMemberCount(json.getInt("members_count"));
        }

        if (json.has("is_closed")) {
            group.setIsClosed(json.getInt("is_closed"));
        }


        return group;
    }

    public void leave(Integer group_id) {
        try {
            StringBuilder urlString = getStringBuilder("groups.leave");
            if (group_id != null) {
                urlString.append("&group_id=").append(group_id);
            }
            getResponse(urlString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFields() {
        String fields = "city, country, place, description, wiki_page, members_count, counters, start_date, finish_date, can_post, can_see_all_posts, activity, status, contacts, links, fixed_post, verified, site, can_create_topic";
        return fields.replace(" ","");
    }
//    groups.isMemberВозвращает информацию о том, является ли пользователь участником сообщества.
//    groups.getByIdВозвращает информацию о заданном сообществе или о нескольких сообществах.
//    groups.getВозвращает список сообществ указанного пользователя.
//    groups.getMembersВозвращает список участников сообщества.


//    groups.getInvitesДанный метод возвращает список приглашений в сообщества и встречи текущего пользователя.
//    groups.getInvitedUsersВозвращает список пользователей, которые были приглашены в группу.
//    groups.banUserДобавляет пользователя в черный список группы.
//    groups.unbanUserУбирает пользователя из черного списка сообщества.
//    groups.getBannedВозвращает список забаненных пользователей в сообществе.
//    groups.createПозволяет создавать новые сообщества.
//    groups.editПозволяет редактировать информацию групп.
//    groups.editPlaceПозволяет редактировать информацию о месте группы.
//    groups.getSettingsПозволяет получать данные, необходимые для отображения страницы редактирования данных сообщества.
//    groups.getRequestsВозвращает список заявок на вступление в сообщество.
//    groups.editManagerПозволяет назначить/разжаловать руководителя в сообществе или изменить уровень его полномочий.
//    groups.inviteПозволяет приглашать друзей в группу.
//    groups.addLinkПозволяет добавлять ссылки в сообщество.
//    groups.deleteLinkПозволяет удалить ссылки из сообщества.
//    groups.editLinkПозволяет редактировать ссылки в сообществе.
//    groups.reorderLinkПозволяет менять местоположение ссылки в списке.
//    groups.removeUserПозволяет исключить пользователя из группы.
//    groups.approveRequestПозволяет одобрить заявку в группу от пользователя.
}
