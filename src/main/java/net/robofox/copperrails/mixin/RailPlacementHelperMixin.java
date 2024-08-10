package net.robofox.copperrails.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailPlacementHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.robofox.copperrails.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RailPlacementHelper.class)
public class RailPlacementHelperMixin {

    @Unique
    private static boolean canMakeSlopes(World world, BlockPos blockPos, BlockPos blockPosCheckSlopes) {
        BlockState railBlockState = world.getBlockState(blockPos);
        BlockState railBlockStateCheckSlopes = world.getBlockState(blockPosCheckSlopes);
        return !railBlockStateCheckSlopes.isOf(ModBlocks.RAIL_CROSSING) && AbstractRailBlock.isRail(railBlockState);
    }

    /**
     * Mixin to check if original rail is allowed to be sloped (in this case crossing)
     * One function for each direction
     * For computeRailShape mixin
     */
    @Redirect(
            method = "computeRailShape",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 0
            )
    )
    private boolean isRailMixinComputeRailShapeNorth(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().south());
    }
    @Redirect(
            method = "computeRailShape",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 1
            )
    )
    private boolean isRailMixinComputeRailShapeSouth(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().north());
    }
    @Redirect(
            method = "computeRailShape",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 2
            )
    )
    private boolean isRailMixinComputeRailShapeEast(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().west());
    }

    @Redirect(
            method = "computeRailShape",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 3
            )
    )
    private boolean isRailMixinComputeRailShapeWest(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().east());
    }

    /**
     * Mixin to check if original rail is allowed to be sloped (in this case crossing)
     * One function for each direction
     * For updateBlockState mixin
     */
    @Redirect(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 0
            )
    )
    private boolean isRailMixinUpdateBlockStateNorth(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().south());
    }

    @Redirect(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 1
            )
    )
    private boolean isRailMixinUpdateBlockStateSouth(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().north());
    }

    @Redirect(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 2
            )
    )
    private boolean isRailMixinUpdateBlockStateEast(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().west());
    }

    @Redirect(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 3
            )
    )
    private boolean isRailMixinUpdateBlockStateWest(World world, BlockPos blockPos) {
        return canMakeSlopes(world, blockPos, blockPos.down().east());
    }

}
