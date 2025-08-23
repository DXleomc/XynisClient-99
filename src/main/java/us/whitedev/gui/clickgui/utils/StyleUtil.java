package us.whitedev.gui.clickgui.utils;

import us.whitedev.gui.clickgui.ClickGui;

import java.util.Locale;

public class StyleUtil {

    private static int shapeInt = 30;

    private static int[] getRainbowGradient(){
        float hue = (float) ((System.currentTimeMillis() / 1000.0) * 0.2f % 1.0);
        int colorFrom = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
        int red1 = (colorFrom >> 16) & 0xFF;
        int green1 = (colorFrom >> 8) & 0xFF;
        int blue1 = colorFrom & 0xFF;
        int colorTo = java.awt.Color.HSBtoRGB((hue + (1.0f / 6.0f)) % 1.0f, 1.0f, 1.0f);
        int red2 = (colorTo >> 16) & 0xFF;
        int green2 = (colorTo >> 8) & 0xFF;
        int blue2 = colorTo & 0xFF;
        return new int[]{red1, green1, blue1, red2, green2, blue2};
    }

    public static String getFinalColor() {
        String color;
        switch (ClickGui.guiStyle){
            case RAINBOW -> color = "linear-gradient(to right, rgba(" + getRainbowGradient()[0] + ", " + getRainbowGradient()[1] + ", " + getRainbowGradient()[2] + ") 30%, rgba(" + getRainbowGradient()[3] + ", " + getRainbowGradient()[4] + ", " + getRainbowGradient()[5] + ") 70%);";
            case DEFAULT -> color = "rgba(40, 255, 246);";
            default -> color = ClickGui.guiStyle.name().toLowerCase(Locale.ROOT) + ";";
        }
        return color;
    }

    public static int getShapeInt() {
        return shapeInt;
    }

    public static void setShapeInt(int shapeInt) {
        StyleUtil.shapeInt = shapeInt;
    }

}
