package de.noque.lobbysystem.friendsystem;

import de.noque.lobbysystem.LobbySystem;
import it.unimi.dsi.fastutil.Hash;
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
import java.util.UUID;

public class FriendCommand implements CommandExecutor {

    private final HashMap<UUID, UUID> friendRequests = LobbySystem.getFriendRequests();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length < 1) return false;

        Player player = (Player) sender;
        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("add")) {
            Player target = Bukkit.getPlayerExact(args[1]);

            if (target == null) {
                player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.add.playernotonline"), NamedTextColor.RED));
                return false;
            }
            player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.add.playermessage", target), NamedTextColor.GREEN));
            target.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.add.targetmessage", player), NamedTextColor.GREEN));

            friendRequests.put(player.getUniqueId(), target.getUniqueId());
        }

        if (subCommand.equalsIgnoreCase("remove")) {
            Player target = Bukkit.getPlayerExact(args[1]);

            //check if player is in friendlist

            player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.remove.playermessage", target), NamedTextColor.GREEN));
            //remove from database
        }

        if (subCommand.equalsIgnoreCase("accept")) {
            Player target = Bukkit.getPlayerExact(args[1]);

            if (target == null) {
                player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.accept.playernotonline"), NamedTextColor.RED));
                return false;
            }
            if (friendRequests.get(target.getUniqueId()) != player.getUniqueId()) {
                player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.accept.norequest"), NamedTextColor.RED));
                return false;
            }
            LobbySystem.getFriendData().addFriend(player, target);
            player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.accept.playermessage", target), NamedTextColor.GREEN));
            player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.accept.targetmessage", player), NamedTextColor.GREEN));
        }

        if (subCommand.equalsIgnoreCase("deny")) {
            Player target = Bukkit.getPlayerExact(args[1]);

            if (target == null) {
                player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.deny.playernotonline"), NamedTextColor.RED));
                return false;
            }
            if (friendRequests.get(target.getUniqueId()) != player.getUniqueId()) {
                player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.deny.norequest"), NamedTextColor.RED));
                return false;
            }
            friendRequests.remove(target.getUniqueId(), player.getUniqueId());
            player.sendMessage(Component.text(LobbySystem.INSTANCE.getMessage("friend.deny.success", target), NamedTextColor.GREEN));
        }

        if (subCommand.equalsIgnoreCase("list")) {
            player.sendMessage(Component.text( "Your Friends", NamedTextColor.GOLD));

            List<UUID> friends = LobbySystem.getFriendData().getFriends(player);
            HashMap<UUID, String> friendStatus = new HashMap<>();

            for (UUID uuid : friends) {
                Player friend = (Player) Bukkit.getOfflinePlayer(uuid);

                if (friend.isOnline())
                    player.sendMessage(Component.text(friend.displayName() + " (Online)", NamedTextColor.GREEN));
                else
                    player.sendMessage(Component.text(friend.displayName() + " (Offline)", NamedTextColor.GREEN));
            }
        }

        return false;
    }
}
