package com.teenkung.afkreward;

import com.teenkung.afkreward.Loader.ConfigLoader;
import com.teenkung.afkreward.Utils.PlayerTimer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.teenkung.afkreward.AFKReward.colorize;

public class CommandListener implements CommandExecutor {
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if (args[0].equalsIgnoreCase("status")) {

                if (args.length > 1) {
                    if (sender.hasPermission("afk-admin")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            if (target.isOnline()) {
                                if (PlayerTimer.isInAFKRegion(target)) {
                                    int seconds = PlayerTimer.getTimer(target);
                                    sender.sendMessage(colorize(ConfigLoader.getMessage("AFK-Notifications-Player", "<prefix><GRADIENT:00FFFF>Player <player> are now AFK for <seconds> seconds</GRADIENT:00FF88>")
                                        .replaceAll("<player>", target.getName())
                                        .replaceAll("<seconds>", String.valueOf(seconds))
                                        .replaceAll("<second>", String.valueOf(seconds % 60))
                                        .replaceAll("<minute>", String.valueOf(Math.floor(seconds / 60D)))
                                        .replaceAll("<hour>", String.valueOf(Math.floor(seconds / 3600D)))));

                                } else {
                                    sender.sendMessage(colorize(ConfigLoader.getMessage("Not-in-Area-Player", "<prefix><GRADIENT:00FFFF>Player <player> is not in AFK Reward area</GRADIENT:00FF88>")));
                                }
                            } else {
                                sender.sendMessage(colorize(ConfigLoader.getMessage("Player-Not-Found", "<prefix><GRADIENT:FB0000>Player not Found or not Online in the current Server!</GRADIENT:840000>")));
                            }
                        } else {
                            sender.sendMessage(colorize(ConfigLoader.getMessage("Player-Not-Found", "<prefix><GRADIENT:FB0000>Player not Found or not Online in the current Server!</GRADIENT:840000>")));
                        }
                    } else {
                        sender.sendMessage(colorize(ConfigLoader.getMessage("Permission-Denied", "<prefix><GRADIENT:FB0000>You don't have permission to use this command!</GRADIENT:840000>")));
                    }
                }

                if (sender instanceof Player player) {
                    if (PlayerTimer.isInAFKRegion(player)) {
                        int seconds = PlayerTimer.getTimer(player);
                        player.sendMessage(colorize(ConfigLoader.getMessage("AFK-Notifications", "<prefix><GRADIENT:00FFFF>You are now AFK for <seconds> seconds</GRADIENT:00FF88>")
                            .replaceAll("<seconds>", String.valueOf(seconds))
                            .replaceAll("<second>", String.valueOf(seconds % 60))
                            .replaceAll("<minute>", String.valueOf(Math.floor(seconds / 60D)))
                            .replaceAll("<hour>", String.valueOf(Math.floor(seconds / 3600D)))));
                    } else {
                        player.sendMessage(colorize(ConfigLoader.getMessage("Not-in-Area", "<prefix><GRADIENT:00FFFF>You are not in AFK Reward area</GRADIENT:00FF88>")));
                    }
                }
            }
            else if (sender.hasPermission("afk-admin")) {
                /*TODO
                *   - set time command
                *   - add time command
                *   - remove time command
                *   - reload command
                * */
                if (args[0].equalsIgnoreCase("set-time")) {
                    return false;
                } else if (args[0].equalsIgnoreCase("add-time")) {
                    return false;
                } else if (args[0].equalsIgnoreCase("remove-time")) {
                    return false;
                }
            } else {
                sender.sendMessage(colorize(ConfigLoader.getMessage("Invalid-Arguments", "<prefix<GRADIENT:FB0000>Invalid Argument. Please Check Spelling or tab complete!</GRADIENT:840000>")));
            }

        return false;
    }
}
