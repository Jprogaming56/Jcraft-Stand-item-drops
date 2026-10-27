package net.user.jcraftstanddrops;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> DROP_ITEM;
    public static final ForgeConfigSpec.DoubleValue DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue LOOTING_BONUS;
    public static final ForgeConfigSpec.IntValue MIN_COUNT;
    public static final ForgeConfigSpec.IntValue MAX_COUNT;
    public static final ForgeConfigSpec.BooleanValue REQUIRE_PLAYER_KILL;
    public static final ForgeConfigSpec.BooleanValue INCLUDE_PLAYERS;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("drops");

        DROP_ITEM = b.comment("Item dropped by stand users. Change to jcraft:stand_arrowhead if you want arrowheads instead.")
                .define("dropItem", "jcraft:stellar_iron_ingot");
        DROP_CHANCE = b.comment("Chance (0.0 - 1.0) that a killed stand user drops the item.")
                .defineInRange("dropChance", 0.5D, 0.0D, 1.0D);
        LOOTING_BONUS = b.comment("Extra drop chance added per Looting level.")
                .defineInRange("lootingBonusPerLevel", 0.1D, 0.0D, 1.0D);
        MIN_COUNT = b.comment("Minimum number of items dropped when the roll succeeds.")
                .defineInRange("minCount", 1, 1, 64);
        MAX_COUNT = b.comment("Maximum number of items dropped when the roll succeeds.")
                .defineInRange("maxCount", 1, 1, 64);
        REQUIRE_PLAYER_KILL = b.comment("Only drop when the kill was caused by a player (including their stand, bombs, clones and projectiles).")
                .define("requirePlayerKill", true);
        INCLUDE_PLAYERS = b.comment("Also drop from players who have a stand (PvP).")
                .define("includePlayers", false);

        b.pop();
        SPEC = b.build();
    }

    private Config() {}
}
