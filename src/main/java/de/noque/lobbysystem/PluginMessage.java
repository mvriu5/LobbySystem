package de.noque.lobbysystem;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PluginMessage implements PluginMessageListener {

    private LobbySystem plugin;

    public PluginMessage(LobbySystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subchannel = input.readUTF();

        if (subchannel.equals("Connect")) return;
    }

    public void connect(String server, Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }
}