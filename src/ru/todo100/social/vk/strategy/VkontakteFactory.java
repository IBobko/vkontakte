package ru.todo100.social.vk.strategy;


public interface VkontakteFactory {
    VkontakteApi getDefaultVkontakteApi();
    void addVkontakteApi(String login,String token);
}
