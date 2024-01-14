package de.noque.lobbysystem.command;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetobbyCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public SetobbyCommand(LobbySystem lobbySystem) {
        configManager = lobbySystem.getConfigManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (player.isOp()) {
            configManager.setLobbySpawn(player.getLocation());
            player.sendMessage(Component.text("You set the lobby spawn", NamedTextColor.YELLOW));
            return true;
        }

        return false;
    }
}
