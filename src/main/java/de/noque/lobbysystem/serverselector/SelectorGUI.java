package de.noque.lobbysystem.serverselector;

import de.noque.lobbysystem.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SelectorGUI {

    public static void open(Player player) {
        Inventory selectorInv = Bukkit.createInventory(null, 9, Component.text("Server Selector", NamedTextColor.GREEN));

        ItemBuilder builder = new ItemBuilder(Material.GOLDEN_SHOVEL);
        builder.setName(Component.text("Item Race", NamedTextColor.GOLD));

        List<TextComponent> raceLore = new ArrayList<>();

        raceLore.add(Component.text("Play a 1v1 against a other player", NamedTextColor.YELLOW));
        raceLore.add(Component.text("searching for random items.", NamedTextColor.YELLOW));
        raceLore.add(Component.text("Find all items faster as your opponent!", NamedTextColor.YELLOW));
        builder.setLore(raceLore);

        selectorInv.setItem(0, builder.toItemStack());

        player.openInventory(selectorInv);
    }
}
