package net.robofox.copperrails.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.block.custom.CrossingRailBlock;
import net.robofox.copperrails.block.custom.GenericCopperRailBlock;
import net.robofox.copperrails.block.custom.OxidizableCopperRailBlock;

public class ModBlocks {

//    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "waxed_oxidized_copper_rail")))

    public static final Block COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL).mapColor(MapColor.COLOR_ORANGE).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "copper_rail")))), "copper_rail");
    public static final Block EXPOSED_COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "exposed_copper_rail")))), "exposed_copper_rail");
    public static final Block WEATHERED_COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL).mapColor(MapColor.WARPED_STEM).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "weathered_copper_rail")))), "weathered_copper_rail");
    public static final Block OXIDIZED_COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL).mapColor(MapColor.WARPED_NYLIUM).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "oxidized_rail")))), "oxidized_copper_rail");
    public static final Block WAXED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.COPPER_RAIL).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "waxed_copper_rail")))), "waxed_copper_rail");
    public static final Block WAXED_EXPOSED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.EXPOSED_COPPER_RAIL).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "waxed_exposed_copper_rail")))), "waxed_exposed_copper_rail");
    public static final Block WAXED_WEATHERED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.WEATHERED_COPPER_RAIL).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "waxed_weathered_copper_rail")))), "waxed_weathered_copper_rail");
    public static final Block WAXED_OXIDIZED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.OXIDIZED_COPPER_RAIL).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "waxed_oxidized_copper_rail")))), "waxed_oxidized_copper_rail");
    public static final Block RAIL_CROSSING = register(new CrossingRailBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL).setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, "rail_crossing")))), "rail_crossing");

    public static void initialize() {
        putInRedstoneTab(ModBlocks.COPPER_RAIL);
        putInRedstoneTab(ModBlocks.EXPOSED_COPPER_RAIL);
        putInRedstoneTab(ModBlocks.WEATHERED_COPPER_RAIL);
        putInRedstoneTab(ModBlocks.OXIDIZED_COPPER_RAIL);
        putInRedstoneTab(ModBlocks.WAXED_COPPER_RAIL);
        putInRedstoneTab(ModBlocks.WAXED_EXPOSED_COPPER_RAIL);
        putInRedstoneTab(ModBlocks.WAXED_WEATHERED_COPPER_RAIL);
        putInRedstoneTab(ModBlocks.WAXED_OXIDIZED_COPPER_RAIL);
        putInRedstoneTab(ModBlocks.RAIL_CROSSING);

        // Register copper rails for oxidation
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.COPPER_RAIL, ModBlocks.EXPOSED_COPPER_RAIL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.EXPOSED_COPPER_RAIL, ModBlocks.WEATHERED_COPPER_RAIL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.WEATHERED_COPPER_RAIL, ModBlocks.OXIDIZED_COPPER_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.COPPER_RAIL, ModBlocks.WAXED_COPPER_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.EXPOSED_COPPER_RAIL, ModBlocks.WAXED_EXPOSED_COPPER_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.WEATHERED_COPPER_RAIL, ModBlocks.WAXED_WEATHERED_COPPER_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.OXIDIZED_COPPER_RAIL, ModBlocks.WAXED_OXIDIZED_COPPER_RAIL);
    }

    public static void putInRedstoneTab(Block block) {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS)
                .register((itemGroup) -> itemGroup.accept(block.asItem()));
    }

    private static Block register(Block block, String id) {
        ResourceLocation blockID = ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, id);
        BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CopperRails.MOD_ID, id))));
        Registry.register(BuiltInRegistries.ITEM, blockID, blockItem);
        return Registry.register(BuiltInRegistries.BLOCK, blockID, block);
    }
}
