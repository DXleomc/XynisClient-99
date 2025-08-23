package us.whitedev.protocol.components.objects;

import java.util.Map;

public enum ItemType {
    KNOWLEDGE_BOOK(Map.of(766,  1166, 767,  1166, 768,  1208, 769,  1218)),
    WRITABLE_BOOK(Map.of(766, 1091, 767, 1091, 768, 1132, 769, 1141)),
    WRITTEN_BOOK(Map.of(766, 1092, 767, 1092, 768, 1133, 769, 1142)),
    WHITE_BANNER(Map.of(766, 1133, 767, 1133, 768, 1175, 769, 1185)),
    BEEHIVE(Map.of(766,  1220, 767,  1223, 768,  1266, 769,  1276)),
    CHEST(Map.of(766,  299, 767,  299, 768,  311, 769,  313));

    private final Map<Integer, Integer> byVersion;

    ItemType(Map<Integer, Integer> byVersion) {
        this.byVersion = byVersion;
    }

    public int getId(int protocolVersion) {
        return byVersion.getOrDefault(protocolVersion, 0);
    }
}
