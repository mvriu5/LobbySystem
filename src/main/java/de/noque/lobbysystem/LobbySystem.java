package de.noque.lobbysystem;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.noque.lobbysystem.command.LobbyCommand;
import de.noque.lobbysystem.command.SetobbyCommand;
import de.noque.lobbysystem.listener.ConnectionListener;
import de.noque.lobbysystem.listener.InteractItemListener;
import de.noque.lobbysystem.listener.InventoryListener;
import de.noque.lobbysystem.listener.PreventionListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public final class LobbySystem extends JavaPlugin {

    public static LobbySystem INSTANCE;
    public static Configuration config;

    @Override
    public void onEnable() {
        INSTANCE = this;

        /* CONFIG */
        config = this.getConfig();
        saveConfig();
        saveDefaultConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage(this));

        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new PreventionListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new InteractItemListener(), this);

        getCommand("setlobby").setExecutor(new SetobbyCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());

        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_MOB_SPAWNING, false);
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setTime(6000);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
}
