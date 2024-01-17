package de.noque.lobbysystem.model;

import de.noque.lobbysystem.model.enums.State;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import org.bson.types.ObjectId;

@Entity("server") @Data
public class ServerDocument {

    private @Id ObjectId Id;
    private String Name;
    private de.noque.lobbysystem.model.enums.State State;
    private String GameMode;

    public ServerDocument() {}

    public ServerDocument(String name, State state, String gameMode) {
        Name = name;
        State = state;
        GameMode = gameMode;
    }
}
