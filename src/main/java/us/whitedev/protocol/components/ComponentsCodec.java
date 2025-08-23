package us.whitedev.protocol.components;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.Tag;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ComponentsCodec {

    public static void writeItem(ByteBuf buf, ItemStack item) {
        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        boolean empty = item == null || item.amount() <= 0;
        PacketCodec.writeVarInt(buf, !empty ? item.amount() : 0);
        if (!empty) {
            PacketCodec.writeVarInt(buf, item.id());
            writeDataComponents(buf, item.dataComponents(), protocol);
        }
    }

    public static void writeDataComponents(ByteBuf buf, DataComponents dataComponents, int protocol) {
        if(dataComponents != null) {
            PacketCodec.writeVarInt(buf, dataComponents.getDataComponents().size());
            PacketCodec.writeVarInt(buf, 0);
            for (DataComponent dataComponent : dataComponents.getDataComponents()) {
                PacketCodec.writeVarInt(buf, dataComponent.getIds().getOrDefault(protocol, 0));
                dataComponent.write(buf);
            }
        }else{
            PacketCodec.writeVarInt(buf, 0);
            PacketCodec.writeVarInt(buf, 0);
        }
    }

    public static void writeTag(ByteBuf buf, Tag tag) {
        try {
            buf.writeByte(tag.getId());
            if (tag.getId() == Tag.TAG_END) return;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            tag.write(dos);
            buf.writeBytes(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
