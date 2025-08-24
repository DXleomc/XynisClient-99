package us.whitedev.protocol.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import us.whitedev.protocol.XynisProtocol;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class PacketCodec {
    private static final XynisProtocol xynisProtocol = new XynisProtocol();

    public static void sendPacket(Packet packet, int packetAmount) {
        System.out.println("chuj");
        ByteBuf buf = Unpooled.buffer();
        packet.write(buf);

        List<ByteBuf> buffs = new ArrayList<>();
        for (int i = 0; i < packetAmount; i++)
            buffs.add(buf.copy());

        xynisProtocol.addPacketsToSend(buffs);
    }

    public static void writeVarInt(ByteBuf buf, int value) {
        while ((value & -128) != 0) {
            buf.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        buf.writeByte(value);
    }

    public static void writeUtf(ByteBuf buf, String string) {
        byte[] abyte = string.getBytes(StandardCharsets.UTF_8);
        writeVarInt(buf, abyte.length);
        buf.writeBytes(abyte);
    }

    public static void writeUUID(ByteBuf buf, UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public static <T> void writeNullable(ByteBuf buf, @Nullable T value, BiConsumer<ByteBuf, T> ifPresent) {
        if (value != null) {
            buf.writeBoolean(true);
            ifPresent.accept(buf, value);
        } else {
            buf.writeBoolean(false);
        }
    }

    public static void writeArray(ByteBuf buf, int[] array) {
        PacketCodec.writeVarInt(buf, array.length + 1);
        for (int holder : array) {
            PacketCodec.writeVarInt(buf, holder);
        }
    }

    public static <T> void writeList(ByteBuf buf, List<T> value, BiConsumer<ByteBuf, T> writer) {
        PacketCodec.writeVarInt(buf, value.size());
        for (T t : value) {
            writer.accept(buf, t);
        }
    }
}
