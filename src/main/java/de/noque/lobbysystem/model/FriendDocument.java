package de.noque.lobbysystem.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Entity("friends") @Data
public class FriendDocument {

    private @Id ObjectId Id;
    private UUID Player;
    private @Reference List<UUID> Friends;

    public FriendDocument() {}

    public FriendDocument(UUID player, List<UUID> friends) {
        Player = player;
        Friends = friends;
    }
}
