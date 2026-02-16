package net.robofox.copperrails;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.robofox.copperrails.block.ModBlocks;

public class CopperRailsClient implements ClientModInitializer {

    private static void cutout(Block block) {
//        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT);
    }

    private static void initializeResourcePack() {
        Identifier id = Identifier.fromNamespaceAndPath(CopperRails.MOD_ID, "copperrails3d");
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(CopperRails.MOD_ID).orElseThrow();
        ResourceLoader.registerBuiltinPack(id, modContainer, Component.nullToEmpty("CopperRails 3D Rails"), PackActivationType.NORMAL);
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
