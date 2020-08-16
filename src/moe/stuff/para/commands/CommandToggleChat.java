package moe.stuff.para.commands;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import moe.stuff.para.ChatSettings;
import moe.stuff.para.ParaEssentials;

public class CommandToggleChat implements CommandExecutor {
    ParaEssentials pluginInstance;

    public CommandToggleChat(ParaEssentials pluginInstance) {
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
                if (settings.isChatDisabled()) {
                    settings.setChatDisabled(false);
                    player.sendMessage(ChatColor.GREEN + "Chat enabled! :)");
                } else {
                    settings.setChatDisabled(true);
                    player.sendMessage(ChatColor.RED + "Chat disabled! :(");
                }
            }
            this.pluginInstance.savePlayerdata();
        } else {
            sender.sendMessage(ChatColor.RED + "You're not a player.");
        }
        return true;
    }
}