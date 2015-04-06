package ru.todo100.social;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Igor Bobko
 */
public class Install {
    private Database database;

    public void paramsInstall() {
        try {
            Statement statement = getDatabase().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES");

            boolean params_table = false;

            while (resultSet.next()) {
                if (resultSet.getString(3).equalsIgnoreCase("params")) {
                    params_table = true;
                    break;
                }

            }
            if (!params_table) {
                statement.execute("CREATE TABLE params (name varchar(255),value varchar(255))");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        logsInstall();
    }

    public void run() {
        paramsInstall();
        logsInstall();
    }

    public void logsInstall() {
        try {
            Statement statement = getDatabase().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES");

            boolean logs_table = false;

            while (resultSet.next()) {
                if (resultSet.getString(3).equalsIgnoreCase("logs")) {
                    logs_table = true;
                    break;
                }

            }
            if (!logs_table) {
                statement.execute("CREATE TABLE logs (" +
                        "id INTEGER ," +
                        "post_id INTEGER ," +
                        "group_id INTEGER," +
                        "group_name VARCHAR(255)," +
                        "group_type VARCHAR (10)," +
                        "message VARCHAR (500)," +
                        "attachment VARCHAR (500)," +
                        "created TIMESTAMP)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
