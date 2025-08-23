package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;

public class MappedEntitySet {
    public static void write(ByteBuf buf, String tagKey, List<Integer> entities) {
        if (tagKey != null) {
            buf.writeByte(0);
            PacketCodec.writeUtf(buf, tagKey);
            return;
        }

        PacketCodec.writeVarInt(buf, entities.size() + 1);
        for (int entity : entities) {
            PacketCodec.writeVarInt(buf, entity);
        }
    }
}