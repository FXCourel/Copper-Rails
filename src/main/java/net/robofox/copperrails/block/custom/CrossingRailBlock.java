package net.robofox.copperrails.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class CrossingRailBlock extends PoweredRailBlock {

    public CrossingRailBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean findPoweredRailSignal(Level world, BlockPos pos, BlockState state, boolean bl, int distance) {
        return false;
    }

    @Override
    protected boolean isSameRailWithPower(Level world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        return false;
    }

}
