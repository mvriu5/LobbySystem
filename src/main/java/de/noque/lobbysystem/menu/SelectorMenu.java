package de.noque.lobbysystem.menu;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.manager.PropertyManager;
import de.noque.lobbysystem.utils.BukkitPlayerInventory;
import de.noque.lobbysystem.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;

public class SelectorMenu extends BukkitPlayerInventory {

    private final LobbySystem _lobbySystem;
    private final PropertyManager _propertyManager;

    public SelectorMenu(LobbySystem lobbySystem) {
        super(Component.text("Server Selector"), 2);
        _lobbySystem = lobbySystem;
        _propertyManager = lobbySystem.getPropertyManager();
    }

    public void open(Player player) {
        addGamemodeItems(player);
        this.openInventory(player);
    }

    private void addGamemodeItems(Player player) {
        HashMap<String, String> gamemodes = _propertyManager.loadAllGamemodes();

        gamemodes.forEach((key, value) -> {
            var item = new ItemBuilder(Material.getMaterial(value));
            item.setName(Component.text(NamedTextColor.GOLD + key));
            this.addSlot(item.toItemStack(), inventoryClickEvent -> onClick(inventoryClickEvent, Component.text(key), player));
        });
    }

    private void onClick(InventoryClickEvent event, Component title, Player player) {
        new GamemodeMenu(_lobbySystem, title, 2).open(player);
    }
}
