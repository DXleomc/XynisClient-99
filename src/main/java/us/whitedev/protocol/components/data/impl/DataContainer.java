package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import us.whitedev.protocol.components.ComponentsCodec;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;
import java.util.Map;

public class DataContainer implements DataComponent {
    private final List<ItemStack> items;

    public DataContainer(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                766, 51, 767, 52,
                768, 62, 769, 62
        );
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeList(buf, items, ComponentsCodec::writeItem);
    }
}
