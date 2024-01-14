package de.noque.lobbysystem.serverselector;

import lombok.Getter;

public class ServerInstance {

    private @Getter String serverName;
    private @Getter String serverState;
    private @Getter String gameMode;
    private @Getter int playerCount;

    public ServerInstance(String _serverName, String _serverState, String _gameMode, int _playerCount) {
        serverName = _serverName;
        serverState = _serverState;
        gameMode = _gameMode;
        playerCount = _playerCount;
    }
}
