package ru.todo100.social;

import java.sql.*;

/**
 * @author Igor Bobko
 */
public class Settings {
    private Database database;

    public String getAntiCaptchaKey() {
        try {
            Statement statement = getDatabase().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PARAMS WHERE name='anti_captcha_key'");
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

    public void setAntiCaptchaKey(String key) {
        try {
            Statement statement = getDatabase().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PARAMS WHERE name='anti_captcha_key'");
            if (!resultSet.next()){
                PreparedStatement preparedStatement = getDatabase().getConnection().prepareStatement("INSERT INTO params (name,value) VALUES(?,?)");
                preparedStatement.setString(1,"anti_captcha_key");
                preparedStatement.setString(2,key);
                preparedStatement.execute();
            } else {
                PreparedStatement preparedStatement = getDatabase().getConnection().prepareStatement("UPDATE params SET value = ? WHERE name = ?");
                preparedStatement.setString(1,key);
                preparedStatement.setString(2,"anti_captcha_key");
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
