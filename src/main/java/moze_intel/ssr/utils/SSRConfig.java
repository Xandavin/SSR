package moze_intel.ssr.utils;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class SSRConfig {
    private static final short[] DEFAULT_MIN_KILLS = {64, 128, 256, 512, 1024};
    private static final byte[] DEFAULT_NUM_SPAWNS = {2, 4, 4, 4, 6};
    private static final byte[] DEFAULT_SPAWN_DELAY = {20, 10, 5, 5, 2};
    private static final boolean[] DEFAULT_NEEDS_PLAYER = {true, true, false, false, false};
    private static final boolean[] DEFAULT_CHECKS_LIGHT = {true, true, true, true, false};
    private static final boolean[] DEFAULT_CHECKS_WORLD = {true, true, true, false, false};
    private static final boolean[] DEFAULT_CHECKS_REDSTONE = {false, false, false, false, true};
    public static int ENCHANT_WEIGHT;
    public static int ENCHANT_KILL_BONUS;
    public static int SPAWNER_ABSORB_BONUS;
    public static int MAX_NUM_ENTITIES;
    public static boolean ALLOW_SPAWNER_ABSORB;
    public static boolean INVERT_REDSTONE;
    public static boolean ENABLE_FLOOD_PREVENTION;
    private static Configuration config;

    public static void init(File configFile) {
        config = new Configuration(configFile);

        try {
            config.load();

            ENCHANT_WEIGHT = config.getInt("Weight", "Soul Stealer Enchant", 8, 1, 10, "Soul-Stealer enchant probability");
            ENCHANT_KILL_BONUS = config.getInt("Kill Bonus", "Soul Stealer Enchant", 1, 1, 10, "Soul-Stealer kill bonus");
            SPAWNER_ABSORB_BONUS = config.getInt("Vanilla Spawner Bonus", "Misc", 200, 1, 400, "Amount of kills added to the shard when right-clicking a spawner");
            ALLOW_SPAWNER_ABSORB = config.getBoolean("Vanilla Spawner Absorbing", "Misc", true, "Allow absorbing of vanilla spawners for a kill bonus");
            INVERT_REDSTONE = config.getBoolean("Invert Redstone", "Misc", false, "Active redstone stops a soul cage");
            ENABLE_FLOOD_PREVENTION = config.getBoolean("Flood Prevention", "Misc", true, "Soul cages will stop when too many entities have been spawned");
            MAX_NUM_ENTITIES = config.getInt("Max Entities Spawned", "Misc", 80, 1, 200, "Max number of Entities soul cages can spawn in an area");

            short[] minKills = new short[5];

            for (int i = 0; i < 5; i++) {
                minKills[i] = (short) config.getInt("Min kills", "Tier " + (i + 1) + " settings", DEFAULT_MIN_KILLS[i], 1, 2048, "Minimum kills for the tier");

                TierHandler.setNumSpawns(i, (byte) config.getInt("Num Spawns", "Tier " + (i + 1) + " settings", DEFAULT_NUM_SPAWNS[i], 1, 10, "Number of spawns per operation"));
                TierHandler.setSpawnDelay(i, (byte) config.getInt("Cooldown", "Tier " + (i + 1) + " settings", DEFAULT_SPAWN_DELAY[i], 1, 60, "Cooldown time for soul cages (in seconds)"));
                TierHandler.setPlayerChecks(i, config.getBoolean("Check Player", "Tier " + (i + 1) + " settings", DEFAULT_NEEDS_PLAYER[i], "Needs a player nearby to spawn entities"));
                TierHandler.setLightChecks(i, config.getBoolean("Check Light", "Tier " + (i + 1) + " settings", DEFAULT_CHECKS_LIGHT[i], "Needs appropriate light to spawn entities"));
                TierHandler.setWorldChecks(i, config.getBoolean("Checks World", "Tier " + (i + 1) + " settings", DEFAULT_CHECKS_WORLD[i], "Needs appropriate world to spawn entities"));
                TierHandler.setRedstoneChecks(i, config.getBoolean("Redstone control", "Tier " + (i + 1) + " settings", DEFAULT_CHECKS_REDSTONE[i], "Reacts to a redstone signal"));
            }

            TierHandler.setTierReqKills(minKills);

            SSRLogger.logInfo("Loaded configuration file!");
        } catch (Exception e) {
            SSRLogger.logFatal("Failed to load configuration file!");
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}