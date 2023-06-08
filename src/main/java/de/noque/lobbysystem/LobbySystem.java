package de.noque.lobbysystem;

import com.mongodb.client.MongoCollection;
import de.noque.lobbysystem.command.LobbyCommand;
import de.noque.lobbysystem.command.SetobbyCommand;
import de.noque.lobbysystem.friendsystem.FriendCommand;
import de.noque.lobbysystem.friendsystem.FriendData;
import de.noque.lobbysystem.listener.ConnectionListener;
import de.noque.lobbysystem.listener.InteractItemListener;
import de.noque.lobbysystem.listener.InventoryListener;
import de.noque.lobbysystem.listener.PreventionListener;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.util.*;

public final class LobbySystem extends JavaPlugin {

    public static LobbySystem INSTANCE;

    public static Configuration config;
    private Properties messages;

    //MongoDB
    @Getter
    private MongoManager mongoManager;
    @Getter
    private static MongoCollection<Document> serverCollection, friendCollection;

    @Getter
    private static FriendData friendData;

    @Getter
    private static HashMap<UUID, UUID> friendRequests;

    @Override
    public void onEnable() {
        INSTANCE = this;

        //DATABASE
        mongoManager = new MongoManager();
        mongoManager.connect();
        serverCollection = getMongoManager().getDatabase().getCollection("serverlist");
        friendCollection = getMongoManager().getDatabase().getCollection("friendlist");

        friendData = new FriendData();

        /* CONFIG */
        config = this.getConfig();
        saveConfig();
        saveDefaultConfig();

        loadMessages();

        friendRequests = new HashMap<>();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage(this));

        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new PreventionListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new InteractItemListener(), this);

        getCommand("setlobby").setExecutor(new SetobbyCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("friend").setExecutor(new FriendCommand());

        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_MOB_SPAWNING, false);
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setTime(6000);
    }

    @Override
    public void onDisable() {
        friendRequests.clear();
        mongoManager.disconnect();
    }

    public void loadMessages() {
        messages = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/messages.properties");
            messages.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String key) {
        return messages.getProperty(key);
    }

    public String getMessage(String key, Player player) {
        return messages.getProperty(key).replaceAll("%name%", player.getName());
    }
}
