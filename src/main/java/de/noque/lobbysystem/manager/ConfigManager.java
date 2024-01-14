package de.noque.lobbysystem.manager;

import de.noque.lobbysystem.LobbySystem;
import org.bukkit.Location;

public class ConfigManager {

    private final LobbySystem _lobbySystem;

    public ConfigManager(LobbySystem lobbySystem) {
        _lobbySystem = lobbySystem;
    }

    public Location getLobbySpawn() {
        return _lobbySystem.getConfig().getLocation("spawn");
    }

    public void setLobbySpawn(Location location) {
        _lobbySystem.getConfig().set("spawn", location);
        _lobbySystem.saveConfig();
    }
}
