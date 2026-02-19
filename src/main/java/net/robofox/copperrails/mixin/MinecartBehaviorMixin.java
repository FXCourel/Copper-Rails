package net.robofox.copperrails.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRules;
import net.robofox.copperrails.CopperRailsConfig;
import net.robofox.copperrails.block.ModBlocks;
import net.robofox.copperrails.gamerules.CopperRailsGamerules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecartBehavior.class)
public abstract class MinecartBehaviorMixin {

	@Shadow
    protected final AbstractMinecart minecart;

    @Shadow public abstract Level level();

    @Shadow public abstract void setPos(double d, double e, double f);

    @Unique
    public int getMaxSpeedByRailType(Block block, GameRules gamerules){
        int answer = CopperRailsConfig.MAX_RAIL_SPEED_NOT_EXPERIMENTAL_BPS;
        if (block == ModBlocks.COPPER_RAIL || block == ModBlocks.WAXED_COPPER_RAIL) {
            answer = gamerules.get(CopperRailsGamerules.MAX_MINECART_SPEED_COPPER);
        } else if (block == ModBlocks.EXPOSED_COPPER_RAIL || block == ModBlocks.WAXED_EXPOSED_COPPER_RAIL) {
            answer = gamerules.get(CopperRailsGamerules.MAX_MINECART_SPEED_COPPER_EXPOSED);
        } else if (block == ModBlocks.WEATHERED_COPPER_RAIL || block == ModBlocks.WAXED_WEATHERED_COPPER_RAIL) {
            answer = gamerules.get(CopperRailsGamerules.MAX_MINECART_SPEED_COPPER_WEATHERED);
        } else if (block == ModBlocks.OXIDIZED_COPPER_RAIL || block == ModBlocks.WAXED_OXIDIZED_COPPER_RAIL) {
            answer = gamerules.get(CopperRailsGamerules.MAX_MINECART_SPEED_COPPER_OXIDIZED);
        } else if (block == Blocks.POWERED_RAIL) {
            answer = gamerules.get(CopperRailsGamerules.MAX_MINECART_SPEED_GOLD);
        }
        return answer;
    }


    public MinecartBehaviorMixin(AbstractMinecart minecart) {
        this.minecart = minecart;
    }
}
