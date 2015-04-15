package ru.todo100.social.vk.strategy;

/**
 * Created by igor on 13.04.15.
 */
public class VkontakteApi {
    public String token;

    public VkontakteApi(String apiKey) {
        token = apiKey;
    }

    public GroupsOperations getGroupsOperations() {
        return new GroupsOperations(token);
    }

    public UserOperations getUserOperations() {
        return new UserOperations(token);
    }
}
