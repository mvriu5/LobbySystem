package de.noque.lobbysystem.menu;

import de.noque.backend.model.PlayerDocument;
import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.utils.BukkitPlayerInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class FriendMenu extends BukkitPlayerInventory {

    private final List<PlayerDocument> _friends;

    public FriendMenu(LobbySystem lobbySystem, Player player) {
        super(Component.text("FriendMenu", NamedTextColor.GOLD), 5);
        _friends = lobbySystem.getFriendList().get(player);
    }

    public void open(Player player) {
        addFriendHeads();
        this.openInventory(player);
    }

    private void addFriendHeads() {
        for(PlayerDocument friend : _friends) {
            Player friendPlayer = (Player) Bukkit.getOfflinePlayer(friend.getUuid());
            getSkull(friendPlayer);
        }
    }

    private void getSkull(Player player) {
        String status;
        if (player.isOnline()) status = "(Online)";
        else status = "(Offline)";

        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(player.getName() + status);
        skull.setOwner(player.getName());
        item.setItemMeta(skull);
    }
}
