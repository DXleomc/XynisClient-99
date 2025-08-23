package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import us.whitedev.protocol.components.ComponentsCodec;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;
import java.util.Map;

public class DataChargedProjectiles implements DataComponent {
    private final List<ItemStack> itemStackList;

    public DataChargedProjectiles(List<ItemStack> itemStackList) {
        this.itemStackList = itemStackList;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                766, 29, 767, 29,
                768, 39, 769, 39
        );
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeVarInt(buf, itemStackList.size());
        itemStackList.forEach(itemStack -> ComponentsCodec.writeItem(buf, itemStack));
    }
}
