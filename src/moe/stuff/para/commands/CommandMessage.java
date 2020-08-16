package moe.stuff.para.commands;

import static moe.stuff.para.ParaEssentials.USAGE;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import moe.stuff.para.MessageFormat;
import moe.stuff.para.ParaEssentials;

public class CommandMessage implements CommandExecutor {
    ParaEssentials pluginInstance;

    String formatFrom, formatTo;

    public CommandMessage(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;
        this.formatFrom = this.pluginInstance.config.getString("message.receive");
        this.formatFrom = MessageFormat.parseFormat(this.formatFrom);
        this.formatTo = this.pluginInstance.config.getString("message.sent");
        this.formatTo = MessageFormat.parseFormat(this.formatTo);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            String msg = USAGE + this.pluginInstance.getHelp("msg").getUsage();
            sender.sendMessage(msg);
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            String senderDisplayName = "Server";
            if (sender instanceof Player) {
                Player player = (Player)sender;
                this.pluginInstance.msgPairs.put(target.getName(), player.getName());
                senderDisplayName = player.getName();
            }
            String[] messageWords = Arrays.copyOfRange(args, 1, args.length);
            String message = String.join(" ", messageWords);
            sender.sendMessage(String.format(this.formatTo, target.getName(), message));
            target.sendMessage(String.format(this.formatFrom, senderDisplayName, message));
        } else {
            String msg = ChatColor.RED + "Player " + ChatColor.GOLD + args[0] + ChatColor.RED + " isn't online.";
            sender.sendMessage(msg);
        }
        return true;
    }
}
