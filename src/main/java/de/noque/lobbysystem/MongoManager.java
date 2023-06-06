package de.noque.lobbysystem;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

@Getter
public class MongoManager {

    private MongoClient client;
    private MongoDatabase database;

    public void connect() {
        client = new MongoClient("202.61.243.216", 27017);
        database = client.getDatabase("bungee");
        Bukkit.getConsoleSender().sendMessage(Component.text("Connected to MongoDB!", NamedTextColor.GREEN));
    }

    public void disconnect() {
        if (client != null) client.close();
        Bukkit.getConsoleSender().sendMessage(Component.text("Disonnected from MongoDB!", NamedTextColor.RED));
    }
}
