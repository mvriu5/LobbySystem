package de.noque.lobbysystem.manager;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.noque.lobbysystem.LobbySystem;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.Bukkit;

public class MongoManager {

    private final MongoClient client;
    private final @Getter Datastore datastore;

    @Getter
    private MongoCollection<Document> serverCollection, friendCollection;

    public MongoManager(LobbySystem lobbySystem) {
        var propertyManager = lobbySystem.getPropertyManager();

        client = new MongoClient(propertyManager.getDbProperty("ip"),  Integer.parseInt(propertyManager.getDbProperty("port")));
        MongoDatabase _database = client.getDatabase(propertyManager.getDbProperty("db_name"));

        serverCollection = _database.getCollection(propertyManager.getDbProperty("servers"));
        friendCollection = _database.getCollection(propertyManager.getDbProperty("friends"));

        datastore = Morphia.createDatastore(MongoClients.create(""), "");
        datastore.getMapper().mapPackage("com.mongodb.morphia.entities");
        datastore.ensureIndexes();

        Bukkit.getConsoleSender().sendMessage(Component.text("Connected to MongoDB!", NamedTextColor.GREEN));
    }

    public void disconnect() {
        if (client != null)
            client.close();

        Bukkit.getConsoleSender().sendMessage(Component.text("Disonnected from MongoDB!", NamedTextColor.RED));
    }
}
