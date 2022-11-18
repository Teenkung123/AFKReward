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
        try {
            if (args.length == 0) {
                if (sender.hasPermission("afk-admin")) {
                    sender.sendMessage(colorize(ConfigLoader.getMessage("Command-Usage-Admin", "<prefix><GRADIENT:00FFFF>Usage: /afkreward (<reload>)(<status> <player>)(<add/subtract/set> <player> <value>)</GRADIENT:00FF88>")));
                } else {
                    sender.sendMessage(colorize(ConfigLoader.getMessage("Command-Usage", "<prefix><GRADIENT:00FFFF>Usage: /afkreward status </GRADIENT:00FF88>")));
                }
                return false;
            } else {
                if (args[0].equalsIgnoreCase("status")) {
                    if (args.length > 1) {
                        if (sender.hasPermission("afk-admin")) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                if (target.isOnline()) {
                                    if (PlayerTimer.isInAllAFKRegion(target)) {
                                        int seconds = PlayerTimer.getTimer(target);
                                        sender.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("AFK-Notifications-Player", "<prefix><GRADIENT:00FFFF>Player <player> are now AFK for <seconds> seconds</GRADIENT:00FF88>"), target, seconds)));
                                    } else {
                                        sender.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Not-in-Area-Player", "<prefix><GRADIENT:00FFFF>Player <player> is not in AFK Reward area</GRADIENT:00FF88>"), target, 0)));
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
                    } else {
                        if (sender instanceof Player player) {
                            if (PlayerTimer.isInAllAFKRegion(player)) {
                                int seconds = PlayerTimer.getTimer(player);
                                player.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("AFK-Notifications", "<prefix><GRADIENT:00FFFF>You are now AFK for <seconds> seconds</GRADIENT:00FF88>"), player, seconds)));
                            } else {
                                player.sendMessage(colorize(ConfigLoader.getMessage("Not-in-Area", "<prefix><GRADIENT:00FFFF>You are not in AFK Reward area</GRADIENT:00FF88>")));
                            }
                        }
                    }
                } else if (sender.hasPermission("afk-admin")) {
                    if (args[0].equalsIgnoreCase("set")) {
                        Player target = checkArgs(args, sender);
                        if (target != null) {
                            try {
                                int seconds = Integer.parseInt(args[2]);
                                PlayerTimer.setTimer(target, seconds);
                                sender.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Modify-Set", "<prefix><GRADIENT:00FFFF>Successfully set <player>'s PlayerTimer to <hour>h <minute>m <second>s</GRADIENT:00FF88>"), target, seconds)));
                                target.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Modify-Notify", "<prefix><GRADIENT:00FFFF>An admin has set your PlayerTimer to <hour>h <minute>m <second>s</GRADIENT:00FF88>"), target, seconds)));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(colorize(ConfigLoader.getMessage("Invalid-Arguments", "<prefix<GRADIENT:FB0000>Invalid Argument. Please Check Spelling or tab complete!</GRADIENT:840000>")));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("add")) {
                        Player target = checkArgs(args, sender);
                        if (target != null) {
                            try {
                                int seconds = Integer.parseInt(args[2]);
                                PlayerTimer.setTimer(target, PlayerTimer.getTimer(target) + seconds);
                                sender.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Modify-Add", "<prefix><GRADIENT:00FFFF>Successfully added <hour>h <minute>m <second>s to PlayerTimer of <player></GRADIENT:00FF88>"), target, seconds)));
                                target.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Modify-Notify", "<prefix><GRADIENT:00FFFF>An admin has set your PlayerTimer to <hour>h <minute>m <second>s</GRADIENT:00FF88>"), target, seconds)));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(colorize(ConfigLoader.getMessage("Invalid-Arguments", "<prefix<GRADIENT:FB0000>Invalid Argument. Please Check Spelling or tab complete!</GRADIENT:840000>")));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("subtract")) {
                        Player target = checkArgs(args, sender);
                        if (target != null) {
                            try {
                                int seconds = Integer.parseInt(args[2]);
                                PlayerTimer.setTimer(target, PlayerTimer.getTimer(target) - seconds);
                                sender.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Modify-Subtract", "<prefix><GRADIENT:00FFFF>Successfully subtract <hour>h <minute>m <second>s from PlayerTimer of <player></GRADIENT:00FF88>"), target, seconds)));
                                target.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Modify-Notify", "<prefix><GRADIENT:00FFFF>An admin has set your PlayerTimer to <hour>h <minute>m <second>s</GRADIENT:00FF88>"), target, seconds)));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(colorize(ConfigLoader.getMessage("Invalid-Arguments", "<prefix<GRADIENT:FB0000>Invalid Argument. Please Check Spelling or tab complete!</GRADIENT:840000>")));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        sender.sendMessage(colorize(ConfigLoader.getMessage("Reloading", "<prefix><GRADIENT:E5FF67>Reloading Configuration. . .</GRADIENT:FD85FC>")));
                        ConfigLoader.loadConfig(true);
                        sender.sendMessage(colorize(ConfigLoader.getMessage("Reloaded", "<prefix><GRADIENT:E5FF67>Successfully reloaded configuration</GRADIENT:FD85FC>")));
                    }
                } else {
                    sender.sendMessage(colorize(ConfigLoader.getMessage("Invalid-Arguments", "<prefix<GRADIENT:FB0000>Invalid Argument. Please Check Spelling or tab complete!</GRADIENT:840000>")));
                }
            }
        } catch (Exception e) {
            sender.sendMessage(colorize(ConfigLoader.getMessage("Unexpected-Error", "<prefix><GRADIENT:FB0000>Unexpected Error Occurred!. Please check server console for more details.</GRADIENT:840000>")));
            System.out.println(colorize("&cUnexpected Error Occurred While running plugin command!"));
            e.printStackTrace();
            System.out.println(colorize("&c============================= End of error ============================="));
        }
        return false;
    }


    private Player checkArgs(String[] args, CommandSender sender) {
        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                if (target.isOnline()) {
                    if (PlayerTimer.isInAllAFKRegion(target)) {
                        return target;
                    } else {
                        sender.sendMessage(colorize(replacePlaceholder(ConfigLoader.getMessage("Not-in-Area-Player", "<prefix><GRADIENT:00FFFF>Player <player> is not in AFK Reward area</GRADIENT:00FF88>"), target, 0)));
                    }
                } else {
                    sender.sendMessage(colorize(ConfigLoader.getMessage("Player-Not-Found", "<prefix><GRADIENT:FB0000>Player not Found or not Online in the current Server!</GRADIENT:840000>")));
                }
            } else {
                sender.sendMessage(colorize(ConfigLoader.getMessage("Player-Not-Found", "<prefix><GRADIENT:FB0000>Player not Found or not Online in the current Server!</GRADIENT:840000>")));
            }
        } else {
            sender.sendMessage(colorize(ConfigLoader.getMessage("Invalid-Arguments", "<prefix<GRADIENT:FB0000>Invalid Argument. Please Check Spelling or tab complete!</GRADIENT:840000>")));
        }
        return null;
    }

    private String replacePlaceholder(String str, Player target, Integer seconds) {
        return str.replaceAll("<player>", target.getName())
                .replaceAll("<seconds>", String.valueOf(seconds))
                .replaceAll("<second>", String.valueOf(seconds % 60))
                .replaceAll("<minute>", String.valueOf(Math.floor(seconds / 60D)))
                .replaceAll("<hour>", String.valueOf(Math.floor(seconds / 3600D)));
    }
}
