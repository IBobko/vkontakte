package ru.todo100.social.vk.strategy;

/**
 * @author Igor Bobko
 */
public class VkontakteApi {
    private GroupsOperations groupsOperations;
    private UserOperations userOperations;

    public String token;

    public VkontakteApi(String apiKey) {
        token = apiKey;
        groupsOperations = new GroupsOperations(token);
        userOperations = new UserOperations(token);
    }

    public GroupsOperations getGroupsOperations() {
        return groupsOperations;
    }

    public UserOperations getUserOperations() {
        return userOperations;
    }


}
