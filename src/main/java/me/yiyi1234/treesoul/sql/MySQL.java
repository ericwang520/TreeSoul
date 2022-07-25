package me.yiyi1234.treesoul.sql;

import me.yiyi1234.treesoul.TreeSoul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private String host = TreeSoul.getInstance().getConfig().getString("MySQL.host");
    private String port = TreeSoul.getInstance().getConfig().getString("MySQL.port");
    private String database = TreeSoul.getInstance().getConfig().getString("MySQL.database");
    private String username = TreeSoul.getInstance().getConfig().getString("MySQL.username");
    private String password = TreeSoul.getInstance().getConfig().getString("MySQL.password");

    private Connection connection;


    public boolean isConnected() {
        return  (connection == null ? false : true);

    }

    public void connect() throws ClassCastException, SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" +
                            host + ":" + port + "/" + database + "?useSSL=false",
                    username, password);
        }
    }


    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
