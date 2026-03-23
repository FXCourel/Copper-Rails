package net.robofox.copperrails.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.CopperRailsConfig;
import net.robofox.copperrails.block.ModBlocks;
import net.robofox.copperrails.block.custom.GenericCopperRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NewMinecartBehavior.class)
public abstract class NewMinecartBehaviorMixin extends MinecartBehaviorMixin {

    protected NewMinecartBehaviorMixin(AbstractMinecart minecart) {
        super(minecart);
    }

    @Unique
    private boolean isPoweringRail(BlockState state, Block block) {
        // This code is injected into the start of AbstractMinecartEntity.moveAlongTrack()V
        if (block == Blocks.POWERED_RAIL) {
            Block unknownRail = state.getBlock();
            // We want to check if this is a powering rail
            return (unknownRail instanceof GenericCopperRailBlock || unknownRail == Blocks.POWERED_RAIL);
        } else {
            CopperRails.LOGGER.warn("isOf() Mixin called with something else than Blocks.POWERED_RAIL");
            return state.is(block);
        }
    }

    @Redirect(
            method = "calculateHaltTrackSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    public boolean isPoweringRailHaltTrackSpeed(BlockState state, Object block) {
        return isPoweringRail(state, (Block) block);
    }

    @Redirect(
            method = "calculateBoostTrackSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    public boolean isPoweringRailBoostTrackSpeed(BlockState state, Object block) {
        return isPoweringRail(state, (Block) block);
    }

//    @Inject(
//            method = "calculateBoostTrackSpeed",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/phys/Vec3;length()D",
//                    ordinal = 0)
//    )
//    private void

    @Unique
    public int getMaxRailSpeed(BlockState blockState) {
        Block block = blockState.getBlock();
        MinecraftServer server = this.level().getServer();
        if (server == null) {
            CopperRails.LOGGER.error("Could not access to server gamerules ! Please report this bug");
            return CopperRailsConfig.MAX_RAIL_SPEED_NOT_EXPERIMENTAL_BPS;
        }
        GameRules gamerules = server.getGameRules();
        int maxSpeed = getMaxSpeedByRailType(block, gamerules);
        return Integer.min(maxSpeed, gamerules.get(GameRules.MAX_MINECART_SPEED));
    }

        @ModifyVariable(
            method = "calculateBoostTrackSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D",
                    ordinal = 0),
            argsOnly = true)
    private Vec3 slowedDownVec3(Vec3 vec3, @Local(ordinal = 0, argsOnly = true) BlockState blockState) {
        // We already know that blockstate is a powered rail.
        float maxSpeed = getMaxRailSpeed(blockState) / 20.0F;
        if (vec3.length() > maxSpeed) {
            double d = Double.max(vec3.length() * 0.95 - 0.06, maxSpeed);
            vec3 = vec3.normalize().scale(d);
        }
        return vec3;
    }

    /**
     * Mixin to implement rail direction switching for crossing rail blocks
     */
    @Unique
    private RailShape getRailShape(BlockState blockState, Property<RailShape> property) {
        RailShape railShape = blockState.getValue(property);
        if (blockState.is(ModBlocks.RAIL_CROSSING)) {
            boolean isPowered = blockState.getValue(PoweredRailBlock.POWERED);
            if (isPowered) {
                switch (railShape) {
                    case NORTH_SOUTH:
                        return RailShape.EAST_WEST;
                    case EAST_WEST:
                        return RailShape.NORTH_SOUTH;
                    default:
                        CopperRails.LOGGER.error("Crossing rail has invalid shape");
                }
            }
        }
        return railShape;
    }

    @Redirect(
            method = "moveAlongTrack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 1))
    public <T extends Comparable<T>> T getMoveAlongTrackMixin(BlockState blockState, Property<RailShape> property) {
        return (T) getRailShape(blockState, property);
    }
    @Redirect(
            method = "stepAlongTrack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 0))
    public <T extends Comparable<T>> T getStepAlongTrackMixin(BlockState blockState, Property<RailShape> property) {
        return (T) getRailShape(blockState, property);
    }
    @Redirect(
            method = "adjustToRails",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 0))
    public <T extends Comparable<T>> T getAdjustToRailMixin(BlockState blockState, Property<RailShape> property) {
        return (T) getRailShape(blockState, property);
    }

}