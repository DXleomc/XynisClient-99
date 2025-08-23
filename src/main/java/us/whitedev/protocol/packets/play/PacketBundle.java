package us.whitedev.protocol.packets.play;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import us.whitedev.protocol.components.ComponentsCodec;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.Packet;
import us.whitedev.protocol.packets.PacketCodec;

public class PacketBundle implements Packet {
    private static final int PACKET_ID = 0x02;

    private final int slot;
    private final int slotIn;

    public PacketBundle(int slot, int slotIn) {
        this.slot = slot;
        this.slotIn = slotIn;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeVarInt(buf, PACKET_ID);
        PacketCodec.writeVarInt(buf, slot);
        PacketCodec.writeVarInt(buf, slotIn);
    }

}
