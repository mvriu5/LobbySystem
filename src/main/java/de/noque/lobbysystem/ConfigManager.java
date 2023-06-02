package de.noque.lobbysystem;

import org.bukkit.Location;

public class ConfigManager {

    public static Location getLobbySpawn() {
        return LobbySystem.INSTANCE.getConfig().getLocation("spawn");
    }

    public static void setLobbySpawn(Location location) {
        LobbySystem.config.set("spawn", location);
        LobbySystem.INSTANCE.saveConfig();
    }
}
