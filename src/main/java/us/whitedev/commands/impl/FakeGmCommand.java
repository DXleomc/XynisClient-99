package us.whitedev.commands.impl;

import net.minecraft.world.level.GameType;
import us.whitedev.commands.Command;

import java.util.Locale;

public class FakeGmCommand implements Command {
    private static final String COMMAND_NAME = "fakegm";

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length == 2 && mc.gameMode != null){
            String gamemode = args[1].toLowerCase(Locale.ROOT);
            switch (gamemode){
                case "creative", "1" -> mc.gameMode.setLocalMode(GameType.CREATIVE);
                case "adventure", "2" -> mc.gameMode.setLocalMode(GameType.ADVENTURE);
                case "spectator", "3" -> mc.gameMode.setLocalMode(GameType.SPECTATOR);
                default -> mc.gameMode.setLocalMode(GameType.SURVIVAL);
            }
            msgHelper.sendMessage("Your GameMode was changed to: &f" + mc.gameMode.getPlayerMode().getName(), true);
        }else{
            msgHelper.sendMessage("Usage&8: &f!fakegm (creative/survival/adventure)", true);
        }
    }
}
