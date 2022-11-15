package com.teenkung.afkreward;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements TabCompleter {
    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ArrayList<String> result = new ArrayList<>();
            result.add("status");
            if (player.hasPermission("afk-admin")) {
                result.add("set-time");
                result.add("add-time");
                result.add("remove-time");
            }
            return result;
        }
        return null;
    }
}
