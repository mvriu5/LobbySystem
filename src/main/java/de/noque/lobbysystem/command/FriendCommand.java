package de.noque.lobbysystem.command;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.manager.PropertyManager;
import de.noque.lobbysystem.service.FriendRequestService;
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

    private final FriendService _friendService;
    private final PropertyManager _propertyManager;
    private final FriendRequestService _friendRequestService;


    public FriendCommand(LobbySystem lobbySystem) {
        _friendService = lobbySystem.getFriendService();
        _propertyManager = lobbySystem.getPropertyManager();
        _friendRequestService = lobbySystem.getFriendRequestService();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length < 1) return false;

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
        var target = Bukkit.getOfflinePlayer(args[1]);

        player.sendMessage(Component.text(_propertyManager.getMessage("friend.add.playermessage", target), NamedTextColor.GREEN));
        target.sendMessage(Component.text(_propertyManager.getMessage("friend.add.targetmessage", player), NamedTextColor.GREEN));

        _friendRequests.put(player.getUniqueId(), target.getUniqueId());
        return true;
    }

    private boolean remove(String[] args, Player player) {
        Player target = Bukkit.getPlayerExact(args[1]);

        //check if player is in friendlist

        player.sendMessage(Component.text(_propertyManager.getMessage("friend.remove.playermessage", target), NamedTextColor.GREEN));
        //remove from database

        return false;
    }

    private boolean accept(String[] args, Player player) {
        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            player.sendMessage(Component.text(_propertyManager.getMessage("friend.accept.playernotonline"), NamedTextColor.RED));
            return false;
        }

        if (_friendRequests.get(target.getUniqueId()) != player.getUniqueId()) {
            player.sendMessage(Component.text(_propertyManager.getMessage("friend.accept.norequest"), NamedTextColor.RED));
            return false;
        }

        _friendService.addFriend(player, target);
        player.sendMessage(Component.text(_propertyManager.getMessage("friend.accept.playermessage", target), NamedTextColor.GREEN));
        player.sendMessage(Component.text(_propertyManager.getMessage("friend.accept.targetmessage", player), NamedTextColor.GREEN));
        return true;
    }

    private boolean deny(String[] args, Player player) {
        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            player.sendMessage(Component.text(_propertyManager.getMessage("friend.deny.playernotonline"), NamedTextColor.RED));
            return false;
        }

        if (_friendRequests.get(target.getUniqueId()) != player.getUniqueId()) {
            player.sendMessage(Component.text(_propertyManager.getMessage("friend.deny.norequest"), NamedTextColor.RED));
            return false;
        }

        _friendRequests.remove(target.getUniqueId(), player.getUniqueId());
        player.sendMessage(Component.text(_propertyManager.getMessage("friend.deny.success", target), NamedTextColor.GREEN));
        return true;
    }

    private boolean list(String[] args, Player player) {
        player.sendMessage(Component.text( "Your Friends", NamedTextColor.GOLD));

        List<UUID> friends = _friendService.getFriends(player);

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
