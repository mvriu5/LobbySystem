package de.noque.lobbysystem.serverselector;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
}
