package de.noque.lobbysystem.service;

import com.mongodb.client.model.Filters;
import de.noque.lobbysystem.manager.MongoManager;
import de.noque.lobbysystem.model.FriendDocument;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filter;
import dev.morphia.query.experimental.updates.UpdateOperators;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendService {

    private final Datastore _datastore;

    public FriendService(MongoManager mongoManager) {
        _datastore = mongoManager.getDatastore();
    }

    public void addDocument(Player player) {
        FriendDocument document = new FriendDocument(player.getUniqueId(), new ArrayList<>());
        _datastore.save(document);
    }

    public void addFriend(Player player, Player target) {
        _datastore.find(FriendDocument.class)
                .filter((Filter) Filters.eq("", player.getUniqueId()))
                .update(UpdateOperators.addToSet("friends", target.getUniqueId()));

        _datastore.find(FriendDocument.class)
                .filter((Filter) Filters.eq("", target.getUniqueId()))
                .update(UpdateOperators.addToSet("friends", player.getUniqueId()));
    }

    public void removeFriend(Player player, Player target) {
        _datastore.find(FriendDocument.class)
                .filter((Filter) Filters.eq("", player.getUniqueId()))
                .update(UpdateOperators.pullAll("friends", List.of(target.getUniqueId())));

        _datastore.find(FriendDocument.class)
                .filter((Filter) Filters.eq("", target.getUniqueId()))
                .update(UpdateOperators.pullAll("friends", List.of(player.getUniqueId())));
    }

    public List<UUID> getFriends(Player player) {
        FriendDocument document = _datastore.find(FriendDocument.class)
                .filter((Filter) Filters.eq("", player.getUniqueId())).first();

        if (document == null) return null;

        return document.getFriends();
    }
}
