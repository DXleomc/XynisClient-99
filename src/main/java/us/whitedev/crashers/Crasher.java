package us.whitedev.crashers;

import net.minecraft.client.Minecraft;
import us.whitedev.helpers.BypassHelper;
import us.whitedev.helpers.MessageHelper;
import us.whitedev.utils.OptionUtil;

import java.util.List;

public interface Crasher {
    String getName();
    void onMethod(String[] args);
    default boolean getEnabled() { return false; }
    default void setEnabled(boolean bool) {}
    String getArgsUsage();
    String getDescription();
    List<OptionUtil> getOptions();

    MessageHelper msgHelper = new MessageHelper();
    BypassHelper bypassHelper = new BypassHelper();
    Minecraft mc = Minecraft.getInstance();
}
