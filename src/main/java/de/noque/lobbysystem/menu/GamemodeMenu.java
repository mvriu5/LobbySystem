package de.noque.lobbysystem.menu;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.model.ServerDocument;
import de.noque.lobbysystem.utils.BukkitPlayerInventory;
import de.noque.lobbysystem.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class GamemodeMenu extends BukkitPlayerInventory {

    private final List<ServerDocument> _servers;

    public GamemodeMenu(LobbySystem lobbySystem, Component title, int rows) {
        super(title, rows);
        _servers = lobbySystem.getServerService().loadAllFromGameMode(title.toString());
    }

    public void open(Player player) {
        buildServerItems();
        this.openInventory(player);
    }

    private void buildServerItems() {
        for (ServerDocument server : _servers) {
            ItemBuilder item = null;

            switch (server.getState()) {
                case WAITING -> item = new ItemBuilder(Material.GREEN_WOOL);
                case STARTING -> item = new ItemBuilder(Material.ORANGE_WOOL);
                case INGAME -> item = new ItemBuilder(Material.RED_WOOL);
                case RESTARTING -> item = new ItemBuilder(Material.CYAN_WOOL);
                default -> { continue; }
            }
            //item.setAmount(server.getPlayerCount());
            item.setName(Component.text(server.getName(), NamedTextColor.GOLD));

            List<TextComponent> lore = new ArrayList<>();
            lore.add(Component.text(server.getState() + "...", NamedTextColor.YELLOW));
            //lore.add(Component.text(server.getPlayerCount() + "/2", NamedTextColor.YELLOW));
            item.setLore(lore);

            this.addSlot(item.toItemStack(), this::onClick);
        }
    }

    private void onClick(InventoryClickEvent event) {
        //send to selected server
    }
}
