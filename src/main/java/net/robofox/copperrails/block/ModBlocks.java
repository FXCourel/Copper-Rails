package net.robofox.copperrails.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
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

    public static final Block COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL).mapColor(MapColor.COLOR_ORANGE)), "copper_rail");
    public static final Block EXPOSED_COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)), "exposed_copper_rail");
    public static final Block WEATHERED_COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL).mapColor(MapColor.WARPED_STEM)), "weathered_copper_rail");
    public static final Block OXIDIZED_COPPER_RAIL = register(new OxidizableCopperRailBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL).mapColor(MapColor.WARPED_NYLIUM)), "oxidized_copper_rail");
    public static final Block WAXED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.copy(ModBlocks.COPPER_RAIL)), "waxed_copper_rail");
    public static final Block WAXED_EXPOSED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.copy(ModBlocks.EXPOSED_COPPER_RAIL)), "waxed_exposed_copper_rail");
    public static final Block WAXED_WEATHERED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.copy(ModBlocks.WEATHERED_COPPER_RAIL)), "waxed_weathered_copper_rail");
    public static final Block WAXED_OXIDIZED_COPPER_RAIL = register(new GenericCopperRailBlock(BlockBehaviour.Properties.copy(ModBlocks.OXIDIZED_COPPER_RAIL)), "waxed_oxidized_copper_rail");
    public static final Block RAIL_CROSSING = register(new CrossingRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL)), "rail_crossing");

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
        ResourceLocation blockID = new ResourceLocation(CopperRails.MOD_ID, id);
        BlockItem blockItem = new BlockItem(block, new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, blockID, blockItem);
        return Registry.register(BuiltInRegistries.BLOCK, blockID, block);
    }
}