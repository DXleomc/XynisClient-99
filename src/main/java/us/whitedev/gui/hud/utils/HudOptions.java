package us.whitedev.gui.hud.utils;

import java.util.HashMap;
import java.util.Map;

public class HudOptions {
    private static final Map<String, Boolean> hudOptionsMap = new HashMap<>();

    public static void initMap(){
        hudOptionsMap.clear();
        hudOptionsMap.put("IP", true);
        hudOptionsMap.put("Engine", true);
        hudOptionsMap.put("Last Packet", true);
        hudOptionsMap.put("XYZ", false);
        hudOptionsMap.put("FPS", false);
        hudOptionsMap.put("PPS", false);
    }

    public static String[] getMapKeys(){
        return hudOptionsMap.keySet().toArray(new String[0]);
    }

    public static void setOptionValue(String name, boolean value){
        hudOptionsMap.replace(name, value);
    }

    public static boolean getOption(String name){
        return hudOptionsMap.get(name);
    }
}
