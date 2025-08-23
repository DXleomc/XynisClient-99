package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import us.whitedev.protocol.components.ComponentsCodec;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.components.objects.ArmorMaterial;
import us.whitedev.protocol.components.objects.ArmorPattern;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.Map;

public class DataArmorTrim implements DataComponent {
    private final ArmorMaterial armorMaterial;
    private final ArmorPattern armorPattern;
    private final boolean showInTooltip;

    public DataArmorTrim(ArmorMaterial armorMaterial, ArmorPattern armorPattern, boolean showInTooltip) {
        this.armorMaterial = armorMaterial;
        this.armorPattern = armorPattern;
        this.showInTooltip = showInTooltip;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                768, 45
        );
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeVarInt(buf, 0);
        writeArmorMaterial(buf);
        PacketCodec.writeVarInt(buf, 0);
        writeArmorPattern(buf);
        buf.writeBoolean(showInTooltip);
    }

    private void writeArmorMaterial(ByteBuf buf) {
        PacketCodec.writeUtf(buf, armorMaterial.assetName());
        PacketCodec.writeVarInt(buf, armorMaterial.ingredient());

        buf.writeFloat(1.0f);

        PacketCodec.writeVarInt(buf, armorMaterial.armorMaterials().size());
        armorMaterial.armorMaterials().forEach((id, name) -> {
            PacketCodec.writeUtf(buf, id);
            PacketCodec.writeUtf(buf, name);
        });

        ComponentsCodec.writeTag(buf, armorMaterial.tag());
    }

    private void writeArmorPattern(ByteBuf buf) {
        PacketCodec.writeUtf(buf, armorPattern.assetId());
        PacketCodec.writeVarInt(buf, armorPattern.templateItem());
        ComponentsCodec.writeTag(buf, armorPattern.tag());
        buf.writeBoolean(armorPattern.isDecal());
    }
}
