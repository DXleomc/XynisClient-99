package us.whitedev.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class MessageHelper {

    private final String prefix = colorFix("&8[&bXynisClient&8] &8>> &7");
    private static final Map<String, ChatFormatting> colorMap = new HashMap<>();

    public void sendMessage(String msg, boolean prefixBool) {
        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(colorFix(prefixBool ? prefix + msg : msg)));
    }

    public void sendSeparateLine(){
        sendMessage("", false);
    }

    static {
        colorMap.put("&f", ChatFormatting.WHITE);
        colorMap.put("&0", ChatFormatting.BLACK);
        colorMap.put("&b", ChatFormatting.BLUE);
        colorMap.put("&1", ChatFormatting.DARK_BLUE);
        colorMap.put("&3", ChatFormatting.AQUA);
        colorMap.put("&e", ChatFormatting.YELLOW);
        colorMap.put("&6", ChatFormatting.GOLD);
        colorMap.put("&a", ChatFormatting.GREEN);
        colorMap.put("&2", ChatFormatting.DARK_GREEN);
        colorMap.put("&7", ChatFormatting.GRAY);
        colorMap.put("&8", ChatFormatting.DARK_GRAY);
        colorMap.put("&d", ChatFormatting.LIGHT_PURPLE);
        colorMap.put("&5", ChatFormatting.DARK_PURPLE);
        colorMap.put("&c", ChatFormatting.RED);
        colorMap.put("&4", ChatFormatting.DARK_RED);
        colorMap.put("&l", ChatFormatting.BOLD);
        colorMap.put("&n", ChatFormatting.ITALIC);
        colorMap.put("&r", ChatFormatting.RESET);
    }

    private String colorFix(String string) {
        for (Map.Entry<String, ChatFormatting> entry : colorMap.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue().toString());
        }
        return string;
    }

}
