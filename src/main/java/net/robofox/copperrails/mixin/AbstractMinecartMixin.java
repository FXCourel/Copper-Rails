package net.robofox.copperrails.mixin;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.CopperRailsConfig;
import net.robofox.copperrails.block.ModBlocks;
import net.robofox.copperrails.block.custom.GenericCopperRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {

	/**
	 * Mixin to replace check if minecart should be propelled (if rail is powering rail)
	 */
	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
	public boolean isPoweringRail(BlockState state, Block block) {
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

	@Unique
	public double getMaxRailSpeed(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block == ModBlocks.COPPER_RAIL || block == ModBlocks.WAXED_COPPER_RAIL) {
			return CopperRailsConfig.COPPER_SPEED;
		} else if (block == ModBlocks.EXPOSED_COPPER_RAIL || block == ModBlocks.WAXED_EXPOSED_COPPER_RAIL) {
			return CopperRailsConfig.EXPOSED_COPPER_SPEED;
		} else if (block == ModBlocks.WEATHERED_COPPER_RAIL || block == ModBlocks.WAXED_WEATHERED_COPPER_RAIL) {
			return CopperRailsConfig.WEATHERED_COPPER_SPEED;
		} else if (block == ModBlocks.OXIDIZED_COPPER_RAIL || block == ModBlocks.WAXED_OXIDIZED_COPPER_RAIL) {
			return CopperRailsConfig.OXIDIZED_COPPER_SPEED;
		} else if (block == Blocks.POWERED_RAIL) {
			return CopperRailsConfig.GOLD_SPEED;
		} else {
			// All other rails not boosting minecarts
			return CopperRailsConfig.NORMAL_RAIL_SPEED;
		}
	}

	/**
	 * @author Robofox
	 * @reason Rewrite all getMaxSpeed (very short) to increase max speed
	 */
	@Overwrite
	public double getMaxSpeed() {
		AbstractMinecart minecart = (AbstractMinecart) (Object) this;
		return (minecart.isInWater() ? CopperRailsConfig.NORMAL_RAIL_SPEED / 2.0 : CopperRailsConfig.NORMAL_RAIL_SPEED);
	}

	@Unique
	private double convergeAbs(double speed, double targetSpeed) {
		if (Math.abs(speed) > targetSpeed) {
			return Math.signum(speed) * Math.max(Math.abs(speed) * 0.7, targetSpeed);
		} else {
			return speed;
		}
	}

	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 9))
	public void setVelocityClamp(AbstractMinecart minecart, Vec3 velocity) {
		double maxSpeed = getMaxRailSpeed(minecart.level().getBlockState(minecart.blockPosition()));
		minecart.setDeltaMovement(convergeAbs(velocity.x, maxSpeed), velocity.y, convergeAbs(velocity.z, maxSpeed));
	}

	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 0))
	public void setVelocityAscendingEast(AbstractMinecart minecart, Vec3 velocity_adder) {
		Vec3 velocity = minecart.getDeltaMovement();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.x > CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_x = CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setDeltaMovement(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 1))
	public void setVelocityAscendingWest(AbstractMinecart minecart, Vec3 velocity_adder) {
		Vec3 velocity = minecart.getDeltaMovement();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.x < - CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_x = - CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setDeltaMovement(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 2))
	public void setVelocityAscendingNorth(AbstractMinecart minecart, Vec3 velocity_adder) {
		Vec3 velocity = minecart.getDeltaMovement();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.z < - CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_z = - CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setDeltaMovement(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 3))
	public void setVelocityAscendingSouth(AbstractMinecart minecart, Vec3 velocity_adder) {
		Vec3 velocity = minecart.getDeltaMovement();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.z > CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_z = CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setDeltaMovement(v_x, v_y, v_z);
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
			method = "getPosOffs",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
					ordinal = 0))
	public <T extends Comparable<T>> T getSnapPositionToRailWithOffsetMixin(BlockState blockState, Property<RailShape> property) {
		return (T) getRailShape(blockState, property);
	}
	@Redirect(
			method = "getPos",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
					ordinal = 0))
	public <T extends Comparable<T>> T getSnapPositionToRailMixin(BlockState blockState, Property<RailShape> property) {
		return (T) getRailShape(blockState, property);
	}
}