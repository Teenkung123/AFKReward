package com.teenkung.afkreward.Loader;

import com.teenkung.afkreward.AFKReward;
import com.teenkung.afkreward.Utils.OptionType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

import static com.teenkung.afkreward.AFKReward.colorize;

public class ConfigLoader {

    private static OptionType type;
    private static String worldName;
    private static String regionName;
    private static final ArrayList<String> idList = new ArrayList<>();
    private static final HashMap<String, Integer> rewardTime = new HashMap<>();
    private static final HashMap<String, Boolean> rewardRepeat = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> rewardCommand = new HashMap<>();

    public static OptionType getType() { return type; }
    public static String getWorldName() { return worldName; }
    public static String getRegionName() { return regionName; }
    public static ArrayList<String> getIdList() { return idList; }
    public static int getRewardTime(String id) { return rewardTime.getOrDefault(id, 2000000000); }
    public static boolean getRewardRepeat(String id) { return rewardRepeat.getOrDefault(id,false); }
    public static ArrayList<String> getRewardCommand(String id) { return rewardCommand.getOrDefault(id, new ArrayList<>()); }


    public static void generateConfig() {
        AFKReward instance = AFKReward.getInstance();
        FileConfiguration config = instance.getConfig();
        config.options().copyDefaults();
        instance.saveDefaultConfig();
    }

    public static void loadConfig() {
        System.out.println(colorize("&aLoading configuration"));
        AFKReward instance = AFKReward.getInstance();
        FileConfiguration config = instance.getConfig();
        type = OptionType.fromValue(config.getString("option.type", "region").toLowerCase());
        if (type == null) {
            System.out.println(colorize("&4Invalid Option Type! Please check spelling at options.type!"));
            Bukkit.getPluginManager().disablePlugin(instance);
            return;
        }
        worldName = config.getString("option.world.name", "AFKReward");
        regionName = config.getString("option.region.name", "AFKReward");

        WorldGuardLoader.clearWorldGuard();
        idList.clear();
        rewardTime.clear();
        rewardRepeat.clear();
        rewardCommand.clear();

        ConfigurationSection rewardSection = config.getConfigurationSection("rewards");
        if (rewardSection!= null) {
            for (String id : rewardSection.getKeys(false)) {
                idList.add(id);
                String path = "rewards." + id + ".";
                if (config.getInt(path+"time") > 0) {
                    rewardTime.put(id, config.getInt(path+"time"));
                } else {
                    rewardTime.put(id, 2000000000);
                }
                rewardRepeat.put(id, config.getBoolean(path+"repeat", false));
                rewardCommand.put(id, new ArrayList<>(config.getStringList(path+"rewards")));

            }
            System.out.println(colorize("&aLoaded configuration"));
        } else {
            System.out.println(colorize("&4Could not load Configuration File! Something went wrong!"));
            Bukkit.getPluginManager().disablePlugin(instance);
        }
        WorldGuardLoader.loadWorldGuard();



    }

}
