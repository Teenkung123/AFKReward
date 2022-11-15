package com.teenkung.afkreward.Utils;

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

    //Method executed by startTimer Method
    private static void executeTask() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (isInAFKRegion(player)) {
                if (playerTimer.containsKey(player)) {
                    playerTimer.replace(player, playerTimer.get(player) + 1);
                    for (String id : ConfigLoader.getIdList()) {
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
                } else {
                    playerTimer.put(player, 0);
                }
            } else {
                if (playerTimer.containsKey(player)) {
                    removePlayer(player);
                }
            }
        }
    }

    private static boolean isInAFKRegion(Player player) {
        if (ConfigLoader.getType() == OptionType.REGION) {
            ProtectedRegion pr = WorldGuardLoader.getRegionManager(player.getWorld()).getRegion(ConfigLoader.getRegionName());
            if (pr != null) {
                return pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
            }
        } else {
            return player.getWorld().getName().equals(ConfigLoader.getWorldName());
        }
        return false;
    }

    private static void executeReward(Player player, String id) {
        for (String command :ConfigLoader.getRewardCommand(id)) {
            command = command.replaceAll("<seconds>", String.valueOf(playerTimer.get(player)));
            command = command.replaceAll("<minute>", String.valueOf(Math.floor(playerTimer.get(player) / 60D)));
            command = command.replaceAll("<hour>", String.valueOf(Math.floor(playerTimer.get(player) / 3600D)));
            command = command.replaceAll("<player>", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}