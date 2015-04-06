package ru.todo100.social;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Igor Bobko
 */
public class Database {
    private Connection connection;
    public Database() {
        try {

            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:db/vk;shutdown=true");//, "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
