package ru.todo100.social;

import java.sql.*;

/**
 * Created by igor on 01.04.15.
 */
public class Settings {
    public String getAntiCaptchaKey() {
        try {
            Connection c = DriverManager.getConnection("jdbc:hsqldb:file:db", "SA", "");
            Statement statement = c.createStatement();
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
            Connection c = DriverManager.getConnection("jdbc:hsqldb:file:db", "SA", "");
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PARAMS WHERE name='anti_captcha_key'");
            if (!resultSet.next()){
                statement.execute("INSERT INTO params (name,value) VALUES('anti_captcha_key','"+key+"')");
            } else {
                statement.execute("UPDATE params SET value = ''"+key+"' WHERE name='anti_captcha_key'");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
