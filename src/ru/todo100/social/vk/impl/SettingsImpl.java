package ru.todo100.social.vk.impl;

import ru.todo100.social.Database;
import ru.todo100.social.Settings;

import java.sql.*;

/**
 * @author Igor Bobko
 */
public class SettingsImpl implements Settings {
    private Database database;

    public String getAntiCaptchaKey() {
        return getValue("anti_captcha_key");
    }

    public void setAntiCaptchaKey(String key) {
        setValue("anti_captcha_key",key);
    }

    public String getValue(String key) {
        try {
            PreparedStatement preparedStatement = getDatabase().getConnection().prepareStatement("SELECT * FROM PARAMS WHERE name=?");
            preparedStatement.setString(1,key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                return "";
            } else {
                return resultSet.getString("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setValue(String key,String value) {
        try {
            PreparedStatement preparedStatement = getDatabase().getConnection().prepareStatement("SELECT * FROM PARAMS WHERE name=?");
            preparedStatement.setString(1,key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                preparedStatement = getDatabase().getConnection().prepareStatement("INSERT INTO params (name,value) VALUES(?,?)");
                preparedStatement.setString(1,key);
                preparedStatement.setString(2,value);
                preparedStatement.execute();
            } else {
                preparedStatement = getDatabase().getConnection().prepareStatement("UPDATE params SET value = ? WHERE name = ?");
                preparedStatement.setString(1,value);
                preparedStatement.setString(2,key);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}
