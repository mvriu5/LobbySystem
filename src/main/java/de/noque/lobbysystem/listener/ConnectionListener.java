package de.noque.lobbysystem.listener;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.service.FriendService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bson.Document;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class ConnectionListener implements Listener {

    private final LobbySystem _lobbySystem;
    private final MongoCollection<Document> _friendCollection;
    private final FriendService _friendService;
    private final Location _spawn;
    private final HashMap<UUID, UUID> _friendRequests;

    public ConnectionListener(LobbySystem lobbySystem) {
        _lobbySystem = lobbySystem;
        _friendCollection = _lobbySystem.getMongoManager().getFriendCollection();
        _friendService = lobbySystem.getFriendService();
        _spawn = _lobbySystem.getConfigManager().getLobbySpawn();
        _friendRequests = _lobbySystem.getFriendRequests();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        if (_friendCollection.find(Filters.eq("uuid", player.getUniqueId())).first() == null) {
            _friendService.addDocument(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        setSpawnItems(player);

        player.teleport(_spawn);
        player.setCompassTarget(_spawn);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setExp(0);

        e.joinMessage(Component.text(""));

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(_lobbySystem, () -> {
            player.sendActionBar(Component.text("+ ", NamedTextColor.GOLD)
                    .append(Component.text("New ", NamedTextColor.YELLOW))
                    .append(Component.text("ItemRACE ", NamedTextColor.GOLD))
                    .append(Component.text("Update", NamedTextColor.YELLOW))
                    .decoration(TextDecoration.ITALIC, false));
        }, 0L, 20L);

        player.showTitle(Title.title(
                Component.text("Welcome to", NamedTextColor.LIGHT_PURPLE),
                Component.text("noque.club", NamedTextColor.YELLOW)));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.quitMessage(Component.text(""));
        _friendRequests.remove(player.getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        e.leaveMessage(Component.text(""));
        _friendRequests.remove(player.getUniqueId());
    }

    private void setSpawnItems(Player player) {
        ItemStack hide = new ItemStack(Material.GRAY_DYE);
        ItemMeta hideMeta = hide.getItemMeta();
        hideMeta.displayName(Component.text("Hide Players", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        hide.setItemMeta(hideMeta);

        ItemStack selector = new ItemStack(Material.COMPASS);
        ItemMeta selcetorMeta = selector.getItemMeta();
        selcetorMeta.displayName(Component.text("Server Selector", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        selector.setItemMeta(selcetorMeta);

        ItemStack settings = new ItemStack(Material.NAME_TAG);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.displayName(Component.text("Settings", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        settings.setItemMeta(settingsMeta);

        player.getInventory().clear();
        player.getInventory().setItem(1, hide);
        player.getInventory().setItem(4, selector);
        player.getInventory().setItem(7, settings);
    }
}
