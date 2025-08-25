package com.viaversion.viaversion.protocols.v1_8to1_9.rewriter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.StringTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.Protocol1_8To1_9;
import com.viaversion.viaversion.protocols.v1_8to1_9.data.EntityIds1_8;
import com.viaversion.viaversion.protocols.v1_8to1_9.data.PotionIdMappings1_9;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.v1_8to1_9.storage.EntityTracker1_9;
import com.viaversion.viaversion.protocols.v1_8to1_9.storage.InventoryTracker;
import com.viaversion.viaversion.rewriter.ItemRewriter;
import com.viaversion.viaversion.util.ComponentUtil;
import com.viaversion.viaversion.util.Key;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ItemPacketRewriter1_9 extends ItemRewriter<ClientboundPackets1_8, ServerboundPackets1_9, Protocol1_8To1_9> {
    private final Gson gson = new Gson();

    public ItemPacketRewriter1_9(Protocol1_8To1_9 protocol) {
        super(protocol, Types.ITEM1_8, Types.ITEM1_8_SHORT_ARRAY);
    }

    // they are stupid asf ahhh
    // easy fixed book json

    protected void registerPackets() {
        this.protocol.registerClientbound(ClientboundPackets1_8.CONTAINER_SET_DATA, new PacketHandlers() {
            public void register() {
                this.map(Types.UNSIGNED_BYTE);
                this.map(Types.SHORT);
                this.map(Types.SHORT);
                this.handler((wrapper) -> {
                    short windowId = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    short property = wrapper.get(Types.SHORT, 0);
                    short value = wrapper.get(Types.SHORT, 1);
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    if (Objects.requireNonNull(inventoryTracker).getInventory() != null && inventoryTracker.getInventory().equalsIgnoreCase("minecraft:enchanting_table") && property > 3 && property < 7) {
                        short level = (short)(value >> 8);
                        short enchantID = (short)(value & 255);
                        wrapper.create(wrapper.getId(), (propertyPacket) -> {
                            propertyPacket.write(Types.UNSIGNED_BYTE, windowId);
                            propertyPacket.write(Types.SHORT, property);
                            propertyPacket.write(Types.SHORT, enchantID);
                        }).scheduleSend(Protocol1_8To1_9.class);
                        wrapper.set(Types.SHORT, 0, (short)(property + 3));
                        wrapper.set(Types.SHORT, 1, level);
                    }

                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_8.OPEN_SCREEN, new PacketHandlers() {
            public void register() {
                this.map(Types.UNSIGNED_BYTE);
                this.map(Types.STRING);
                this.map(Types.STRING, Protocol1_8To1_9.STRING_TO_JSON);
                this.map(Types.UNSIGNED_BYTE);
                this.handler((wrapper) -> {
                    String inventory = wrapper.get(Types.STRING, 0);
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    Objects.requireNonNull(inventoryTracker).setInventory(inventory);
                });
                this.handler((wrapper) -> {
                    String inventory = wrapper.get(Types.STRING, 0);
                    if (inventory.equals("minecraft:brewing_stand")) {
                        wrapper.set(Types.UNSIGNED_BYTE, 1, (short)(wrapper.get(Types.UNSIGNED_BYTE, 1) + 1));
                    }

                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_8.CONTAINER_SET_SLOT, new PacketHandlers() {
            public void register() {
                this.map(Types.UNSIGNED_BYTE);
                this.map(Types.SHORT);
                this.map(Types.ITEM1_8);
                this.handler((wrapper) -> {
                    Item stack = wrapper.get(Types.ITEM1_8, 0);
                    boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                    if (showShieldWhenSwordInHand) {
                        InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_8To1_9.class);
                        short slotID = wrapper.get(Types.SHORT, 0);
                        byte windowId = wrapper.get(Types.UNSIGNED_BYTE, 0).byteValue();
                        Objects.requireNonNull(inventoryTracker).setItemId(windowId, slotID, stack == null ? 0 : stack.identifier());
                        Objects.requireNonNull(entityTracker).syncShieldWithSword();
                    }

                    ItemPacketRewriter1_9.this.handleItemToClient(wrapper.user(), stack);
                });
                this.handler((wrapper) -> {
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    short slotID = wrapper.get(Types.SHORT, 0);
                    if (Objects.requireNonNull(inventoryTracker).getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand") && slotID >= 4) {
                        wrapper.set(Types.SHORT, 0, (short)(slotID + 1));
                    }

                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_8.CONTAINER_SET_CONTENT, new PacketHandlers() {
            public void register() {
                this.map(Types.UNSIGNED_BYTE);
                this.map(Types.ITEM1_8_SHORT_ARRAY);
                this.handler((wrapper) -> {
                    Item[] stacks = wrapper.get(Types.ITEM1_8_SHORT_ARRAY, 0);
                    Short windowId = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_8To1_9.class);
                    boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();

                    for(short i = 0; i < stacks.length; ++i) {
                        Item stack = stacks[i];
                        if (showShieldWhenSwordInHand) {
                            Objects.requireNonNull(inventoryTracker).setItemId(windowId, i, stack == null ? 0 : stack.identifier());
                        }

                        ItemPacketRewriter1_9.this.handleItemToClient(wrapper.user(), stack);
                    }

                    if (showShieldWhenSwordInHand) {
                        Objects.requireNonNull(entityTracker).syncShieldWithSword();
                    }

                });
                this.handler((wrapper) -> {
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    if (Objects.requireNonNull(inventoryTracker).getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand")) {
                        Item[] oldStack = wrapper.get(Types.ITEM1_8_SHORT_ARRAY, 0);
                        Item[] newStack = new Item[oldStack.length + 1];

                        for(int i = 0; i < newStack.length; ++i) {
                            if (i > 4) {
                                newStack[i] = oldStack[i - 1];
                            } else if (i != 4) {
                                newStack[i] = oldStack[i];
                            }
                        }

                        wrapper.set(Types.ITEM1_8_SHORT_ARRAY, 0, newStack);
                    }

                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_8.CONTAINER_CLOSE, new PacketHandlers() {
            public void register() {
                this.map(Types.UNSIGNED_BYTE);
                this.handler((wrapper) -> {
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    Objects.requireNonNull(inventoryTracker).setInventory(null);
                    inventoryTracker.resetInventory(wrapper.get(Types.UNSIGNED_BYTE, 0));
                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_8.MAP_ITEM_DATA, new PacketHandlers() {
            public void register() {
                this.map(Types.VAR_INT);
                this.map(Types.BYTE);
                this.handler((wrapper) -> wrapper.write(Types.BOOLEAN, true));
            }
        });
        this.protocol.registerServerbound(ServerboundPackets1_9.SET_CREATIVE_MODE_SLOT, new PacketHandlers() {
            public void register() {
                this.map(Types.SHORT);
                this.map(Types.ITEM1_8);
                this.handler((wrapper) -> {
                    Item stack = wrapper.get(Types.ITEM1_8, 0);
                    boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                    if (showShieldWhenSwordInHand) {
                        InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_8To1_9.class);
                        short slotID = wrapper.get(Types.SHORT, 0);
                        Objects.requireNonNull(inventoryTracker).setItemId((short)0, slotID, stack == null ? 0 : stack.identifier());
                        Objects.requireNonNull(entityTracker).syncShieldWithSword();
                    }

                    ItemPacketRewriter1_9.this.handleItemToServer(wrapper.user(), stack);
                });
                this.handler((wrapper) -> {
                    short slot = wrapper.get(Types.SHORT, 0);
                    boolean throwItem = slot == 45;
                    if (throwItem) {
                        wrapper.create(ClientboundPackets1_9.CONTAINER_SET_SLOT, (w) -> {
                            w.write(Types.UNSIGNED_BYTE, (short) 0);
                            w.write(Types.SHORT, slot);
                            w.write(Types.ITEM1_8, null);
                        }).send(Protocol1_8To1_9.class);
                        wrapper.set(Types.SHORT, 0, (short) -999);
                    }

                });
            }
        });
        this.protocol.registerServerbound(ServerboundPackets1_9.CONTAINER_CLICK, new PacketHandlers() {
            public void register() {
                this.map(Types.UNSIGNED_BYTE);
                this.map(Types.SHORT);
                this.map(Types.BYTE);
                this.map(Types.SHORT);
                this.map(Types.VAR_INT, Types.BYTE);
                this.map(Types.ITEM1_8);
                this.handler((wrapper) -> {
                    Item stack = wrapper.get(Types.ITEM1_8, 0);
                    if (Via.getConfig().isShowShieldWhenSwordInHand()) {
                        Short windowId = wrapper.get(Types.UNSIGNED_BYTE, 0);
                        byte mode = wrapper.get(Types.BYTE, 1);
                        short hoverSlot = wrapper.get(Types.SHORT, 0);
                        byte button = wrapper.get(Types.BYTE, 0);
                        InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        Objects.requireNonNull(inventoryTracker).handleWindowClick(wrapper.user(), windowId, mode, hoverSlot, button);
                    }

                    ItemPacketRewriter1_9.this.handleItemToServer(wrapper.user(), stack);
                });
                this.handler((wrapper) -> {
                    short windowID = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    short slot = wrapper.get(Types.SHORT, 0);
                    boolean throwItem = slot == 45 && windowID == 0;
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    if (Objects.requireNonNull(inventoryTracker).getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand")) {
                        if (slot == 4) {
                            throwItem = true;
                        }

                        if (slot > 4) {
                            wrapper.set(Types.SHORT, 0, (short)(slot - 1));
                        }
                    }

                    if (throwItem) {
                        wrapper.create(ClientboundPackets1_9.CONTAINER_SET_SLOT, (w) -> {
                            w.write(Types.UNSIGNED_BYTE, windowID);
                            w.write(Types.SHORT, slot);
                            w.write(Types.ITEM1_8, null);
                        }).scheduleSend(Protocol1_8To1_9.class);
                        wrapper.set(Types.BYTE, 0, (byte)0);
                        wrapper.set(Types.BYTE, 1, (byte)0);
                        wrapper.set(Types.SHORT, 0, (short) -999);
                    }

                });
            }
        });
        this.protocol.registerServerbound(ServerboundPackets1_9.CONTAINER_CLOSE, new PacketHandlers() {
            public void register() {
                this.map(Types.UNSIGNED_BYTE);
                this.handler((wrapper) -> {
                    InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    Objects.requireNonNull(inventoryTracker).setInventory(null);
                    inventoryTracker.resetInventory(wrapper.get(Types.UNSIGNED_BYTE, 0));
                });
            }
        });
        this.protocol.registerServerbound(ServerboundPackets1_9.SET_CARRIED_ITEM, new PacketHandlers() {
            public void register() {
                this.map(Types.SHORT);
                this.handler((wrapper) -> {
                    boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                    EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_8To1_9.class);
                    if (Objects.requireNonNull(entityTracker).isBlocking()) {
                        entityTracker.setBlocking(false);
                        if (!showShieldWhenSwordInHand) {
                            entityTracker.setSecondHand(null);
                        }
                    }

                    if (showShieldWhenSwordInHand) {
                        entityTracker.setHeldItemSlot(wrapper.get(Types.SHORT, 0));
                        entityTracker.syncShieldWithSword();
                    }

                });
            }
        });
    }

    private String validateAndConvertJson(String json) {
        try {
            return gson.toJson(gson.fromJson(json, Object.class));
        } catch (JsonSyntaxException e) {
            return e.getMessage();
        }
    }

    public @Nullable Item handleItemToClient(UserConnection connection, @Nullable Item item) {
        if (item == null) {
            return null;
        } else {
            CompoundTag tag;
            StringTag page;
            if (item.identifier() == 383 && item.data() != 0) {
                tag = item.tag();
                if (tag == null) {
                    tag = new CompoundTag();
                }

                CompoundTag entityTag = new CompoundTag();
                String entityName = EntityIds1_8.ENTITY_ID_TO_NAME.get((int) item.data());
                if (entityName != null) {
                    page = new StringTag(entityName);
                    entityTag.put("id", page);
                    tag.put("EntityTag", entityTag);
                }

                item.setTag(tag);
                item.setData((short)0);
            }

            if (item.identifier() == 373) {
                tag = item.tag();
                if (tag == null) {
                    tag = new CompoundTag();
                }

                if (item.data() >= 16384) {
                    item.setIdentifier(438);
                    item.setData((short)(item.data() - 8192));
                }

                String name = PotionIdMappings1_9.potionNameFromDamage(item.data());
                StringTag potion = new StringTag(Key.namespaced(name));
                tag.put("Potion", potion);
                item.setTag(tag);
                item.setData((short)0);
            }

            if (item.identifier() == 387) {
                tag = item.tag();
                if (tag == null) {
                    tag = new CompoundTag();
                }

                ListTag<StringTag> pages = tag.getListTag("pages", StringTag.class);
                if (pages == null) {
                    pages = new ListTag<>(StringTag.class);
                    pages.add(new StringTag(validateAndConvertJson(ComponentUtil.emptyJsonComponent().toString())));
                    tag.put("pages", pages);
                } else {
                    for (int i = 0; i < pages.size(); ++i) {
                        page = pages.get(i);
                        page.setValue(validateAndConvertJson(page.getValue()));
                    }
                }
                item.setTag(tag);
            }

            return item;
        }
    }

    public @Nullable Item handleItemToServer(UserConnection connection, @Nullable Item item) {
        if (item == null) {
            return null;
        } else {
            CompoundTag tag;
            int data;
            if (item.identifier() == 383 && item.data() == 0) {
                tag = item.tag();
                data = 0;
                if (tag != null && tag.getCompoundTag("EntityTag") != null) {
                    CompoundTag entityTag = tag.getCompoundTag("EntityTag");
                    StringTag id = Objects.requireNonNull(entityTag).getStringTag("id");
                    if (id != null && EntityIds1_8.ENTITY_NAME_TO_ID.containsKey(id.getValue())) {
                        data = EntityIds1_8.ENTITY_NAME_TO_ID.get(id.getValue());
                    }

                    tag.remove("EntityTag");
                }

                item.setTag(tag);
                item.setData((short)data);
            }

            StringTag potion;
            String potionName;
            if (item.identifier() == 373) {
                tag = item.tag();
                data = 0;
                if (tag != null && tag.getStringTag("Potion") != null) {
                    potion = tag.getStringTag("Potion");
                    potionName = Key.stripMinecraftNamespace(Objects.requireNonNull(potion).getValue());
                    if (PotionIdMappings1_9.POTION_NAME_TO_ID.containsKey(potionName)) {
                        data = PotionIdMappings1_9.POTION_NAME_TO_ID.get(potionName);
                    }

                    tag.remove("Potion");
                }

                item.setTag(tag);
                item.setData((short)data);
            }

            if (item.identifier() == 438) {
                tag = item.tag();
                data = 0;
                item.setIdentifier(373);
                if (tag != null && tag.getStringTag("Potion") != null) {
                    potion = tag.getStringTag("Potion");
                    potionName = Key.stripMinecraftNamespace(Objects.requireNonNull(potion).getValue());
                    if (PotionIdMappings1_9.POTION_NAME_TO_ID.containsKey(potionName)) {
                        data = PotionIdMappings1_9.POTION_NAME_TO_ID.get(potionName) + 8192;
                    }

                    tag.remove("Potion");
                }

                item.setTag(tag);
                item.setData((short)data);
            }

            if (item.identifier() == 387) {
                tag = item.tag();
                if (tag != null) {
                    ListTag<StringTag> pages = tag.getListTag("pages", StringTag.class);
                    if (pages != null) {
                        for (StringTag page : pages) {
                            page.setValue(validateAndConvertJson(page.getValue()));
                        }
                    }
                }
                item.setTag(tag);
            }

            boolean newItem = item.identifier() >= 198 && item.identifier() <= 212;
            newItem |= item.identifier() == 397 && item.data() == 5;
            newItem |= item.identifier() >= 432 && item.identifier() <= 448;
            if (newItem) {
                item.setIdentifier(1);
                item.setData((short)0);
            }

            return item;
        }
    }
}
