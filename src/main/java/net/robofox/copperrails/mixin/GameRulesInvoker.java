package net.robofox.copperrails.mixin;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.class)
public interface GameRulesInvoker {

    @Invoker("registerInteger")
    static GameRule<Integer> callRegisterInteger(String string, GameRuleCategory gameRuleCategory, int i, int j, int k, FeatureFlagSet featureFlagSet){
        throw new AssertionError("Mixin did not apply.");
    }
}
