package us.whitedev.proxy.listeners;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import us.whitedev.proxy.XynisProxy;
import us.whitedev.proxy.repository.BotsRep;
import us.whitedev.proxy.utils.LogType;
import us.whitedev.proxy.utils.ProxyLogger;

public class BotListener extends SessionAdapter {
    private final BotsRep botsRep = BotsRep.getInstance();

    @Override
    public void packetSent(Session session, Packet packet) {
        if(XynisProxy.DEBUG_MODE) ProxyLogger.send("Packet Sent by Bot -> " + packet.getClass().getName(), LogType.DEBUG);
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        if(XynisProxy.DEBUG_MODE) ProxyLogger.send("Packet Received by Bot -> " + packet.getClass().getName(), LogType.DEBUG);
    }

    @Override
    public void connected(ConnectedEvent event){
        ProxyLogger.send("New session joined on the server!", LogType.INFO);
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        if(XynisProxy.DEBUG_MODE) ProxyLogger.send("Bot Disconnected with reason: " + event.getReason(), LogType.DEBUG);
        botsRep.removeSession(event.getSession());
    }
}
