package de.noque.lobbysystem.service;

import com.mongodb.client.model.Filters;
import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.model.FriendRequestDocument;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;

import java.util.List;
import java.util.UUID;

public class FriendRequestService {

    private final Datastore _datastore;

    public FriendRequestService(LobbySystem lobbySystem) {
        _datastore = lobbySystem.getMongoManager().getDatastore();
    }

    public void add(UUID sender, UUID target) {
        var document = new FriendRequestDocument(sender, target);
        _datastore.save(document);
    }

    public void remove(UUID sender, UUID target) {
        var document = _datastore.find(FriendRequestDocument.class)
                .filter((Filter) Filters.eq("sender", sender))
                .filter((Filter) Filters.eq("target", target)).first();

        if (document != null) _datastore.delete(document);
    }

    public List<FriendRequestDocument> getRequests(UUID target) {
        return _datastore.find(FriendRequestDocument.class)
                .filter((Filter) Filters.eq("target", target)).stream().toList();
    }
}
