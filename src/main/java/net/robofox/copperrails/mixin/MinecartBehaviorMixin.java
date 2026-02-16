package net.robofox.copperrails.mixin;

import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecartBehavior.class)
public abstract class MinecartBehaviorMixin {

	@Shadow
    protected final AbstractMinecart minecart;

    @Shadow public abstract Level level();

    @Shadow public abstract void setPos(double d, double e, double f);

    public MinecartBehaviorMixin(AbstractMinecart minecart) {
        this.minecart = minecart;
    }
}
