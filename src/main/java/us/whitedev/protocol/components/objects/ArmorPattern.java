package us.whitedev.protocol.components.objects;

import net.minecraft.nbt.CompoundTag;

public record ArmorPattern(String assetId, int templateItem, CompoundTag tag, boolean isDecal) {
}
