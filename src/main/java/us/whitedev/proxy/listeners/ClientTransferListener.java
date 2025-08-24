package us.whitedev.proxy.listeners;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosRotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerRotPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import us.whitedev.proxy.repository.BotsRep;
import us.whitedev.proxy.repository.SessionRep;
import us.whitedev.proxy.utils.LogType;
import us.whitedev.proxy.utils.ProxyLogger;

public class ClientTransferListener extends SessionAdapter {
    private final SessionRep sessionRep = SessionRep.getInstance();
    private final BotsRep botsRep = BotsRep.getInstance();

    @Override
    public void packetReceived(Session session, Packet packet) {
        if (sessionRep.getSession().isConnected()) {
            if (packet instanceof ServerboundMovePlayerPosRotPacket posRotPacket) {
                sessionRep.setLocation(posRotPacket.getX(), posRotPacket.getY(), posRotPacket.getZ(), posRotPacket.getYaw(), posRotPacket.getPitch(), posRotPacket.isOnGround());
                botsRep.setLocation(posRotPacket.getX(), posRotPacket.getY(), posRotPacket.getZ(), posRotPacket.getYaw(), posRotPacket.getPitch(), posRotPacket.isOnGround());
            } else if (packet instanceof ServerboundMovePlayerPosPacket posRotPacket) {
                sessionRep.setLocation(posRotPacket.getX(), posRotPacket.getY(), posRotPacket.getZ(), posRotPacket.isOnGround());
                botsRep.setLocation(posRotPacket.getX(), posRotPacket.getY(), posRotPacket.getZ(), posRotPacket.isOnGround());
            } else if (packet instanceof ServerboundMovePlayerRotPacket rotPacket) {
                sessionRep.setRotation(rotPacket.getYaw(), rotPacket.getPitch(), rotPacket.isOnGround());
                botsRep.setRotation(rotPacket.getYaw(), rotPacket.getPitch(), rotPacket.isOnGround());
            } else {
                if (!(packet instanceof ServerboundKeepAlivePacket)) {
                    sessionRep.getSession().send(packet);
                    botsRep.sendPacket(packet, false);
                }
            }
        }
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        if (event.getCause() != null) {
            event.getCause().printStackTrace();
        }
        sessionRep.getSession().disconnect(event.getReason());
    }
}
