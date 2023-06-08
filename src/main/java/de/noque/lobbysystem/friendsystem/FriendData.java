package de.noque.lobbysystem.friendsystem;

import com.mongodb.client.model.Filters;
import de.noque.lobbysystem.LobbySystem;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FriendData {

    public FriendData() {}

    public void addDocument(Player player) {
        new Document("uuid", player.getUniqueId()).append("friends", "");
    }

    public void addFriend(Player player, Player target) {
        Document playerDoc = LobbySystem.getFriendCollection().find(Filters.eq("uuid", player.getUniqueId())).first();
        assert playerDoc != null;
        String playerFriends = (String) playerDoc.get("friends");
        String newFriendListPlayer = playerFriends + target.getUniqueId() + ",";
        Document playerUpdate = new Document("$set", new Document("friends", newFriendListPlayer));

        Document targetDoc = LobbySystem.getFriendCollection().find(Filters.eq("uuid", target.getUniqueId())).first();
        assert targetDoc != null;
        String targetFriends = (String) targetDoc.get("friends");
        String newFriendListTarget = targetFriends + target.getUniqueId() + ",";
        Document targetUpdate = new Document("$set", new Document("friends", newFriendListTarget));

        LobbySystem.getFriendCollection().updateOne(playerDoc, playerUpdate);
        LobbySystem.getFriendCollection().updateOne(targetDoc, targetUpdate);
    }

    public void removeFriend(Player player, Player target) {
        //REMOVE
    }

    public List<UUID> getFriends(Player player) {
        Document found = LobbySystem.getFriendCollection().find(Filters.eq("uuid", player.getUniqueId())).first();
        String friends = (String) found.get("friends");
        List<String> list = Arrays.asList(friends.split("\\s*,\\s*"));

        List<UUID> friendList = new ArrayList<>();
        for (String item : list) friendList.add(UUID.fromString(item));
        return friendList;
    }
}
