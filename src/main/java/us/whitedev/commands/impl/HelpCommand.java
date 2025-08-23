package us.whitedev.commands.impl;

import us.whitedev.commands.Command;

public class HelpCommand implements Command {
    private static final String COMMAND_NAME = "help";

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        msgHelper.sendSeparateLine();
        msgHelper.sendMessage("All available commands:", true);
        msgHelper.sendMessage("authors &8- &fCheck Authors of This Client", true);
        msgHelper.sendMessage("bypasslist &8- &fSearch Bypass Config", true);
        msgHelper.sendMessage("stop &8- &fStop All Started Crashers", true);
        msgHelper.sendMessage("crash &8- &fCheck/Send Crash Packet", true);
        msgHelper.sendMessage("exploit &8- &fCheck/Send Exploit", true);
        msgHelper.sendMessage("fakegm &8- &fSpoof GameMode for Your Session", true);
        msgHelper.sendMessage("detect &8- &fDetect AntiCrash on Server", true);
        msgHelper.sendMessage("proxy &8- &fManipulate bots/proxy settings", true);
        msgHelper.sendMessage("loadconfig &8- &fLoad Configs for Crashers/Exploits", true);
        msgHelper.sendSeparateLine();
    }
}
