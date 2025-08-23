package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataProfile implements DataComponent {
    private final String name;
    private final UUID uuid;
    private final List<Property> properties;

    public DataProfile(String name, UUID uuid, List<Property> properties) {
        this.name = name;
        this.uuid = uuid;
        this.properties = properties;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                766, 46, 767, 47,
                768, 57, 769, 57
        );
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeNullable(buf, name, PacketCodec::writeUtf);
        PacketCodec.writeNullable(buf, uuid, PacketCodec::writeUUID);
        PacketCodec.writeList(buf, properties, DataProfile::writeProperty);
    }

    private static void writeProperty(ByteBuf buf, Property property) {
        PacketCodec.writeUtf(buf, property.name);
        PacketCodec.writeUtf(buf, property.value);
        PacketCodec.writeNullable(buf, property.signature, PacketCodec::writeUtf);
    }

    public record Property(String name, String value, String signature){}
}