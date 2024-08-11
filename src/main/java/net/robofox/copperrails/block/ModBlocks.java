package net.robofox.copperrails.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.block.custom.CrossingRailBlock;
import net.robofox.copperrails.block.custom.GenericCopperRailBlock;
import net.robofox.copperrails.block.custom.OxidizableCopperRailBlock;

public class ModBlocks {

    public static final Block COPPER_RAIL = register(new OxidizableCopperRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.copy(Blocks.POWERED_RAIL).mapColor(MapColor.ORANGE)), "copper_rail");
    public static final Block EXPOSED_COPPER_RAIL = register(new OxidizableCopperRailBlock(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.copy(Blocks.POWERED_RAIL).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)), "exposed_copper_rail");
    public static final Block WEATHERED_COPPER_RAIL = register(new OxidizableCopperRailBlock(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.copy(Blocks.POWERED_RAIL).mapColor(MapColor.DARK_AQUA)), "weathered_copper_rail");
    public static final Block OXIDIZED_COPPER_RAIL = register(new OxidizableCopperRailBlock(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.copy(Blocks.POWERED_RAIL).mapColor(MapColor.TEAL)), "oxidized_copper_rail");
    public static final Block WAXED_COPPER_RAIL = register(new GenericCopperRailBlock(AbstractBlock.Settings.copy(ModBlocks.COPPER_RAIL)), "waxed_copper_rail");
    public static final Block WAXED_EXPOSED_COPPER_RAIL = register(new GenericCopperRailBlock(AbstractBlock.Settings.copy(ModBlocks.EXPOSED_COPPER_RAIL)), "waxed_exposed_copper_rail");
    public static final Block WAXED_WEATHERED_COPPER_RAIL = register(new GenericCopperRailBlock(AbstractBlock.Settings.copy(ModBlocks.WEATHERED_COPPER_RAIL)), "waxed_weathered_copper_rail");
    public static final Block WAXED_OXIDIZED_COPPER_RAIL = register(new GenericCopperRailBlock(AbstractBlock.Settings.copy(ModBlocks.OXIDIZED_COPPER_RAIL)), "waxed_oxidized_copper_rail");
    public static final Block RAIL_CROSSING = register(new CrossingRailBlock(AbstractBlock.Settings.copy(Blocks.POWERED_RAIL)), "rail_crossing");

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
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE)
                .register((itemGroup) -> itemGroup.add(block.asItem()));
    }

    private static Block register(Block block, String id) {
        Identifier blockID = Identifier.of(CopperRails.MOD_ID, id);
        BlockItem blockItem = new BlockItem(block, new Item.Settings());
        Registry.register(Registries.ITEM, blockID, blockItem);
        return Registry.register(Registries.BLOCK, blockID, block);
    }
}
