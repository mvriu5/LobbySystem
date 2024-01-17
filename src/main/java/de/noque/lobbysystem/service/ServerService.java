package de.noque.lobbysystem.service;

import com.mongodb.client.model.Filters;
import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.model.ServerDocument;
import de.noque.lobbysystem.model.enums.State;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;

import java.util.List;

public class ServerService {

    private final Datastore _datastore;

    public ServerService(LobbySystem lobbySystem) {
        _datastore = lobbySystem.getMongoManager().getDatastore();
    }

    public ServerDocument loadFromName(String name) {
        return _datastore.find(ServerDocument.class)
                .filter((Filter) Filters.eq("name", name)).first();
    }

    public List<ServerDocument> loadAllFromGameMode(String gameMode) {
        return _datastore.find(ServerDocument.class)
                .filter((Filter) Filters.eq("gameMode", gameMode)).stream().toList();
    }

    public List<ServerDocument> loadAll() {
        return _datastore.find(ServerDocument.class).stream().toList();
    }

    public boolean add(String name, String gameMode) {
        if (loadFromName(name) != null) return false;

        var document = new ServerDocument(name, State.WAITING, gameMode);
        _datastore.save(document);
        return true;
    }

    public boolean remove(String name) {
        var document = _datastore.find(ServerDocument.class).filter((Filter) Filters.eq("name", name)).first();

        if (document == null) return false;

        _datastore.delete(document);
        return true;
    }
}
