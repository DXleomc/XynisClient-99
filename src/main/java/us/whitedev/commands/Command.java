package us.whitedev.commands;

import net.minecraft.client.Minecraft;
import us.whitedev.helpers.BypassHelper;
import us.whitedev.helpers.MessageHelper;

public interface Command {
    String getName();
    void onCommand(String[] args);

    MessageHelper msgHelper = new MessageHelper();
    BypassHelper bypassHelper = new BypassHelper();
    Minecraft mc = Minecraft.getInstance();
}
