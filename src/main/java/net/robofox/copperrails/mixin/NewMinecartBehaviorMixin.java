package net.robofox.copperrails.mixin;

import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NewMinecartBehavior.class)
public abstract class NewMinecartBehaviorMixin extends MinecartBehaviorMixin {

    protected NewMinecartBehaviorMixin(AbstractMinecart minecart) {
        super(minecart);
    }


}