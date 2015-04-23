package ru.todo100.social;

import ru.todo100.social.Database;

/**
 * @author Igor Bobko
 */
public interface Settings {
    public String getAntiCaptchaKey();

    public void setAntiCaptchaKey(String key);

    public String getValue(String key);

    public void setValue(String key, String value);

    public Database getDatabase();

    public void setDatabase(Database database);
}
