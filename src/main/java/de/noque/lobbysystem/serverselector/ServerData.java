package de.noque.lobbysystem.serverselector;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import de.noque.lobbysystem.LobbySystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ServerData {

    public static ServerInstance loadServerFromDB(Document server) {
        Document found = LobbySystem.getServerCollection().find(Filters.eq("servername", server)).first();
        assert found != null;

        String serverName = (String) found.get("servername");
        String serverState = (String) found.get("serverstate");
        String gameMode = (String) found.get("gamemode");
        int playerCount = (int) found.get("playercount");

        return new ServerInstance(serverName, serverState, gameMode, playerCount);
    }

    public static List<ServerInstance> getServerList() {
        List<ServerInstance> serverList = new ArrayList<>();

        FindIterable<Document> findIterable = LobbySystem.getServerCollection().find();
        for (Document server : findIterable) {
            ServerInstance serverInstance = loadServerFromDB(server);
            serverList.add(serverInstance);
        }
        return serverList;
    }

    public static List<Inventory> getServerInventory() {
        List<Inventory> invList = new ArrayList<>();

        Inventory itemRaceInv = Bukkit.createInventory(null, 9, Component.text("ItemRace Servers", NamedTextColor.GREEN));
        invList.add(itemRaceInv);

        for (ServerInstance server : getServerList()) {
            ItemStack item = new ItemStack(Material.BARRIER);
            String serverDescription = null;

            switch (server.getServerState()) {
                case "waiting" -> {
                    item.setType(Material.GREEN_WOOL);
                    serverDescription = "WAITING...";
                }
                case "starting" -> {
                    item.setType(Material.ORANGE_WOOL);
                    serverDescription = "STARTING...";
                }
                case "ingame" -> {
                    item.setType(Material.RED_WOOL);
                    serverDescription = "INGAME...";
                }
                case "restarting" -> {
                    item.setType(Material.CYAN_WOOL);
                    serverDescription = "RESTARTING...";
                }
            }
            item.setAmount(server.getPlayerCount());

            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(server.getServerName(), NamedTextColor.GOLD));

            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(serverDescription, NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            meta.lore(lore);

            item.setItemMeta(meta);

            switch(server.getGameMode()) {
                case "itemrace" -> itemRaceInv.addItem(item);
            }
        }
        return invList;
    }
}
