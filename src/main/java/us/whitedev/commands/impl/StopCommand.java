package us.whitedev.commands.impl;

import us.whitedev.commands.Command;
import us.whitedev.crashers.CrashManager;
import us.whitedev.crashers.Crasher;

import java.util.HashSet;
import java.util.Set;

public class StopCommand implements Command {
    private static final String COMMAND_NAME = "stop";

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        Set<String> stoppedCrashers = new HashSet<>();
        for (Crasher crasher : CrashManager.getManager().getMethodsList()) {
            if(crasher.getEnabled()) {
                crasher.setEnabled(false);
                stoppedCrashers.add(crasher.getName());
            }
        }
        if(stoppedCrashers.isEmpty()) {
            msgHelper.sendMessage("Any crasher isn't active!", true);
        }else{
            msgHelper.sendSeparateLine();
            msgHelper.sendMessage("All crashers has been &cstopped!", true);
            msgHelper.sendMessage("List of stopped crashers &8-> &f" + String.join(", ", stoppedCrashers), true);
            msgHelper.sendSeparateLine();
        }
    }
}
