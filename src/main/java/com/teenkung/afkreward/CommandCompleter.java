package com.teenkung.afkreward;

import com.teenkung.afkreward.Loader.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.teenkung.afkreward.AFKReward.colorize;

public class CommandCompleter implements TabCompleter {
    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (sender instanceof Player player) {
                ArrayList<String> result = new ArrayList<>();
                result.add("status");
                if (player.hasPermission("afk-admin")) {
                    result.add("set");
                    result.add("add");
                    result.add("subtract");
                    result.add("reload");
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("subtract")) {
                            for (Player loop : Bukkit.getOnlinePlayers()) {
                                if (((Player) sender).canSee(loop)) {
                                    result.add(loop.getName());
                                }
                            }
                        }
                    } else if (args.length > 2) {
                        if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("subtract")) {
                            if (args.length == 3) {
                                for (int i = 0; i <= 9; i++) {
                                    result.add(args[2] + i);
                                }
                            } else {
                                for (int i = 0; i <= 9; i++) {
                                    result.add(String.valueOf(i));
                                }
                            }
                        }
                    }
                }
                return result;
            }
        } catch (Exception e) {
            ArrayList<String> result = new ArrayList<>();
            result.add(colorize(ConfigLoader.getMessage("Unexpected-Error", "<prefix><GRADIENT:FB0000>Unexpected Error Occurred!. Please check server console for more details.</GRADIENT:840000>")));
            System.out.println(colorize("&cUnexpected Error Occurred While running plugin command!"));
            e.printStackTrace();
            System.out.println(colorize("&c============================= End of error ============================="));
            return result;
        }
        return null;
    }
}
