package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import us.whitedev.protocol.components.ComponentsCodec;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;
import java.util.Map;

public class DataLore implements DataComponent {
    private final List<CompoundTag> compoundTags;

    public DataLore(List<CompoundTag> compoundTags) {
        this.compoundTags = compoundTags;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                766, 7, 767, 7,
                768, 8, 769, 8
        );
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeList(buf, compoundTags, ComponentsCodec::writeTag);
    }
}
