package ru.todo100.social;

import ru.todo100.social.vk.datas.LogData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public boolean checkLogs(Long group_id, String message, String attachment) {
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("Select * from logs WHERE group_id=? and message =? and atachment=?");
            statement.setLong(1, group_id);
            statement.setString(2, message);
            statement.setString(3, attachment);
            ResultSet rs = statement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertLogs(Long group_id, String message, String attachment) {
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("INSERT INTO logs (group_id,message,atachment) VALUES(?,?,?)");
            statement.setLong(1, group_id);
            statement.setString(2, message);
            statement.setString(3, attachment);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LogData> getLogs() {
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("SELECT * FROM logs");
            ResultSet rs = statement.executeQuery();
            ArrayList<LogData> a =  new ArrayList<>();
            while(rs.next()) {
                LogData logData = new LogData();
                logData.setGroupID(rs.getInt("group_id"));
                logData.setMessage(rs.getString("message"));
                logData.setAttachment(rs.getString("atachment"));
                a.add(logData);
            }
            return a;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}