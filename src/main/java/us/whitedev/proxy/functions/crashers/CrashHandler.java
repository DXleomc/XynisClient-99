package us.whitedev.proxy.functions.crashers;

import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.MessageHelper;
import us.whitedev.proxy.repository.OptionsRep;
import us.whitedev.proxy.utils.LogType;
import us.whitedev.proxy.utils.ProxyLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrashHandler {
    private static CrashHandler crashHandler;
    private final OptionsRep optionsRep = OptionsRep.getInstance();
    private final MessageHelper msgHelper = new MessageHelper();
    private final List<BotCrasher> allMethods = new ArrayList<>();

    public static CrashHandler getInstance() {
        if (crashHandler == null) crashHandler = new CrashHandler();
        return crashHandler;
    }

    public List<BotCrasher> getAllMethods() {
        return allMethods;
    }

    public void registerMethods(BotCrasher... crashers) {
        allMethods.addAll(List.of(crashers));
    }

    public void handleMethod(String name) {
        allMethods.forEach(c -> {
            String[] args = optionsRep.getOptionsForCrash(name);
            if (c.getName().equals(name)) {
                if (args.length >= c.getArgsName().length) {
                    c.onMethod(args);
                } else {
                    ProxyLogger.send("An error occurred during startup, please make sure the config is correct!", LogType.ERROR);
                    msgHelper.sendMessage("An &cerror &7occurred during startup, please make sure the config is correct!", true);
                }
            }
        });
    }

    public Optional<BotCrasher> getCrashClassByName(String name) {
        return allMethods.stream().filter((crasher) -> crasher.getName().equalsIgnoreCase(name)).findFirst();
    }
}
