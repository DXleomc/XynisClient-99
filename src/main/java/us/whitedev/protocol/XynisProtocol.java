package us.whitedev.protocol;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import us.whitedev.helpers.PacketHelper;

import java.util.ArrayList;
import java.util.List;

public class XynisProtocol {
    public static final List<ByteBuf> OUT_PACKETS = new ArrayList<>();
    public static final List<Integer> SUPPORTED_PROTOCOLS = List.of(766, 767, 768, 769);

    public void addPacketsToSend(List<ByteBuf> packets){
        OUT_PACKETS.addAll(packets);
        PacketHelper.send(new ServerboundKeepAlivePacket(0)); // only for trigger
    }
}
