package net.robofox.copperrails.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockState;
import net.robofox.copperrails.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RailState.class)
public class RailStateMixin {

    @Unique
    private static boolean canMakeSlopes(Level world, BlockPos blockPos, BlockPos blockPosCheckSlopes, Operation<Boolean> original) {
        BlockState railBlockStateCheckSlopes = world.getBlockState(blockPosCheckSlopes);
        return !railBlockStateCheckSlopes.is(ModBlocks.RAIL_CROSSING) && original.call(world, blockPos);
    }

    /**
     * Mixin to check if original rail is allowed to be sloped (in this case crossing)
     * One function for each direction
     * For connectTo mixin
     */
    @WrapOperation(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0
            ))
    private boolean isRailMixinComputeRailShapeNorth(Level level, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(level, blockPos, blockPos.below().south(), original);
    }
    @WrapOperation(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 1
            )
    )
    private boolean isRailMixinComputeRailShapeSouth(Level world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.below().north(), original);
    }
    @WrapOperation(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 2
            )
    )
    private boolean isRailMixinComputeRailShapeEast(Level world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.below().west(), original);
    }

    @WrapOperation(
            method = "connectTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 3
            )
    )
    private boolean isRailMixinComputeRailShapeWest(Level world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.below().east(), original);
    }

    /**
     * Mixin to check if original rail is allowed to be sloped (in this case crossing)
     * One function for each direction
     * For place mixin
     */
    @WrapOperation(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0
            )
    )
    private boolean isRailMixinUpdateBlockStateNorth(Level world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.below().south(), original);
    }

    @WrapOperation(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 1
            )
    )
    private boolean isRailMixinUpdateBlockStateSouth(Level world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.below().north(), original);
    }

    @WrapOperation(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 2
            )
    )
    private boolean isRailMixinUpdateBlockStateEast(Level world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.below().west(), original);
    }

    @WrapOperation(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 3
            )
    )
    private boolean isRailMixinUpdateBlockStateWest(Level world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.below().east(), original);
    }

}