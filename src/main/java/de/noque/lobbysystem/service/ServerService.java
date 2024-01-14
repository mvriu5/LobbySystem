package de.noque.lobbysystem.service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.manager.MongoManager;
import de.noque.lobbysystem.serverselector.ServerInstance;
import de.noque.lobbysystem.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ServerService {

    private final MongoCollection<Document> _serverCollection;

    public ServerService(LobbySystem lobbySystem) {
        _serverCollection = lobbySystem.getMongoManager().getServerCollection();
    }

    public ServerInstance loadServerFromDB(Document server) {
        String name =  server.getString("servername");
        Document found = _serverCollection.find(Filters.eq("servername", name)).first();
        assert found != null;

        String serverName = (String) found.get("servername");
        String serverState = (String) found.get("serverstate");
        String gameMode = (String) found.get("gamemode");
        int playerCount = (int) found.get("playercount");

        return new ServerInstance(serverName, serverState, gameMode, playerCount);
    }

    public List<ServerInstance> getServerList() {
        List<ServerInstance> serverList = new ArrayList<>();

        FindIterable<Document> findIterable = _serverCollection.find();
        for (Document server : findIterable) {
            ServerInstance serverInstance = loadServerFromDB(server);
            serverList.add(serverInstance);
        }
        return serverList;
    }

    public List<Inventory> getServerInventory() {
        List<Inventory> invList = new ArrayList<>();

        Inventory itemRaceInv = Bukkit.createInventory(null, 9, Component.text("ItemRace Servers", NamedTextColor.GREEN));
        invList.add(itemRaceInv);

        for (ServerInstance server : getServerList()) {
            ItemBuilder item = null;
            String serverDescription = null;

            switch (server.getServerState()) {
                case "waiting" -> {
                    item = new ItemBuilder(Material.GREEN_WOOL);
                    serverDescription = "WAITING...";
                }
                case "starting" -> {
                    item = new ItemBuilder(Material.ORANGE_WOOL);
                    serverDescription = "STARTING...";
                }
                case "ingame" -> {
                    item = new ItemBuilder(Material.RED_WOOL);
                    serverDescription = "INGAME...";
                }
                case "restarting" -> {
                    item = new ItemBuilder(Material.CYAN_WOOL);
                    serverDescription = "RESTARTING...";
                }
                default -> {
                    return null;
                }
            }
            item.setAmount(server.getPlayerCount());
            item.setName(Component.text(server.getServerName(), NamedTextColor.GOLD));

            List<TextComponent> lore = new ArrayList<>();
            lore.add(Component.text(serverDescription, NamedTextColor.YELLOW));
            lore.add(Component.text(server.getPlayerCount() + "/2", NamedTextColor.YELLOW));
            item.setLore(lore);

            switch(server.getGameMode()) {
                case "itemrace" -> itemRaceInv.addItem(item.toItemStack());
            }
        }
        return invList;
    }
}
