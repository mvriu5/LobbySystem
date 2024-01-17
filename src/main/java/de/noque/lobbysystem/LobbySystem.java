package de.noque.lobbysystem;

import de.noque.lobbysystem.command.LobbyCommand;
import de.noque.lobbysystem.command.SetobbyCommand;
import de.noque.lobbysystem.manager.ConfigManager;
import de.noque.lobbysystem.manager.MongoManager;
import de.noque.lobbysystem.manager.PropertyManager;
import de.noque.lobbysystem.listener.ConnectionListener;
import de.noque.lobbysystem.listener.InteractItemListener;
import de.noque.lobbysystem.listener.PreventionListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public final class LobbySystem extends JavaPlugin {

    public static Configuration config;

    private @Getter ConfigManager configManager;
    private @Getter PropertyManager propertyManager;
    private @Getter MongoManager mongoManager;

    @Override
    public void onEnable() {
        mongoManager = new MongoManager(this);
        propertyManager = new PropertyManager();
        configManager = new ConfigManager(this);

        config = this.getConfig();
        saveConfig();
        saveDefaultConfig();

        registerListeners();
        registerCommands();
        initGamerules();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage(this));
    }

    @Override
    public void onDisable() {
        mongoManager.disconnect();
    }



    private void registerCommands() {
        getCommand("setlobby").setExecutor(new SetobbyCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PreventionListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractItemListener(this), this);
    }

    private void initGamerules() {
        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_MOB_SPAWNING, false);
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setTime(6000);
    }
}
