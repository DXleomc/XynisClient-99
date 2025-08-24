package us.whitedev.proxy.listeners;

import com.github.steveice10.mc.protocol.ServerLoginHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.Session;
import us.whitedev.proxy.functions.SessionCreator;
import us.whitedev.proxy.repository.OptionsRep;
import us.whitedev.proxy.utils.ColorUtil;

import java.util.ArrayList;

public class ServerLoginListener implements ServerLoginHandler {
    private final SessionCreator sessionCreator = new SessionCreator();
    private final OptionsRep optionsRep = OptionsRep.getInstance();

    @Override
    public void loggedIn(Session session) {
        if (optionsRep.getServerIp() != null && !optionsRep.getServerIp().isEmpty()) {
            session.send(generateLoginPacket());
            session.send(new ClientboundPlayerPositionPacket(0.0, 0.0, 0.0, 0F, 0F, 0, new ArrayList<>()));
            session.send(new ClientboundPlayerAbilitiesPacket(true, true, true, true, 0F, 0F));
            session.addListener(new ClientTransferListener());
            sessionCreator.initSession(session, false, optionsRep.getUsername(), optionsRep.getPassword());
        }else{
            session.disconnect(ColorUtil.format("&cYou need to set a target IP to connect to the proxy!"));
        }
    }

    private ClientboundLoginPacket generateLoginPacket(){
        String[] worldNames = new String[] {"minecraft:overworld"};
        CompoundTag registry = new CompoundTag("");
        return new ClientboundLoginPacket(0, false, GameMode.SPECTATOR, null, worldNames, registry, "minecraft:overworld", "minecraft:overworld", 0L, 1, 2, 2, false, true, false, false, null, 0);
    }

}
