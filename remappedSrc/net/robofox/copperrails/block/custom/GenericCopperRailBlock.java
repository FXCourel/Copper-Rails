package net.robofox.copperrails.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenericCopperRailBlock extends PoweredRailBlock {
    public GenericCopperRailBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean isPoweredByOtherRails(World world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (!GenericCopperRailBlock.class.isAssignableFrom(block.getClass())) {
            return false;
        } else {
            RailShape railShape = blockState.get(SHAPE);
            if (shape != RailShape.EAST_WEST || railShape != RailShape.NORTH_SOUTH && railShape != RailShape.ASCENDING_NORTH && railShape != RailShape.ASCENDING_SOUTH) {
                if (shape != RailShape.NORTH_SOUTH || railShape != RailShape.EAST_WEST && railShape != RailShape.ASCENDING_EAST && railShape != RailShape.ASCENDING_WEST) {
                    if (!(Boolean)blockState.get(POWERED)) {
                        return false;
                    } else {
                        return world.isReceivingRedstonePower(pos) || this.isPoweredByOtherRails(world, pos, blockState, bl, distance + 1);
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

}
