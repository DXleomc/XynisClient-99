package us.whitedev.helpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

public class ExceptionPacket implements Packet<ServerGamePacketListener> {

    // LPX 3.4.0/3.4.2 PacketEvent ABUSER
    // This Packet is only for creating exception by sending random invalid packetid

    @Override
    public void write(FriendlyByteBuf p_131343_) {}

    @Override
    public void handle(ServerGamePacketListener p_131342_) {}
}
