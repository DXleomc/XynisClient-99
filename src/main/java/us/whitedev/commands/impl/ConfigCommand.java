package us.whitedev.commands.impl;

import us.whitedev.commands.Command;
import us.whitedev.helpers.ConfigHelper;

public class ConfigCommand implements Command {
    private static final String COMMAND_NAME = "loadconfig";
    private final ConfigHelper configHelper = new ConfigHelper();

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 2) {
            String name = args[1];
            if(configHelper.loadConfig(name)) {
                msgHelper.sendMessage("Config &f" + name + " &7was loaded &asuccessful&7!", true);
            }else if(name.equalsIgnoreCase("clear")) {
                ConfigHelper.loadedConfigs.clear();
                msgHelper.sendMessage("Loaded configs was cleared &asuccessful&7!", true);
            }else{
                msgHelper.sendMessage("Config &f" + name + " &7doesnt &cexist&7!", true);
            }
        }else{
            msgHelper.sendMessage("Usage&8: &f!loadconfig <name/clear> &7!", true);
            msgHelper.sendMessage("Example&8: &f!loadconfig LPX335", true);

        }
    }
}
