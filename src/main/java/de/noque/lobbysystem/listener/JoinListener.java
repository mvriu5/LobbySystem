package de.noque.lobbysystem.listener;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import static org.bukkit.Bukkit.getServer;

public class JoinListener implements Listener {

    private final LobbySystem _lobbySystem;

    public JoinListener(LobbySystem lobbySystem) {
        _lobbySystem = lobbySystem;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Location spawn = _lobbySystem.getConfigManager().getLobbySpawn();

        setSpawnItems(player);

        player.teleport(spawn);
        player.setCompassTarget(spawn);
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

    private void setSpawnItems(Player player) {
        ItemBuilder hide = new ItemBuilder(Material.GRAY_DYE);
        hide.setName(Component.text("Hide Players", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        ItemBuilder selector = new ItemBuilder(Material.COMPASS);
        selector.setName(Component.text("Server Selector", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        ItemBuilder settings = new ItemBuilder(Material.NAME_TAG);
        selector.setName(Component.text("Settings", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        ItemBuilder friends = new ItemBuilder(Material.CHEST);
        friends.setName(Component.text("Friends", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        player.getInventory().clear();
        player.getInventory().setItem(1, hide.toItemStack());
        player.getInventory().setItem(3, selector.toItemStack());
        player.getInventory().setItem(5, settings.toItemStack());
        player.getInventory().setItem(7, friends.toItemStack());
    }
}
