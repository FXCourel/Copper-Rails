package net.robofox.copperrails;

public class CopperRailsConfig {

    // Config : Modify speeds (in blocks per second) here
    public static final int COPPER_SPEED_BPS = 16;
    public static final int EXPOSED_COPPER_SPEED_BPS = 12;
    public static final int WEATHERED_COPPER_SPEED_BPS = 6;
    public static final int OXIDIZED_COPPER_SPEED_BPS = 4;
    public static final int MAX_ASCENDING_SPEED_BPS = 10;
    public static final int GOLD_SPEED_BPS = 8;
    public static final int MAX_RAIL_SPEED_NOT_EXPERIMENTAL_BPS = 16;

    public static final int EXPERIMENTAL_RECOMMENDED_GOLD_SPEED_BPS = 48;
    public static final int EXPERIMENTAL_RECOMMENDED_COPPER_SPEED_BPS = 24;
    public static final int EXPERIMENTAL_RECOMMENDED_EXPOSED_COPPER_SPEED_BPS = 16;
    public static final int EXPERIMENTAL_RECOMMENDED_WEATHERED_COPPER_SPEED_BPS = 8;
    public static final int EXPERIMENTAL_RECOMMENDED_OXIDIZED_COPPER_SPEED_BPS = 4;

    // Do not touch (conversion below)
    private static float blockPerSecondToTick(int blockPerSecondSpeed) {
        return blockPerSecondSpeed / 20.0F;
    }

    public static final float COPPER_SPEED = blockPerSecondToTick(COPPER_SPEED_BPS);
    public static final float EXPOSED_COPPER_SPEED = blockPerSecondToTick(EXPOSED_COPPER_SPEED_BPS);
    public static final float WEATHERED_COPPER_SPEED = blockPerSecondToTick(WEATHERED_COPPER_SPEED_BPS);
    public static final float OXIDIZED_COPPER_SPEED = blockPerSecondToTick(OXIDIZED_COPPER_SPEED_BPS);
    public static final float MAX_ASCENDING_SPEED = blockPerSecondToTick(MAX_ASCENDING_SPEED_BPS);
    public static final float GOLD_SPEED = blockPerSecondToTick(GOLD_SPEED_BPS);
    public static final float MAX_RAIL_SPEED_NOT_EXPERIMENTAL = blockPerSecondToTick(MAX_RAIL_SPEED_NOT_EXPERIMENTAL_BPS);
}
