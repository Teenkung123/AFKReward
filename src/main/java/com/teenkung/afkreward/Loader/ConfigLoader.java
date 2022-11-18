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
    private static ArrayList<String> worldName = new ArrayList<>();
    private static ArrayList<String> regionName = new ArrayList<>();
    private static final ArrayList<String> idList = new ArrayList<>();
    private static final HashMap<String, Integer> rewardTime = new HashMap<>();
    private static final HashMap<String, Boolean> rewardRepeat = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> rewardCommand = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> applyRegions = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> applyWorlds = new HashMap<>();
    private static boolean useCMI = false;

    public static OptionType getType() { return type; }
    public static ArrayList<String> getWorldName() { return worldName; }
    public static ArrayList<String> getRegionName() { return regionName; }
    public static ArrayList<String> getIdList() { return idList; }
    public static int getRewardTime(String id) { return rewardTime.getOrDefault(id, 2000000000); }
    public static boolean getRewardRepeat(String id) { return rewardRepeat.getOrDefault(id,false); }
    public static ArrayList<String> getRewardCommand(String id) { return rewardCommand.getOrDefault(id, new ArrayList<>()); }
    public static ArrayList<String> getApplyRegions(String id) { return applyRegions.getOrDefault(id, new ArrayList<>()); }
    public static ArrayList<String> getApplyWorlds(String id) { return applyWorlds.getOrDefault(id , new ArrayList<>()); }
    public static boolean getUseCMI() { return useCMI; }


    public static void generateConfig() {
        AFKReward instance = AFKReward.getInstance();
        FileConfiguration config = instance.getConfig();
        config.options().copyDefaults();
        instance.saveDefaultConfig();
    }

    public static String getMessage(String path, String def) {
        return AFKReward.getInstance().getConfig().getString("Language." + path, def)
            .replaceAll("<prefix>", colorize(AFKReward.getInstance().getConfig().getString("Languages.Prefix", "<GRADIENT:FBEA01>[AFK Rewards]</GRADIENT:00FDAA>&r ")));
    }
    public static void loadConfig(boolean reload) {
        System.out.println(colorize(getMessage("Loading", "<GRADIENT:E5FF67>Loading Configuration. . .</GRADIENT:FD85FC>")));
        AFKReward instance = AFKReward.getInstance();
        if (reload) {
            instance.reloadConfig();
            useCMI = false;
            WorldGuardLoader.clearWorldGuard();
            applyWorlds.clear();
            applyRegions.clear();
            worldName.clear();
            regionName.clear();
            idList.clear();
            rewardTime.clear();
            rewardRepeat.clear();
            rewardCommand.clear();
        }
        FileConfiguration config = instance.getConfig();
        type = OptionType.fromValue(config.getString("option.type", "region").toLowerCase());
        if (type == null) {
            System.out.println(colorize("&4Invalid Option Type! Please check spelling at options.type!"));
            Bukkit.getPluginManager().disablePlugin(instance);
            return;
        }
        worldName = new ArrayList<>(config.getStringList("options.world.name"));
        regionName = new ArrayList<>(config.getStringList("options.region.name"));

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
                if (config.isList(path+"apply-to.region")) {
                    applyRegions.put(id, new ArrayList<>(config.getStringList(path+"apply-to.region")));
                } else {
                    applyRegions.put(id, getRegionName());
                }
                if (config.isList(path+"apply-to.world")) {
                    applyWorlds.put(id, new ArrayList<>(config.getStringList(path+"apply-to.world")));
                } else {
                    applyRegions.put(id, getWorldName());
                }
            }
            System.out.println(colorize(getMessage("Loaded", "<GRADIENT:E5FF67>Successfully loaded configuration</GRADIENT:FD85FC>")));
        } else {
            System.out.println(colorize("&4Could not load Configuration File! Something went wrong!"));
            Bukkit.getPluginManager().disablePlugin(instance);
        }
        if (type == OptionType.REGION || type == OptionType.BOTH) {
            WorldGuardLoader.loadWorldGuard();
        }
        if (config.getBoolean("options.Interaction.CMI")) {
            useCMI = true;
        }


    }

}
