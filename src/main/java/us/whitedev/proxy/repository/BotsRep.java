package us.whitedev.proxy.repository;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosRotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerRotPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotsRep {
    private static BotsRep botsRep;
    private final OptionsRep optionsRep = OptionsRep.getInstance();
    private final List<Session> sessions = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static BotsRep getInstance(){
        if(botsRep == null) botsRep = new BotsRep();
        return botsRep;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public List<Session> getConnectedSessions(){
        return sessions.stream().filter(Session::isConnected).toList();
    }

    public void addSession(Session session){
        sessions.add(session);
    }

    public void removeSession(Session session){
        sessions.remove(session);
    }

    public void setLocation(double x, double y, double z, float yaw, float pitch, boolean onGround){
        sendPacket(new ServerboundMovePlayerPosRotPacket(onGround, x, y, z, yaw, pitch), false);
    }

    public void setLocation(double x, double y, double z, boolean onGround){
        sendPacket(new ServerboundMovePlayerPosPacket(onGround, x, y, z), false);
    }

    public void setRotation(float yaw, float pitch, boolean onGround){
        sendPacket(new ServerboundMovePlayerRotPacket(onGround, yaw, pitch), false);
    }

    public void sendPacket(Packet packet, boolean force) {
        if (optionsRep.isMother() || force) {
            List<Session> sessions = getConnectedSessions();
            long initialDelay = 0;
            long delayIncrement = optionsRep.getMotherDelay();

            for (Session session : sessions) {
                long delay = initialDelay;
                scheduler.schedule(() -> session.send(packet), delay, TimeUnit.MILLISECONDS);
                initialDelay += delayIncrement;
            }
        }
    }


    public void disconnectAll(){
        getConnectedSessions().forEach(s -> {
            s.disconnect("Disconnected by proxy root");
        });
    }

}
