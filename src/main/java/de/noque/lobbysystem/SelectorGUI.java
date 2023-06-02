package de.noque.lobbysystem;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SelectorGUI {

    @Getter
    private static Inventory selectorInv;

    public static void open(Player player) {
        selectorInv = Bukkit.createInventory(null, 9, Component.text("Server Selector", NamedTextColor.GREEN));

        ItemStack itemRace = new ItemStack(Material.GOLDEN_SHOVEL);
        ItemMeta raceMeta = itemRace.getItemMeta();
        raceMeta.displayName(Component.text("Item Race", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        List<Component> raceLore = new ArrayList<>();
        raceLore.add(Component.text("Play a 1v1 against a other player", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        raceLore.add(Component.text("searching for random items.", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        raceLore.add(Component.text("Find all items faster as your opponent!", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        raceMeta.lore(raceLore);
        itemRace.setItemMeta(raceMeta);

        selectorInv.setItem(0, itemRace);

        player.openInventory(selectorInv);
    }

    public static void openItemRace(Player player) {
        PluginMessage pluginMessage = new PluginMessage(LobbySystem.INSTANCE);

        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.INSTANCE, () -> {
            pluginMessage.registerChannel(player);

            Bukkit.getScheduler().runTaskLater(LobbySystem.INSTANCE, () -> {
                List<String> serverList = pluginMessage.getServerList();

                Inventory inv = Bukkit.createInventory(null, 9, Component.text("ItemRace Servers", NamedTextColor.GREEN));

                for (String server : serverList) {
                    ItemStack item = createServerItem(server);
                    inv.addItem(item);
                }
                player.openInventory(inv);
            }, 20);
        });
    }

    private static ItemStack createServerItem(String serverName) {
        ItemStack item = new ItemStack(Material.GREEN_WOOL);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(serverName, NamedTextColor.GOLD));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Players: " + Bukkit.getOnlinePlayers(), NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        meta.lore(lore);

        item.setItemMeta(meta);

        return item;
    }
}
