package de.noque.lobbysystem.listener;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.PluginMessage;
import de.noque.lobbysystem.SelectorGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        e.setCancelled(true);

        if (e.getView().title().equals(Component.text("Server Selector", NamedTextColor.GREEN))) {
            if (e.getCurrentItem() == null) return;

            switch (e.getCurrentItem().getType()) {
                case GOLDEN_SHOVEL:
                    player.closeInventory();
                    SelectorGUI.openItemRace(player);
                    break;
                default:
                    break;
            }
        }

        if (e.getView().title().equals(Component.text("ItemRace Servers", NamedTextColor.GREEN))) {
            if (e.getCurrentItem() == null) return;

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                String server = clickedItem.getItemMeta().getDisplayName();
                PluginMessage pluginMessage = new PluginMessage(LobbySystem.INSTANCE);
                pluginMessage.connect(player, server);
            }
        }
    }
}
