package moe.stuff.para.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import moe.stuff.para.ParaEssentials;

public class CommandReply implements CommandExecutor {
    ParaEssentials pluginInstance;

    public CommandReply(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            String lastPMTarget = this.pluginInstance.msgPairs.get(player.getName());
            if (lastPMTarget != null) {
                List<String> fwdArgs = new ArrayList<>();
                fwdArgs.add(lastPMTarget);
                fwdArgs.addAll(Arrays.asList(args));
                String[] fArgs = fwdArgs.toArray(new String[fwdArgs.size()]);
                this.pluginInstance.commandMessage.onCommand(sender, command, label, fArgs);
            } else {
                sender.sendMessage(ChatColor.RED + "You haven't talked to anyone yet.");
            }
        }
        return true;
    }
}
