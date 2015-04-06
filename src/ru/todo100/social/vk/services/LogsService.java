package ru.todo100.social.vk.services;

import ru.todo100.social.Database;
import ru.todo100.social.vk.datas.LogData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Igor Bobko
 */

public class LogsService {
    private Database database;
    public Database getDatabase() {
        return database;
    }
    public void setDatabase(Database database) {
        this.database = database;
    }
    public boolean checkLogs(LogData logData) {
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("SELECT * from logs WHERE group_id=? and message =? and attachment=?");
            statement.setLong(1, logData.getGroupID());
            statement.setString(2, logData.getMessage());
            statement.setString(3, logData.getAttachment());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertLogs(LogData logData) {
        try {
            Calendar cal = new GregorianCalendar();
            java.sql.Timestamp created =new java.sql.Timestamp(cal.getTimeInMillis());
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("INSERT INTO logs (group_id,group_type,group_name,post_id,message,attachment,created) VALUES(?,?,?,?,?,?,?)");
            statement.setInt(1, logData.getGroupID());
            statement.setString(2, logData.getGroupType());
            statement.setString(3, logData.getGroupName());
            statement.setInt(4, logData.getPostID());
            statement.setString(5, logData.getMessage());
            statement.setString(6, logData.getAttachment());
            statement.setTimestamp(7,created);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LogData> getLogs() {
        return getLogs(100);
    }

    public List<LogData> getLogs(Integer count) {
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("SELECT * FROM logs ORDER BY created ASC LIMIT " + count);
            ResultSet rs = statement.executeQuery();
            ArrayList<LogData> a =  new ArrayList<>();
            while(rs.next()) {
                LogData logData = new LogData();
                logData.setGroupID(rs.getInt("group_id"));
                logData.setMessage(rs.getString("message"));
                logData.setAttachment(rs.getString("attachment"));
                logData.setGroupName(rs.getString("group_name"));
                logData.setGroupType(rs.getString("group_type"));
                logData.setPostID(rs.getInt("post_id"));
                Calendar created = new GregorianCalendar();
                created.setTimeInMillis(rs.getTimestamp("created").getTime());
                logData.setCreated(created);
                a.add(logData);
            }
            return a;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}