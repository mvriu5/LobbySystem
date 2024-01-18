package de.noque.lobbysystem;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerDocument;
import de.noque.backend.service.FriendService;
import de.noque.backend.service.ServerService;
import de.noque.lobbysystem.command.LobbyCommand;
import de.noque.lobbysystem.command.SetobbyCommand;
import de.noque.lobbysystem.listener.InteractItemListener;
import de.noque.lobbysystem.listener.JoinListener;
import de.noque.lobbysystem.listener.LeaveListener;
import de.noque.lobbysystem.listener.PreventionListener;
import de.noque.lobbysystem.manager.ConfigManager;
import de.noque.lobbysystem.manager.PropertyManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public final class LobbySystem extends JavaPlugin {

    public static Configuration config;

    private @Getter ConfigManager configManager;
    private @Getter PropertyManager propertyManager;

    private @Getter Network network;
    private @Getter FriendService friendService;
    private @Getter ServerService serverService;

    private @Getter HashMap<Player, List<PlayerDocument>> friendList;

    @Override
    public void onEnable() {
        propertyManager = new PropertyManager();
        configManager = new ConfigManager(this);

        network = new Network();
        friendService = new FriendService(network);
        serverService = new ServerService(network);

        friendList = new HashMap<>();

        config = this.getConfig();
        saveConfig();
        saveDefaultConfig();

        registerListeners();
        registerCommands();
        initGamerules();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        getCommand("setlobby").setExecutor(new SetobbyCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
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
