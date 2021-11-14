package com.nmmoc7.polymercore.common.multiblock.builder;

import com.google.gson.*;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.builder.IBasicMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.builder.ICharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.builder.IMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.builder.IPartListMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.common.multiblock.part.DefaultMultiblockPart;
import com.nmmoc7.polymercore.common.multiblock.part.DefaultPartChoice;
import com.nmmoc7.polymercore.common.multiblock.part.SingleChoicePart;
import com.nmmoc7.polymercore.common.utils.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibm.icu.impl.ValidIdentifiers.Datatype.x;

public class JsonMultiblockBuilder implements IBasicMultiblockBuilder {
    private final JsonElement element;

    public JsonMultiblockBuilder(String json) {
        this.element = new JsonParser().parse(json);
    }

    public JsonMultiblockBuilder(Reader reader) {
        this.element = new JsonParser().parse(reader);
    }

    private <T extends IMultiblockBuilder<T>> void readBasicProps(JsonObject obj, IMultiblockBuilder<T> builder) {
        if (obj.has("machine"))
            builder.machine(obj.getAsJsonPrimitive("machine").getAsString());
        if (obj.has("type")) {
            builder.type(obj.getAsJsonPrimitive("machine").getAsString());
        }
        if (obj.has("symmetrical")) {
            builder.allowSymmetrical(obj.getAsJsonPrimitive("symmetrical").getAsBoolean());
        }
        if (obj.has("tag")) {
            builder.addTags(obj.getAsJsonPrimitive("tag").getAsString());
        } else if (obj.has("tags")) {
            JsonArray tags = obj.getAsJsonArray("tags");
            for (JsonElement tag : tags) {
                builder.addTags(tag.getAsString());
            }
        }
        if (obj.has("limits")) {
            JsonArray limits = obj.getAsJsonArray("limits");
            for (JsonElement limit : limits) {
                JsonObject limitObj = limit.getAsJsonObject();
                String type = limitObj.getAsJsonPrimitive("type").getAsString();
                int min = -1;
                int max = -1;
                if (limitObj.has("min")) {
                    min = limitObj.getAsJsonPrimitive("min").getAsInt();
                }
                if (limitObj.has("max")) {
                    max = limitObj.getAsJsonPrimitive("max").getAsInt();
                }
                if (min >= 0 && max >= 0) {
                    builder.limit(type, min, max);
                }
            }
        }

    }


    private static class PartialPartInfo {
        public Integer x;
        public Integer y;
        public Integer z;
        public final List<IPartChoice> choices;

        public PartialPartInfo(Integer x, Integer y, Integer z, List<IPartChoice> choices) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.choices = choices;
        }

    }

    private void resolvePart(JsonObject partObj, IPartListMultiblockBuilder builder, PartialPartInfo parentInfo) {
        Integer x = null;
        Integer y = null;
        Integer z = null;
        List<IPartChoice> choices;
        if (parentInfo != null) {
            x = parentInfo.x;
            y = parentInfo.y;
            z = parentInfo.z;
            choices = parentInfo.choices;
        } else {
            choices = new ArrayList<>();
        }
        if (partObj.has("x"))
            x = partObj.getAsJsonPrimitive("x").getAsInt();
        if (partObj.has("y"))
            y = partObj.getAsJsonPrimitive("y").getAsInt();
        if (partObj.has("z"))
            z = partObj.getAsJsonPrimitive("z").getAsInt();

        if (partObj.has("choices")) {
            for (JsonElement element : partObj.getAsJsonArray("choices")) {
                IPartChoice choice = resolveChoice(element.getAsJsonObject());
                choices.add(choice);
            }
        } else if (partObj.has("choice")) {
            IPartChoice choice = resolveChoice(partObj.getAsJsonObject("choice"));
            choices.add(choice);
        }

        if (partObj.has("children")) {
            if (parentInfo == null) {
                parentInfo = new PartialPartInfo(x, y, z, choices);
            }
            JsonArray children = partObj.getAsJsonArray("children");
            for (JsonElement child : children) {
                JsonObject childObj = child.getAsJsonObject();
                resolvePart(childObj, builder, parentInfo);
            }
        } else {
            if (choices.isEmpty()) {
                throw new JsonParseException("Part did not have any choices");
            }
            if (x == null || y == null || z == null) {
                throw new JsonParseException("Part did not qualified offset position");
            }
            if (choices.size() == 1) {
                builder.part(x, y, z, new SingleChoicePart(choices.get(0)));
            } else {
                builder.part(x, y, z, new DefaultMultiblockPart(choices));
            }
        }

    }

    private void resolvePartMap(JsonObject partObj, ICharMarkedMultiblockBuilder builder) {
        for (Map.Entry<String, JsonElement> entry : partObj.entrySet()) {
            List<IPartChoice> choices = new ArrayList<>();
            String key = entry.getKey();
            if (key.length() != 1) {
                throw new JsonParseException("Pattern key must be single char");
            }
            JsonElement valueElement = entry.getValue();
            if (valueElement.isJsonArray()) {
                for (JsonElement element : valueElement.getAsJsonArray()) {
                    IPartChoice choice = resolveChoice(element.getAsJsonObject());
                    choices.add(choice);
                }
            } else {
                IPartChoice choice = resolveChoice(valueElement.getAsJsonObject());
                choices.add(choice);
            }
            if (choices.isEmpty()) {
                throw new JsonParseException("Part did not have any choices");
            }
            if (choices.size() == 1) {
                builder.part(key.charAt(0), new SingleChoicePart(choices.get(0)));
            } else {
                builder.part(key.charAt(0), new DefaultMultiblockPart(choices));
            }
        }


    }

    private final DefaultUnitFactory unitFactory = new DefaultUnitFactory();

    private IMultiblockUnit resolveUnit(JsonObject obj) {
        IMultiblockUnit unit = null;
        if (obj.has("block")) {
            JsonElement blockElement = obj.get("block");
            if (blockElement.isJsonObject()) {
                JsonObject blockObj = blockElement.getAsJsonObject();
                String name = blockObj.get("name").getAsString();
                Map<String, String> properties = new HashMap<>();
                JsonObject propObj = blockObj.getAsJsonObject("properties");
                for (Map.Entry<String, JsonElement> entry : propObj.entrySet()) {
                    properties.put(entry.getKey(), entry.getValue().getAsString());
                }
                unit = unitFactory.createByProperties(name, properties);
            } else {
                String block = blockElement.getAsString();
                unit = unitFactory.createByBlock(block);
            }
        } else if (obj.has("fluid")) {
            String fluid = obj.get("fluid").getAsString();
            unit = unitFactory.createByFluid(fluid);
        } else if (obj.has("tag")) {
            String tag = obj.get("tag").getAsString();
            unit = unitFactory.createByTag(tag);
        } else if (obj.has("elements")) {
            JsonArray elements = obj.getAsJsonArray("elements");
            List<IMultiblockUnit> children = new ArrayList<>();
            for (JsonElement element : elements) {
                IMultiblockUnit childUnit = resolveUnit(element.getAsJsonObject());
                if(childUnit != null) {
                    children.add(childUnit);
                }
            }
            unit = unitFactory.combine(children.toArray(new IMultiblockUnit[0]));
        }

        return unit;
    }

    private IPartChoice resolveChoice(JsonObject obj) {
        String type = null;
        if (obj.has("type")) {
            type = obj.getAsJsonPrimitive("type").getAsString();
        }
        IMultiblockUnit unit = resolveUnit(obj);
        boolean sample = obj.has("sample") && obj.get("sample").getAsBoolean();

        if (unit == null) {
            throw new JsonParseException("Could not detect choice part");
        }
        return new DefaultPartChoice(type, unit, sample);


    }


    @Override
    public IDefinedMultiblock build() {
        /*
         * 序列化json
         */
        if (!(element instanceof JsonObject)) {
            throw new JsonParseException("Multiblock json should be an object");
        }
        JsonObject obj = element.getAsJsonObject();
        String format = obj.get("format").getAsString();

        String registryName;
        if (obj.has("registryName")) {
            registryName = obj.get("registryName").getAsString();
        } else {
            registryName = obj.get("registry_name").getAsString();
        }

        switch (format) {
            case "charPattern":
            case "char_pattern": {
                ICharMarkedMultiblockBuilder builder = new DefaultCharMarkedMultiblockBuilder();
                readBasicProps(obj, builder);

                JsonObject parts = obj.getAsJsonObject("parts");
                resolvePartMap(parts, builder);

                JsonArray patterns = obj.getAsJsonArray("patterns");
                for (JsonElement patternLine : patterns) {
                    List<String> patternList = new ArrayList<>();
                    for (JsonElement pattern : patternLine.getAsJsonArray()) {
                        patternList.add(pattern.getAsString());
                    }
                    builder.patternLine(patternList.toArray(new String[0]));
                }

                char core = obj.getAsJsonPrimitive("core").getAsCharacter();
                builder.core(core);


                return builder.build().setRegistryName(new ResourceLocation(registryName));
            }

            case "partList":
            case "part_list": {
                IPartListMultiblockBuilder builder = new DefaultPartListMultiblockBuilder();
                readBasicProps(obj, builder);
                JsonArray parts = obj.getAsJsonArray("parts");
                for (JsonElement part : parts) {
                    JsonObject partObj = part.getAsJsonObject();
                    resolvePart(partObj, builder, null);
                }

                return builder.build().setRegistryName(new ResourceLocation(registryName));
            }
            default:
                throw new JsonParseException("Unsupported json format");
        }


    }

}
