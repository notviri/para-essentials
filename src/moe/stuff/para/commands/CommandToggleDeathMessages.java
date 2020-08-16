package moe.stuff.para.commands;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import moe.stuff.para.ChatSettings;
import moe.stuff.para.ParaEssentials;

public class CommandToggleDeathMessages implements CommandExecutor  {
    ParaEssentials pluginInstance;

    public CommandToggleDeathMessages(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            ConcurrentHashMap<String, ChatSettings> chatSettings = this.pluginInstance.chatSettings;
            chatSettings.putIfAbsent(player.getName(), ChatSettings.getDefault());
            ChatSettings settings = chatSettings.get(player.getName());
            synchronized (settings) {
                if (settings.areDeathMessagesDisabled()) {
                    settings.setDeathMessagesDisabled(false);
                    player.sendMessage(ChatColor.GREEN + "Death messages enabled!");
                } else {
                    settings.setDeathMessagesDisabled(true);
                    player.sendMessage(ChatColor.RED + "Death messages disabled!");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You're not a player.");
        }
        return true;
    }
}
