package com.teenkung.afkreward;

import org.bukkit.plugin.java.JavaPlugin;

public final class AFKReward extends JavaPlugin {

    private static AFKReward Instance;
    @Override
    public void onEnable() {
        Instance = this;
        timer.startTimer();
    }

    @Override
    public void onDisable() {
    }

    //getInstance class
    public static AFKReward getInstance() {
        return Instance;
    }

    
}
