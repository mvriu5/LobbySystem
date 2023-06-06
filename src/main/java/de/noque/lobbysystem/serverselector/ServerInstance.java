package de.noque.lobbysystem.serverselector;

import lombok.Getter;

public class ServerInstance {

    @Getter
    private String serverName;
    @Getter
    private String serverState;
    @Getter
    private String gameMode;
    @Getter
    private int playerCount;

    public ServerInstance(String _serverName, String _serverState, String _gameMode, int _playerCount) {
        serverName = _serverName;
        serverState = _serverState;
        gameMode = _gameMode;
        playerCount = _playerCount;
    }
}
