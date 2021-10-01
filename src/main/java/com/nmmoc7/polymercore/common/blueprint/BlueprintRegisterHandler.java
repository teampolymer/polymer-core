package com.nmmoc7.polymercore.common.blueprint;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.blueprint.IBlueprint;
import com.nmmoc7.polymercore.common.RegisterHandler;
import com.nmmoc7.polymercore.common.blueprint.type.BlockBlueprintType;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class BlueprintRegisterHandler {
    private static final int MAX_VARINT = Integer.MAX_VALUE - 1;

    public static IForgeRegistry<IBlueprint> REGISTRY = new RegistryBuilder<IBlueprint>()
            .setName(new ResourceLocation(PolymerCore.MOD_ID, "blueprint"))
            .setType(IBlueprint.class).setMaxID(MAX_VARINT).disableSaving().create();
    public static DeferredRegister<IBlueprint> REGISTER = DeferredRegister.create(REGISTRY, PolymerCore.MOD_ID);

    public static final RegistryObject<IBlueprint> TEST_BLUEPRINT = REGISTER.register("test_blueprint",
            () -> new Blueprint(new String[][] {
                    {
                            "AAA",
                            "AAA",
                            "AAA"
                    },
                    {
                            "AAA",
                            "ACA",
                            "AAA"
                    }
            },
                    new Blueprint.Def('C', new BlockBlueprintType(RegisterHandler.BLUEPRINT_BLOCK)),
                    new Blueprint.Def('A', new BlockBlueprintType(Blocks.GOLD_BLOCK))
            )
    );
}
