package net.robofox.copperrails.mixin;

import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.block.custom.GenericCopperRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {

    @Redirect(
            method = "getRedstoneDirection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    public boolean isPoweringRail(BlockState state, Object block) {
        // This code is injected into the start of AbstractMinecartEntity.moveAlongTrack()V
        if (block == Blocks.POWERED_RAIL) {
            Block unknownRail = state.getBlock();
            // We want to check if this is a powering rail
            return (unknownRail instanceof GenericCopperRailBlock || unknownRail == Blocks.POWERED_RAIL);
        } else {
            CopperRails.LOGGER.warn("isOf() Mixin called with something else than Blocks.POWERED_RAIL");
            return state.is((Block) block);
        }
    }

}
