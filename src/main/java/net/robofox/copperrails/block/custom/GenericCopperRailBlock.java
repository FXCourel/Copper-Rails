package net.robofox.copperrails.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class GenericCopperRailBlock extends PoweredRailBlock {
    public GenericCopperRailBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean isSameRailWithPower(Level world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (!GenericCopperRailBlock.class.isAssignableFrom(block.getClass())) {
            return false;
        } else {
            RailShape railShape = blockState.getValue(SHAPE);
            if (shape != RailShape.EAST_WEST || railShape != RailShape.NORTH_SOUTH && railShape != RailShape.ASCENDING_NORTH && railShape != RailShape.ASCENDING_SOUTH) {
                if (shape != RailShape.NORTH_SOUTH || railShape != RailShape.EAST_WEST && railShape != RailShape.ASCENDING_EAST && railShape != RailShape.ASCENDING_WEST) {
                    if (!(Boolean)blockState.getValue(POWERED)) {
                        return false;
                    } else {
                        return world.hasNeighborSignal(pos) || this.findPoweredRailSignal(world, pos, blockState, bl, distance + 1);
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
