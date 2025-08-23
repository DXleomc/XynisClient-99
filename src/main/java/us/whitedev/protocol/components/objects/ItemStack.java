package us.whitedev.protocol.components.objects;

import us.whitedev.protocol.components.data.DataComponents;

public record ItemStack(int id, int amount, DataComponents dataComponents) {
}