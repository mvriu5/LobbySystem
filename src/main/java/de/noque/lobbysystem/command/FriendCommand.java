package de.noque.lobbysystem.command;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.manager.PropertyManager;
import de.noque.lobbysystem.service.FriendService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class FriendCommand implements CommandExecutor {

    private final HashMap<UUID, UUID> friendRequests = LobbySystem.getFriendRequests();

    private final LobbySystem _lobbySystem;
    private final FriendService friendService;
    private final PropertyManager propertyManager;


    public FriendCommand(LobbySystem lobbySystem) {
        _lobbySystem = lobbySystem;
        friendService = lobbySystem.getFriendService();
        propertyManager = lobbySystem.getPropertyManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length < 1) return false;

        Player player = (Player) sender;
        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("add"))
            return add(args, player);

        if (subCommand.equalsIgnoreCase("remove"))
            return remove(args, player);

        if (subCommand.equalsIgnoreCase("accept"))
            return accept(args, player);

        if (subCommand.equalsIgnoreCase("deny"))
            return deny(args, player);

        if (subCommand.equalsIgnoreCase("list"))
            return list(args, player);

        return false;
    }

    private boolean add(String[] args, Player player) {
        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            player.sendMessage(Component.text(propertyManager.getMessage("friend.add.playernotonline"), NamedTextColor.RED));
            return false;
        }

        player.sendMessage(Component.text(propertyManager.getMessage("friend.add.playermessage", target), NamedTextColor.GREEN));
        target.sendMessage(Component.text(propertyManager.getMessage("friend.add.targetmessage", player), NamedTextColor.GREEN));

        friendRequests.put(player.getUniqueId(), target.getUniqueId());
        return true;
    }

    private boolean remove(String[] args, Player player) {
        Player target = Bukkit.getPlayerExact(args[1]);

        //check if player is in friendlist

        player.sendMessage(Component.text(propertyManager.getMessage("friend.remove.playermessage", target), NamedTextColor.GREEN));
        //remove from database

        return false;
    }

    private boolean accept(String[] args, Player player) {
        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            player.sendMessage(Component.text(propertyManager.getMessage("friend.accept.playernotonline"), NamedTextColor.RED));
            return false;
        }

        if (friendRequests.get(target.getUniqueId()) != player.getUniqueId()) {
            player.sendMessage(Component.text(propertyManager.getMessage("friend.accept.norequest"), NamedTextColor.RED));
            return false;
        }

        friendService.addFriend(player, target);
        player.sendMessage(Component.text(propertyManager.getMessage("friend.accept.playermessage", target), NamedTextColor.GREEN));
        player.sendMessage(Component.text(propertyManager.getMessage("friend.accept.targetmessage", player), NamedTextColor.GREEN));
        return true;
    }

    private boolean deny(String[] args, Player player) {
        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            player.sendMessage(Component.text(propertyManager.getMessage("friend.deny.playernotonline"), NamedTextColor.RED));
            return false;
        }

        if (friendRequests.get(target.getUniqueId()) != player.getUniqueId()) {
            player.sendMessage(Component.text(propertyManager.getMessage("friend.deny.norequest"), NamedTextColor.RED));
            return false;
        }

        friendRequests.remove(target.getUniqueId(), player.getUniqueId());
        player.sendMessage(Component.text(propertyManager.getMessage("friend.deny.success", target), NamedTextColor.GREEN));
        return true;
    }

    private boolean list(String[] args, Player player) {
        player.sendMessage(Component.text( "Your Friends", NamedTextColor.GOLD));

        List<UUID> friends = friendService.getFriends(player);

        for (UUID uuid : friends) {
            Player friend = (Player) Bukkit.getOfflinePlayer(uuid);

            if (friend.isOnline())
                player.sendMessage(Component.text(friend.displayName() + " (Online)", NamedTextColor.GREEN));
            else
                player.sendMessage(Component.text(friend.displayName() + " (Offline)", NamedTextColor.GREEN));
        }
        return true;
    }
}
