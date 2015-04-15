package ru.todo100.social.vk.strategy.impl;

import org.springframework.util.StringUtils;
import ru.todo100.social.vk.strategy.VkontakteApi;
import ru.todo100.social.vk.strategy.VkontakteFactory;

import java.util.HashMap;

/**
 * @author Igor Bobko
 */
public class VkontakteFactoryApi implements VkontakteFactory {
    private HashMap<String, VkontakteApi> vkontakteApiHashMap = new HashMap<>();
    private String defaultVkontakteApiKey;

    public HashMap<String, VkontakteApi> getVkontakteApiHashMap() {
        return vkontakteApiHashMap;
    }

    public String getDefaultVkontakteApiKey() {
        return defaultVkontakteApiKey;
    }

    public void setDefaultVkontakteApiKey(String defaultVkontakteApiKey) {
        this.defaultVkontakteApiKey = defaultVkontakteApiKey;
    }

    @Override
    public VkontakteApi getDefaultVkontakteApi() {
        if (!StringUtils.isEmpty(getDefaultVkontakteApiKey())) {
            if (getVkontakteApiHashMap().containsKey(getDefaultVkontakteApiKey())) {
                return getVkontakteApiHashMap().get(getDefaultVkontakteApiKey());
            }
        }
        return null;
    }

    @Override
    public void addVkontakteApi(String login, String token) {
        if (!getVkontakteApiHashMap().containsKey(token)) {
            getVkontakteApiHashMap().put(login, new VkontakteApi(token));
            setDefaultVkontakteApiKey(login);
        }
    }
}
