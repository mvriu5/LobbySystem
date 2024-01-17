package de.noque.lobbysystem.manager;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.util.*;

public class PropertyManager {

    private @Getter Properties messages;
    private @Getter Properties dbconn;
    private @Getter Properties gamemodes;

    public PropertyManager() {
        loadMessages();
        loadDbConn();
        loadGamemodes();
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

    private void loadGamemodes() {
        gamemodes = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/gamemodes.properties");
            gamemodes.load(inputStream);
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


    public String getGamemodeItem(String key) {
        return gamemodes.getProperty(key);
    }

    public void setGamemodeItem(String key, String value) {
        gamemodes.setProperty(key, value);
    }

    public HashMap<String, String> loadAllGamemodes() {
        var gamemodeMap = new HashMap<String, String>();

        Enumeration<?> enumeration = gamemodes.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = gamemodes.getProperty(key);
            gamemodeMap.put(key, value);
        }
        return gamemodeMap;
    }

}
