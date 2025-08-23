package us.whitedev.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.commands.SharedSuggestionProvider;
import us.whitedev.commands.Command;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DetectCommand implements Command {
    private static final String COMMAND_NAME = "detect";
    private final Map<String, String[]> knownAntiCrashers = Map.of(
            "LPX", new String[]{"lpx"},
            "ExploitFixer", new String[]{"ef", "exploitfixer"},
            "SpigotGuard", new String[]{"sg", "spigotguard"},
            "Sierra", new String[]{"sierra"},
            "IceGuard", new String[]{"iceguard"},
            "Honey", new String[]{"honey"},
            "ServerPatches", new String[]{"spatches"}
    );

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        List<String> detectedAc = new ArrayList<>();
        if (mc.player != null) {
            CompletableFuture<Suggestions> pendingSuggestions = getTabResponse("", 0);
            pendingSuggestions.thenAccept(suggestions -> {
                suggestions.getList().forEach(suggestion -> knownAntiCrashers.keySet().forEach(key ->
                        Arrays.stream(knownAntiCrashers.get(key))
                                .filter(alias -> suggestion.getText().equalsIgnoreCase(alias))
                                .forEach(alias -> addIfDoesntExist(key, detectedAc))));

                if (!detectedAc.isEmpty()) {
                    msgHelper.sendMessage("Detected &8(&f" + detectedAc.size() + "&8) &7AntiCrash Plugin" + (detectedAc.size() > 1 ? "s" : ""), true);
                    detectedAc.forEach(name -> msgHelper.sendMessage("&8- &f" + name, true));
                } else {
                    msgHelper.sendMessage("No AntiCrash detected!", true);
                }
            });
        }
    }


    public CompletableFuture<Suggestions> getTabResponse(String content, int cursorIndex){
        CommandDispatcher<SharedSuggestionProvider> commanddispatcher = mc.player.connection.getCommands();
        ParseResults<SharedSuggestionProvider> currentParse = commanddispatcher.parse(new StringReader(content), mc.player.connection.getSuggestionsProvider());
        return commanddispatcher.getCompletionSuggestions(currentParse, cursorIndex);
    }


    private void addIfDoesntExist(String name, List<String> list){
        if(!list.contains(name)) list.add(name);
    }
}
