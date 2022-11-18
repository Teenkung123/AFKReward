package com.teenkung.afkreward;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.teenkung.afkreward.Loader.ConfigLoader;
import com.teenkung.afkreward.Utils.PlayerTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AFKReward extends JavaPlugin {

    private static AFKReward Instance;
    @Override
    public void onEnable() {
        Instance = this;
        PlayerTimer.startTimer();

        ConfigLoader.generateConfig();
        ConfigLoader.loadConfig(false);

        //Register Plugin Event Handler
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);

        //register Plugin Commands
        Objects.requireNonNull(getCommand("afkreward")).setExecutor(new CommandListener());
        Objects.requireNonNull(getCommand("afkreward")).setTabCompleter(new CommandCompleter());//load Worldguard API

    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getScheduler().cancelTasks(this);
    }

    //getInstance class
    public static AFKReward getInstance() {
        return Instance;
    }

    public static String colorize(String message) {
        if (message == null) {
            message = "";
        }
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return IridiumColorAPI.process(ChatColor.translateAlternateColorCodes('&', message));
    }
}
