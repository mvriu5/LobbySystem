package de.noque.lobbysystem.listener;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.SelectorGUI;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InteractItemListener implements Listener {

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Material material = player.getInventory().getItemInHand().getType();

        /* HIDE PLAYERS */
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && material == Material.GRAY_DYE) {

            for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(LobbySystem.INSTANCE, onlinePlayers);
            }

            ItemStack show = new ItemStack(Material.LIME_DYE);
            ItemMeta showMeta = show.getItemMeta();
            showMeta.displayName(Component.text("Show Players", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            show.setItemMeta(showMeta);

            player.getInventory().setItem(1, show);
        }

        /* SHOW PLAYERS */
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && material == Material.LIME_DYE) {

            for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                player.showPlayer(LobbySystem.INSTANCE, onlinePlayers);
            }

            ItemStack hide = new ItemStack(Material.GRAY_DYE);
            ItemMeta hideMeta = hide.getItemMeta();
            hideMeta.displayName(Component.text("Hide Players", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            hide.setItemMeta(hideMeta);

            player.getInventory().setItem(1, hide);
        }

        /* SERVER SELECTOR */
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && material == Material.COMPASS) {
            SelectorGUI.open(player);

        }
    }
}
