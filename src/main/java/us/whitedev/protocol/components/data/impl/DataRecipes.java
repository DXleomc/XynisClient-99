package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.ListTag;
import us.whitedev.protocol.components.ComponentsCodec;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;
import java.util.Map;

public class DataRecipes implements DataComponent {
    private final ListTag items;

    public DataRecipes(ListTag items) {
        this.items = items;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                766, 42, 767, 43,
                768, 53, 769, 53
        );
    }

    @Override
    public void write(ByteBuf buf) {
        ComponentsCodec.writeTag(buf, items);
    }
}
