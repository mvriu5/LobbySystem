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
    @Getter
    private List<String> serverList;

    public PluginMessage(LobbySystem plugin) {
        this.plugin = plugin;
        this.serverList = new ArrayList<>();
    }

    public void registerChannel(Player player) {
        player.sendPluginMessage(plugin, "BungeeCord", getServers());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subChannel = input.readUTF();

        if (subChannel.equals("GetServers")) {
            short serverCount = input.readShort();
            for (int i = 0; i < serverCount; i++) {
                serverList.add(input.readUTF());
            }
        }
    }

    public void connect(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public byte[] getServers() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServers");
        return output.toByteArray();
    }
}