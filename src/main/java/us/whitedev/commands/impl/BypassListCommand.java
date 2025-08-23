package us.whitedev.commands.impl;

import us.whitedev.commands.Command;
import us.whitedev.helpers.ConfigHelper;

public class BypassListCommand implements Command {
    private static final String COMMAND_NAME = "bypasslist";
    private final ConfigHelper configHelper = new ConfigHelper();

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        msgHelper.sendSeparateLine();
        msgHelper.sendMessage("Bypass List Configs:", true);
        configHelper.getBypassLists().forEach(bypass -> {
            msgHelper.sendSeparateLine();
            msgHelper.sendMessage(getBypassString(bypass.moduleName(), bypass.bypassFor(), bypass.bypassName(), bypass.configString()), true);
            msgHelper.sendSeparateLine();
        });
    }

    private String getBypassString(String moduleName, String bypassName, String bypassFor, String configString){
        return moduleName + " &8(&f" + bypassName + "&8) &8(&f" + bypassFor + "&8) - &f" + configString;
    }
}
