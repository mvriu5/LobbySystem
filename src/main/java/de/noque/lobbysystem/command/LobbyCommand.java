package de.noque.lobbysystem.command;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.manager.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public LobbyCommand(LobbySystem lobbySystem) {
        configManager = lobbySystem.getConfigManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.teleport(configManager.getLobbySpawn());

        return false;
    }
}
