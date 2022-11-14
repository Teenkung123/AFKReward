package com.teenkung.afkreward;

import org.bukkit.Bukkit;

public class timer {

    public static void startTimer() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(AFKReward.getInstance(), timer::executeTask, 20, 20);
    }

    private static void executeTask() {
        Bukkit.broadcastMessage(String.valueOf(Bukkit.isPrimaryThread()));
    }

}
