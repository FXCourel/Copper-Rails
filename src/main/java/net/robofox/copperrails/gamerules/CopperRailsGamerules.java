package net.robofox.copperrails.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder.IntegerRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.robofox.copperrails.CopperRails;
import net.robofox.copperrails.CopperRailsConfig;


public class CopperRailsGamerules {

    public static GameRuleCategory RAILS_CATEGORY;
    public static GameRule<Integer> MAX_MINECART_SPEED_GOLD;
    public static GameRule<Integer> MAX_MINECART_SPEED_COPPER;
    public static GameRule<Integer> MAX_MINECART_SPEED_COPPER_EXPOSED;
    public static GameRule<Integer> MAX_MINECART_SPEED_COPPER_WEATHERED;
    public static GameRule<Integer> MAX_MINECART_SPEED_COPPER_OXIDIZED;
    public static GameRule<Double> MINECART_ASCEND_SLOWDOWN;

    private static GameRule<Integer> createRailGamerule(String name, int defaultValue) {
        return IntegerRuleBuilder.forInteger(defaultValue)
                .range(1, 1000)
                .category(RAILS_CATEGORY)
                .buildAndRegister(Identifier.fromNamespaceAndPath(CopperRails.MOD_ID, name));
    }

//    private static GameRule<Double> createExperimentalRailGamerule(String name, double defaultValue) {
//        return GameRuleBuilder.DoubleRuleBuilder.forDouble(defaultValue)
//                .range(0., 1.)
//                .category(RAILS_CATEGORY)
//                .requiredFeatures(FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS))
//                .buildAndRegister(Identifier.fromNamespaceAndPath(CopperRails.MOD_ID, name));
//    }

    static {
        RAILS_CATEGORY = GameRuleCategory.register(Identifier.fromNamespaceAndPath(CopperRails.MOD_ID, "rail_speeds"));
        MAX_MINECART_SPEED_GOLD = createRailGamerule("max_minecart_speed_gold", CopperRailsConfig.EXPERIMENTAL_RECOMMENDED_GOLD_SPEED_BPS);
        MAX_MINECART_SPEED_COPPER = createRailGamerule("max_minecart_speed_copper", CopperRailsConfig.EXPERIMENTAL_RECOMMENDED_COPPER_SPEED_BPS);
        MAX_MINECART_SPEED_COPPER_EXPOSED = createRailGamerule("max_minecart_speed_copper_exposed", CopperRailsConfig.EXPERIMENTAL_RECOMMENDED_EXPOSED_COPPER_SPEED_BPS);
        MAX_MINECART_SPEED_COPPER_WEATHERED = createRailGamerule("max_minecart_speed_copper_weathered", CopperRailsConfig.EXPERIMENTAL_RECOMMENDED_WEATHERED_COPPER_SPEED_BPS);
        MAX_MINECART_SPEED_COPPER_OXIDIZED = createRailGamerule("max_minecart_speed_copper_oxidized", CopperRailsConfig.EXPERIMENTAL_RECOMMENDED_OXIDIZED_COPPER_SPEED_BPS);
        // MINECART_ASCEND_SLOWDOWN = createExperimentalRailGamerule("minecart_ascend_slowdown", CopperRailsConfig.MINECART_ASCEND_SLOWDOWN_FACTOR);
    }
}
