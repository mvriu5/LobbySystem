package de.noque.lobbysystem.listener;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.menu.FriendMenu;
import de.noque.lobbysystem.menu.SelectorMenu;
import de.noque.lobbysystem.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractItemListener implements Listener {

    private final LobbySystem _lobbySystem;

    public InteractItemListener(LobbySystem lobbySystem) {
        _lobbySystem = lobbySystem;
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Material material = player.getInventory().getItemInHand().getType();

        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && material == Material.GRAY_DYE)
            hidePlayers(player);

        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && material == Material.LIME_DYE)
            showPlayers(player);

        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && material == Material.COMPASS)
            new SelectorMenu(_lobbySystem).open(player);

        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && material == Material.CHEST)
            new FriendMenu(_lobbySystem, player).open(player);
    }

    private void hidePlayers(Player player) {
        Bukkit.getOnlinePlayers().forEach(target ->
                player.hidePlayer(_lobbySystem, target)
        );

        ItemBuilder show = new ItemBuilder(Material.LIME_DYE);
        show.setName(Component.text("Show Players", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
        player.getInventory().setItem(1, show.toItemStack());
    }

    private void showPlayers(Player player) {
        Bukkit.getOnlinePlayers().forEach(target ->
                player.showPlayer(_lobbySystem, target)
        );

        ItemBuilder hide = new ItemBuilder(Material.GRAY_DYE);
        hide.setName(Component.text("Hide Players", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
        player.getInventory().setItem(1, hide.toItemStack());
    }
}
