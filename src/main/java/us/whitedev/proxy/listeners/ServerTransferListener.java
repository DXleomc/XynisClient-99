package us.whitedev.proxy.listeners;

import com.github.steveice10.mc.protocol.packet.ingame.clientbound.*;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory.ClientboundContainerClosePacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundGameProfilePacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundHelloPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundLoginCompressionPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import us.whitedev.proxy.utils.ColorUtil;

import java.util.Arrays;
import java.util.List;

public class ServerTransferListener extends SessionAdapter {

    List<Class<?>> blockedPackets = Arrays.asList(
            ClientboundLoginCompressionPacket.class,
            ClientboundGameProfilePacket.class,
            ClientboundHelloPacket.class
    );

    private final Session originalSession;

    public ServerTransferListener(Session originalSession) {
        this.originalSession = originalSession;
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        if (blockedPackets.stream().noneMatch(cls -> cls.isInstance(packet))) {
            if (packet instanceof ClientboundLoginPacket loginPacket) {
                originalSession.send(packet);
                spawnPlayer(loginPacket);
            } else {
                originalSession.send(packet);
            }
        }
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        if (event.getCause() != null) {
            event.getCause().printStackTrace();
        }
        originalSession.disconnect(ColorUtil.format("&cDisconnected, reason: &4" + event.getReason()));
    }

    private void spawnPlayer(ClientboundLoginPacket packet) {
        originalSession.send(new ClientboundLoginPacket(packet.getEntityId(), packet.isHardcore(), packet.getGameMode(), packet.getPreviousGamemode(), packet.getWorldNames(), packet.getRegistry(), packet.getDimension(), packet.getWorldName(), packet.getHashedSeed(), packet.getMaxPlayers(), packet.getViewDistance(), packet.getSimulationDistance(), packet.isReducedDebugInfo(), packet.isEnableRespawnScreen(), packet.isDebug(), packet.isFlat(), packet.getLastDeathPos(), packet.getPortalCooldown()));
        originalSession.send(new ClientboundRespawnPacket(packet.getDimension(), packet.getWorldName(), packet.getHashedSeed(), packet.getGameMode(), packet.getPreviousGamemode(), packet.isDebug(), packet.isFlat(), true, true, packet.getLastDeathPos(), packet.getPortalCooldown()));
        originalSession.send(new ClientboundContainerClosePacket(0));
    }
}