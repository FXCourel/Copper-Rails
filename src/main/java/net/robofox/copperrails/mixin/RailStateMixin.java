package net.robofox.copperrails.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockState;
import net.robofox.copperrails.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RailState.class)
public class RailStateMixin {

    @Unique
    private static boolean canMakeSlopes(Level world, BlockPos blockPos, BlockPos blockPosCheckSlopes) {
        BlockState railBlockState = world.getBlockState(blockPos);
        BlockState railBlockStateCheckSlopes = world.getBlockState(blockPosCheckSlopes);
        return !railBlockStateCheckSlopes.is(ModBlocks.RAIL_CROSSING) && BaseRailBlock.isRail(railBlockState);
    }

    /**
     * Mixin to check if original rail is allowed to be sloped (in this case crossing)
     * One function for each direction
     * For connectTo mixin
     */
    @Redirect(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0
            )
    )
    private boolean isRailMixinComputeRailShapeNorth(Level level, BlockPos blockPos) {
        return canMakeSlopes(level, blockPos, blockPos.below().south());
    }
    @Redirect(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 1
            )
    )
    private boolean isRailMixinComputeRailShapeSouth(Level world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.below().north());
    }
    @Redirect(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 2
            )
    )
    private boolean isRailMixinComputeRailShapeEast(Level world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.below().west());
    }

    @Redirect(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 3
            )
    )
    private boolean isRailMixinComputeRailShapeWest(Level world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.below().east());
    }

    /**
     * Mixin to check if original rail is allowed to be sloped (in this case crossing)
     * One function for each direction
     * For place mixin
     */
    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0
            )
    )
    private boolean isRailMixinUpdateBlockStateNorth(Level world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.below().south());
    }

    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 1
            )
    )
    private boolean isRailMixinUpdateBlockStateSouth(Level world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.below().north());
    }

    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 2
            )
    )
    private boolean isRailMixinUpdateBlockStateEast(Level world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.below().west());
    }

    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 3
            )
    )
    private boolean isRailMixinUpdateBlockStateWest(Level world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.below().east());
    }

}
