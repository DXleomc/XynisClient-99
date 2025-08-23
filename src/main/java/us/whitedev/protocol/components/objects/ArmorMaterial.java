package us.whitedev.protocol.components.objects;

import net.minecraft.nbt.CompoundTag;

import java.util.Map;

public record ArmorMaterial(String assetName, int ingredient, Map<String, String> armorMaterials, CompoundTag tag) {
}
