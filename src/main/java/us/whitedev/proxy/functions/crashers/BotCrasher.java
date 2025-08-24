package us.whitedev.proxy.functions.crashers;

import net.minecraft.client.Minecraft;
import us.whitedev.helpers.MessageHelper;
import us.whitedev.proxy.repository.BotsRep;

public interface BotCrasher {
    String getName();
    void onMethod(String[] args);
    String getDescription();
    String[] getArgsName();
    String[] getArgsType();
    BotsRep botsRep = BotsRep.getInstance();
    MessageHelper msgHelper = new MessageHelper();
    Minecraft mc = Minecraft.getInstance();
}
