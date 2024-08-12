package net.robofox.copperrails;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.robofox.copperrails.block.ModBlocks;

public class CopperRailsClient implements ClientModInitializer {

    private static void cutout(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
    }

    private static void initializeResourcePack() {
        Identifier id = Identifier.of(CopperRails.MOD_ID, "copperrails3d");
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(CopperRails.MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(id, modContainer, Text.of("CopperRails 3D Rails"), ResourcePackActivationType.NORMAL);
    }

    @Override
    public void onInitializeClient() {
        cutout(ModBlocks.COPPER_RAIL);
        cutout(ModBlocks.EXPOSED_COPPER_RAIL);
        cutout(ModBlocks.WEATHERED_COPPER_RAIL);
        cutout(ModBlocks.OXIDIZED_COPPER_RAIL);
        cutout(ModBlocks.WAXED_COPPER_RAIL);
        cutout(ModBlocks.WAXED_EXPOSED_COPPER_RAIL);
        cutout(ModBlocks.WAXED_WEATHERED_COPPER_RAIL);
        cutout(ModBlocks.WAXED_OXIDIZED_COPPER_RAIL);
        cutout(ModBlocks.RAIL_CROSSING);
        initializeResourcePack();
    }
}
