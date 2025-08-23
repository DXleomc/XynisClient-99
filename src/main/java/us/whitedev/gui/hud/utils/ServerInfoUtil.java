package us.whitedev.gui.hud.utils;

import net.minecraft.client.Minecraft;

public class ServerInfoUtil {
    private final Minecraft mc = Minecraft.getInstance();

    public String getBrand() {
        String brand = mc.player.getServerBrand();
        String finalBrand = (brand != null && brand.contains(" ")) ? brand.split(" ")[0] + " -> " + brand.split(" ")[1] : brand;
        if(finalBrand != null) return finalBrand.length() > 29 ? finalBrand.substring(0, 29) : finalBrand;
        else return "unknown";
    }

    public String getServerIp() {
        return mc.getConnection().getConnection().isConnected() ?
                mc.getConnection().getConnection().getRemoteAddress().toString().split("/")[1] : "none";
    }

    public String getPlayerCoordinates() {
        return ((int) mc.player.getX()) + ", " + ((int) mc.player.getY()) + ", " + ((int) mc.player.getZ());
    }
}
