package moe.stuff.para.commands;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import moe.stuff.para.ChatSettings;
import moe.stuff.para.ParaEssentials;

public class CommandIgnoreList implements CommandExecutor {
    ParaEssentials pluginInstance;

    public CommandIgnoreList(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            ConcurrentHashMap<String, ChatSettings> chatSettings = this.pluginInstance.chatSettings;
            ChatSettings settings = chatSettings.get(player.getName());
            if (settings == null) {
                player.sendMessage(ChatColor.GREEN + "You aren't ignoring anybody!");
                return true;
            }
            synchronized (settings) {
                if (settings != null && !settings.getIgnoredPlayers().isEmpty()) {
                    String msg = ChatColor.BLUE + "Ignored players: ";
                    player.sendMessage(msg + String.join(", ", settings.getIgnoredPlayers()));
                } else {
                    player.sendMessage(ChatColor.GREEN + "You aren't ignoring anybody!");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You're not a player.");
        }
        return true;
    }
}
