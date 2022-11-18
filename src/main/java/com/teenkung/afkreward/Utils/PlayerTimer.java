package com.teenkung.afkreward.Utils;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.teenkung.afkreward.AFKReward;
import com.teenkung.afkreward.Loader.WorldGuardLoader;
import com.teenkung.afkreward.Loader.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerTimer {

    private static final HashMap<Player, Integer> playerTimer = new HashMap<>();

    //Method used to start Timer when plugin loaded
    public static void startTimer() {
        Bukkit.getScheduler().runTaskTimer(AFKReward.getInstance(), PlayerTimer::executeTask, 20, 20);
    }

    //Method used to remove Player from Timer
    public static void removePlayer(Player player) {
        playerTimer.remove(player);
    }
    public static int getTimer(Player player) { return playerTimer.getOrDefault(player, 0); }


    private static void executeTask() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isInAllAFKRegion(player)) {
                if (playerTimer.containsKey(player)) {
                    playerTimer.replace(player, playerTimer.get(player) + 1);
                } else {
                    playerTimer.put(player, 0);
                }

                for (String id : ConfigLoader.getIdList()) {
                    if (isInAFKRegion(player, id)) {
                        if (ConfigLoader.getRewardTime(id) != -1) {
                            if (ConfigLoader.getRewardRepeat(id)) {
                                if (playerTimer.get(player) % ConfigLoader.getRewardTime(id) == 0) {
                                    executeReward(player, id);
                                }
                            } else {
                                if (playerTimer.get(player) == ConfigLoader.getRewardTime(id)) {
                                    executeReward(player, id);
                                }
                            }
                        }
                    }
                }
            } else {
                if (playerTimer.containsKey(player) && !isInAllAFKRegion(player)) {
                    playerTimer.remove(player);
                }
            }
        }
    }

    public static boolean isInAFKRegion(Player player, String id) {
        if (otherPluginCheck(player)) {
            if (ConfigLoader.getType() == OptionType.REGION) {
                for (String name : ConfigLoader.getApplyRegions(id)) {
                    ProtectedRegion pr = WorldGuardLoader.getRegionManager(player.getWorld()).getRegion(name);
                    if (pr != null) {
                        return pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                    }
                }
            } else if (ConfigLoader.getType() == OptionType.WORLD) {
                return ConfigLoader.getApplyWorlds(id).contains(player.getWorld().getName());
            } else if (ConfigLoader.getType() == OptionType.BOTH) {
                if (ConfigLoader.getApplyWorlds(id).contains(player.getWorld().getName())) {
                    return ConfigLoader.getApplyWorlds(id).contains(player.getWorld().getName());
                } else {
                    for (String name : ConfigLoader.getApplyRegions(id)) {
                        ProtectedRegion pr = WorldGuardLoader.getRegionManager(player.getWorld()).getRegion(name);
                        if (pr != null) {
                            return pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                        }
                    }
                }

            }
        }
        return false;
    }

    public static boolean isInAllAFKRegion(Player player) {
        if (ConfigLoader.getType() == OptionType.REGION) {
            for (String name : ConfigLoader.getRegionName()) {
                ProtectedRegion pr = WorldGuardLoader.getRegionManager(player.getWorld()).getRegion(name);
                if (pr != null) {
                    return pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                }
            }
        } else if (ConfigLoader.getType() == OptionType.WORLD){
            return ConfigLoader.getWorldName().contains(player.getWorld().getName());
        } else if (ConfigLoader.getType() == OptionType.BOTH) {
            if (ConfigLoader.getWorldName().contains(player.getWorld().getName())) {
                return ConfigLoader.getWorldName().contains(player.getWorld().getName());
            } else {
                for (String name : ConfigLoader.getRegionName()) {
                    ProtectedRegion pr = WorldGuardLoader.getRegionManager(player.getWorld()).getRegion(name);
                    if (pr != null) {
                        return pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                    }
                }
            }

        }
        return false;
    }

    private static void executeReward(Player player, String id) {
        for (String command :ConfigLoader.getRewardCommand(id)) {
            command = command
                    .replaceAll("<seconds>", String.valueOf(playerTimer.getOrDefault(player, 0)))
                    .replaceAll("<second>", String.valueOf(playerTimer.getOrDefault(player, 0) % 60))
                    .replaceAll("<minute>", String.valueOf(Math.floor(playerTimer.getOrDefault(player, 0) / 60D)))
                    .replaceAll("<hour>", String.valueOf(Math.floor(playerTimer.getOrDefault(player, 0) / 3600D)))
                    .replaceAll("<player>", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public static void setTimer(Player target, int seconds) {
        playerTimer.put(target, seconds);
    }

    private static boolean otherPluginCheck(Player player) {
        if (ConfigLoader.getUseCMI()) {
            CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
            if (user != null) {
                Long afkTime = user.getAfkInfo().getAfkFrom();
                return afkTime != null;
            }
            return false;
        }
        return true;
    }
}
