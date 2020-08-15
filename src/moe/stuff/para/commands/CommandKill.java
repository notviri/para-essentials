package moe.stuff.para.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import moe.stuff.para.ParaEssentials;

public class CommandKill implements CommandExecutor {
    ParaEssentials pluginInstance;

    public CommandKill(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 0) {
            Player player = (Player)sender;
            player.setHealth(0.0);
            return true;
        } else if (sender instanceof ServerOperator && args.length >= 1) {
            ServerOperator operator = (ServerOperator)sender;
            if (operator.isOp()) {
                for (String arg : args) {
                    Player target = Bukkit.getPlayer(arg);
                    if (target != null) {
                        String msg = ChatColor.GREEN + "Killed " + ChatColor.YELLOW + arg + ChatColor.GREEN + ".";
                        target.setHealth(0.0);
                        sender.sendMessage(msg);
                    } else {
                        String msg = ChatColor.RED + "Couldn't find " + ChatColor.YELLOW + arg + ChatColor.GREEN + ".";
                        sender.sendMessage(msg);
                    }
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You can't do that.");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can't kill yourself.");
            return true;
        }
    }
}
