package us.whitedev.commands.impl;

import net.minecraft.client.Minecraft;
import us.whitedev.commands.Command;
import us.whitedev.commands.CommandManager;
import us.whitedev.crashers.CrashManager;
import us.whitedev.exploits.ExploitManager;

public class CrashCommand implements Command {
    private static final String COMMAND_NAME = "crash";
    private final Minecraft mc = Minecraft.getInstance();
    private final CrashManager crashManager = CrashManager.getManager();

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        if(mc.player != null && mc.getConnection() != null){
            StringBuilder builder = new StringBuilder();
            for (String arg : args) builder.append(arg).append(" ");
            crashManager.handleMethod(CommandManager.COMMAND_PREFIX + builder);
        }
    }
}
