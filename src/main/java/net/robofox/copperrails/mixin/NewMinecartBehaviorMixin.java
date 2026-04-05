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

    /**
     * Returns the effective RailShape for a crossing block, flipping it when powered.
     * This mirrors the same fix in OldMinecartBehaviorMixin so that crossing direction
     * switching works with the experimental minecart physics engine too.
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

    /**
     * moveAlongTrack ordinal 0: first RailShape getValue call (byte offset 167).
     * Note: ordinal 0 of getValue overall is a Boolean (POWERED), which is skipped by type.
     * Mixin ordinal counts only calls matching the exact target descriptor, so ordinal 0 here
     * is the first RailShape cast getValue.
     */
    @Redirect(
            method = "moveAlongTrack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 0))
    public <T extends Comparable<T>> T getMoveAlongTrackShapeMixin0(BlockState blockState, Property<RailShape> property) {
        return (T) getRailShape(blockState, property);
    }

    /**
     * moveAlongTrack ordinal 1: second RailShape getValue call (byte offset 369).
     */
    @Redirect(
            method = "moveAlongTrack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 1))
    public <T extends Comparable<T>> T getMoveAlongTrackShapeMixin1(BlockState blockState, Property<RailShape> property) {
        return (T) getRailShape(blockState, property);
    }

    /**
     * adjustToRails ordinal 0: positions the cart on the rail after shape is determined.
     * Equivalent to getPos/getPosOffs in OldMinecartBehavior.
     */
    @Redirect(
            method = "adjustToRails",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 0))
    public <T extends Comparable<T>> T getAdjustToRailsMixin(BlockState blockState, Property<RailShape> property) {
        return (T) getRailShape(blockState, property);
    }

    @Unique
    private boolean isPoweringRail(BlockState state, Object block) {
        // This code is injected into the start of AbstractMinecartEntity.moveAlongTrack()V
        if (block == Blocks.POWERED_RAIL) {
            Block unknownRail = state.getBlock();
            // We want to check if this is a powering rail
            return (unknownRail instanceof GenericCopperRailBlock || unknownRail == Blocks.POWERED_RAIL);
        } else {
            CopperRails.LOGGER.warn("isOf() Mixin called with something else than Blocks.POWERED_RAIL");
            return state.is((net.minecraft.world.level.block.Block) block);
        }
    }

    @Redirect(
            method = "calculateHaltTrackSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    public boolean isPoweringRailHaltTrackSpeed(BlockState state, Object block) {
        return isPoweringRail(state, block);
    }

    @Redirect(
            method = "calculateBoostTrackSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    public boolean isPoweringRailBoostTrackSpeed(BlockState state, Object block) {
        return isPoweringRail(state, block);
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
        GameRules gamerules = server.getWorldData().getGameRules();
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



}