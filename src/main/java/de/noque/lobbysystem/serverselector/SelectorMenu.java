package de.noque.lobbysystem.serverselector;

import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.model.ServerDocument;
import de.noque.lobbysystem.service.ServerService;
import de.noque.lobbysystem.utils.BukkitPlayerInventory;
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

public class SelectorMenu extends BukkitPlayerInventory {

    private final List<ServerDocument> _servers;

    public SelectorMenu(LobbySystem lobbySystem) {
        super(Component.text("Server Selector"), 2);
        _servers = lobbySystem.getServerService().loadAll();
    }

    public void open(Player player) {
        this.openInventory(player);
    }


    public List<Inventory> buildSelctorInventory() {
        List<Inventory> invList = new ArrayList<>();

        Inventory itemRaceInv = Bukkit.createInventory(null, 9, Component.text("ItemRace Servers", NamedTextColor.GREEN));
        invList.add(itemRaceInv);

        for (ServerDocument server : _servers) {
            ItemBuilder item = null;
            String serverDescription = null;

            switch (server.getState()) {
                case WAITING -> {
                    item = new ItemBuilder(Material.GREEN_WOOL);
                    serverDescription = "WAITING...";
                }
                case STARTING -> {
                    item = new ItemBuilder(Material.ORANGE_WOOL);
                    serverDescription = "STARTING...";
                }
                case INGAME -> {
                    item = new ItemBuilder(Material.RED_WOOL);
                    serverDescription = "INGAME...";
                }
                case RESTARTING -> {
                    item = new ItemBuilder(Material.CYAN_WOOL);
                    serverDescription = "RESTARTING...";
                }
                default -> {
                    return null;
                }
            }
            //item.setAmount(server.getPlayerCount());
            item.setName(Component.text(server.getName(), NamedTextColor.GOLD));

            List<TextComponent> lore = new ArrayList<>();
            lore.add(Component.text(serverDescription, NamedTextColor.YELLOW));
            //lore.add(Component.text(server.getPlayerCount() + "/2", NamedTextColor.YELLOW));
            item.setLore(lore);

            switch(server.getGameMode()) {
                case "itemrace" -> itemRaceInv.addItem(item.toItemStack());
            }
        }
        return invList;
    }
}
