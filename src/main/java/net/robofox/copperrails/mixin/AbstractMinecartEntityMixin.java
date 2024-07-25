package net.robofox.copperrails.mixin;

import net.minecraft.block.*;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.CopperRailsConfig;
import net.robofox.copperrails.block.ModBlocks;
import net.robofox.copperrails.block.custom.GenericCopperRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin {

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
	public boolean isPoweringRail(BlockState state, Block block) {
		// This code is injected into the start of AbstractMinecartEntity.moveOnRail()V
		if (block == Blocks.POWERED_RAIL) {
			Block unknownRail = state.getBlock();
			// We want to check if this is a powering rail
			return (unknownRail instanceof GenericCopperRailBlock || unknownRail == Blocks.POWERED_RAIL);
		} else {
			CopperRails.LOGGER.warn("isOf() Mixin called with something else than Blocks.POWERED_RAIL");
			return state.isOf(block);
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
		AbstractMinecartEntity minecart = (AbstractMinecartEntity) (Object) this;
		return (minecart.isTouchingWater() ? CopperRailsConfig.NORMAL_RAIL_SPEED / 2.0 : CopperRailsConfig.NORMAL_RAIL_SPEED);
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
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 9))
	public void setVelocityClamp(AbstractMinecartEntity minecart, Vec3d velocity) {
		double maxSpeed = getMaxRailSpeed(minecart.getBlockStateAtPos());
		minecart.setVelocity(convergeAbs(velocity.x, maxSpeed), velocity.y, convergeAbs(velocity.z, maxSpeed));
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 0))
	public void setVelocityAscendingEast(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.x > CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_x = CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 1))
	public void setVelocityAscendingWest(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.x < - CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_x = - CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 2))
	public void setVelocityAscendingNorth(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.z < - CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_z = - CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 3))
	public void setVelocityAscendingSouth(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.z > CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_z = CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
	}
}