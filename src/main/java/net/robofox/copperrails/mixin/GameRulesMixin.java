package net.robofox.copperrails.mixin;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.gamerules.CopperRailsGamerules;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.level.gamerules.GameRules.class)
public abstract class GameRulesMixin {

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/gamerules/GameRules;registerInteger(Ljava/lang/String;Lnet/minecraft/world/level/gamerules/GameRuleCategory;IIILnet/minecraft/world/flag/FeatureFlagSet;)Lnet/minecraft/world/level/gamerules/GameRule;",
                    ordinal = 0
            )
    )
    private static GameRule<Integer> redirectMaxMinecartSpeed(String string, GameRuleCategory gameRuleCategory, int i, int j, int k, FeatureFlagSet featureFlagSet) {
        // Return a modified GameRule or a new instance
        CopperRails.LOGGER.debug("Went to redirect");
        if (CopperRailsGamerules.RAILS_CATEGORY == null) {
            throw new AssertionError("Rails category is null");
        }
        return GameRulesInvoker.callRegisterInteger(string, CopperRailsGamerules.RAILS_CATEGORY, i, j, k, featureFlagSet);
    }


}
