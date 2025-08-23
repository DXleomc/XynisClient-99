package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import us.whitedev.protocol.components.ComponentsCodec;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;
import java.util.Map;

public class DataBees implements DataComponent {
    private final List<BeeEntry> bees;

    public DataBees(List<BeeEntry> bees) {
        this.bees = bees;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                766, 53, 767, 54,
                768, 64, 769, 64
        );
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeList(buf, bees, DataBees::writeBees);
    }

    private static void writeBees(ByteBuf buf, BeeEntry beeEntry){
        ComponentsCodec.writeTag(buf, beeEntry.entityData);
        PacketCodec.writeVarInt(buf, beeEntry.ticksInHive);
        PacketCodec.writeVarInt(buf, beeEntry.minTicksInHive);
    }

    public record BeeEntry(CompoundTag entityData, int ticksInHive, int minTicksInHive) {}
}
