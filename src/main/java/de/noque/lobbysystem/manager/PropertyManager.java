package de.noque.lobbysystem.manager;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

    private @Getter Properties messages;
    private @Getter Properties dbconn;


    public PropertyManager() {
        loadMessages();
        loadDbConn();
    }

    private void loadMessages() {
        messages = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/messages.properties");
            messages.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDbConn() {
        dbconn = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/dbconn.properties");
            dbconn.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String key) {
        return messages.getProperty(key);
    }

    public String getMessage(String key, Player player) {
        return messages.getProperty(key).replaceAll("%player%", player.getName());
    }

    public String getMessage(String key, Player player, Player target) {
        return messages.getProperty(key).replaceAll("%player%", player.getName()).replaceAll("%target%", target.getName());
    }

    public String getDbProperty(String key) {
        return dbconn.getProperty(key);
    }
}
