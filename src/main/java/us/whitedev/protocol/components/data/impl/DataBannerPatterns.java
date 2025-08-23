package us.whitedev.protocol.components.data.impl;

import io.netty.buffer.ByteBuf;
import us.whitedev.protocol.components.data.DataComponent;
import us.whitedev.protocol.packets.PacketCodec;

import java.util.List;
import java.util.Map;

public class DataBannerPatterns implements DataComponent {
    private final List<BannerPattern> patternList;

    public DataBannerPatterns(List<BannerPattern> patternList) {
        this.patternList = patternList;
    }

    @Override
    public Map<Integer, Integer> getIds() {
        return Map.of(
                766, 48, 767, 49,
                768, 59, 769, 59
        );
    }

    @Override
    public void write(ByteBuf buf) {
        PacketCodec.writeList(buf, patternList, DataBannerPatterns::writeBannerPattern);
    }

    private static void writeBannerPattern(ByteBuf buf, BannerPattern bannerPattern){
        writePattern(buf, bannerPattern.pattern);
        PacketCodec.writeVarInt(buf, bannerPattern.color);
    }

    private static void writePattern(ByteBuf buf, Pattern pattern){
        PacketCodec.writeVarInt(buf, 0);
        PacketCodec.writeUtf(buf, pattern.assetsId);
        PacketCodec.writeUtf(buf, pattern.key);
    }

    public record BannerPattern(Pattern pattern, int color){}
    public record Pattern(String assetsId, String key){}
}