package us.whitedev.proxy.repository;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosRotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerRotPacket;
import com.github.steveice10.packetlib.Session;

public class SessionRep {
    private static SessionRep sessionRep;
    private Session session;
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    private boolean onGround;

    public static SessionRep getInstance(){
        if(sessionRep == null){
            sessionRep = new SessionRep();
        }
        return sessionRep;
    }

    public Session getSession() {
        return session;
    }

    public void setLocation(double x, double y, double z, float yaw, float pitch, boolean onGround){
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        session.send(new ServerboundMovePlayerPosRotPacket(onGround, x, y, z, yaw, pitch));
    }

    public void setLocation(double x, double y, double z, boolean onGround){
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.onGround = onGround;
        session.send(new ServerboundMovePlayerPosPacket(onGround, x, y, z));
    }

    public void setRotation(float yaw, float pitch, boolean onGround){
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        session.send(new ServerboundMovePlayerRotPacket(onGround, yaw, pitch));
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
