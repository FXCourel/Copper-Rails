package net.robofox.copperrails.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import net.minecraft.world.level.block.BaseRailBlock;
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
import net.robofox.copperrails.gamerules.CopperRailsGamerules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect; 

@Mixin(OldMinecartBehavior.class)
public abstract class OldMinecartBehaviorMixin extends MinecartBehaviorMixin {

	protected OldMinecartBehaviorMixin(AbstractMinecart minecart) {
		super(minecart);
	}

	/**
	 * Mixin to replace check if minecart should be propelled (if rail is powering rail)
	 */
	@Redirect(
			method = "moveAlongTrack",
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
		return Integer.min(maxSpeed, CopperRailsConfig.MAX_RAIL_SPEED_NOT_EXPERIMENTAL_BPS);
	}

	/**
	 * @author Robofox
	 * @reason Rewrite all getMaxSpeed (very short) to increase max speed
	 */
	@Overwrite
	public double getMaxSpeed(ServerLevel serverLevel) {
		return (this.minecart.isInWater() ? CopperRailsConfig.MAX_RAIL_SPEED_NOT_EXPERIMENTAL / 2.0 : CopperRailsConfig.MAX_RAIL_SPEED_NOT_EXPERIMENTAL);
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
					target = "Lnet/minecraft/world/entity/vehicle/minecart/OldMinecartBehavior;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 9))
	public void setVelocityClamp(OldMinecartBehavior minecart, Vec3 velocity) {
		double maxSpeed = getMaxRailSpeed(this.minecart.getInBlockState()) / 20.0F;
		minecart.setDeltaMovement(convergeAbs(velocity.x, maxSpeed), velocity.y, convergeAbs(velocity.z, maxSpeed));
	}

	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/minecart/OldMinecartBehavior;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 0))
	public void setVelocityAscendingEast(OldMinecartBehavior minecart, Vec3 velocity_adder) {
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
					target = "Lnet/minecraft/world/entity/vehicle/minecart/OldMinecartBehavior;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 1))
	public void setVelocityAscendingWest(OldMinecartBehavior minecart, Vec3 velocity_adder) {
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
					target = "Lnet/minecraft/world/entity/vehicle/minecart/OldMinecartBehavior;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 2))
	public void setVelocityAscendingNorth(OldMinecartBehavior minecart, Vec3 velocity_adder) {
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
					target = "Lnet/minecraft/world/entity/vehicle/minecart/OldMinecartBehavior;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
					ordinal = 3))
	public void setVelocityAscendingSouth(OldMinecartBehavior minecart, Vec3 velocity_adder) {
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

	@Redirect(
			method = "moveAlongTrack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
			)
	)
	public void accurateCollisionCheckOnMove(AbstractMinecart minecart, MoverType moverType, Vec3 vec32, @Local(ordinal = 0) RailShape railShape) {
		if (vec32.horizontalDistance() < 0.6) {
			minecart.move(moverType, vec32);
			return;
		}
		Vec3 destination = this.minecart.position().add(vec32);
		int i = Mth.floor(destination.x);
		int j = Mth.floor(destination.y);
		int k = Mth.floor(destination.z);
		BlockState destinationBlockState = this.level().getBlockState(new BlockPos(i, j, k));
		if (destinationBlockState.is(BlockTags.RAILS)) {
			RailShape destinationShape = destinationBlockState.getValue(((BaseRailBlock) destinationBlockState.getBlock()).getShapeProperty());
			if (destinationShape == RailShape.ASCENDING_EAST && vec32.x > 0.6 ||
					destinationShape == RailShape.ASCENDING_WEST && vec32.x < -0.6 ||
					destinationShape == RailShape.ASCENDING_SOUTH && vec32.z > 0.6 ||
					destinationShape == RailShape.ASCENDING_NORTH && vec32.z > -0.6
			) {
				this.setPos(this.minecart.getX(), this.minecart.getY() + 1, this.minecart.getZ());
			}
		}
		minecart.move(moverType, vec32);
	}
}