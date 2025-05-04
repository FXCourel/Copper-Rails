package net.robofox.copperrails.mixin;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecartBehavior.class)
public abstract class MinecartBehaviorMixin {

	@Shadow
    protected final AbstractMinecart minecart;

    public MinecartBehaviorMixin(AbstractMinecart minecart) {
        this.minecart = minecart;
    }
}
