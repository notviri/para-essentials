package moe.stuff.para.commands;

import static moe.stuff.para.ChatSettings.SEPARATOR;
import static moe.stuff.para.ChatSettings.SEPARATOR2;
import static moe.stuff.para.ParaEssentials.USAGE;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import moe.stuff.para.ChatSettings;
import moe.stuff.para.ParaEssentials;

public class CommandIgnore implements CommandExecutor {
    static final Integer MAX_IGNORED_PLAYERS = 100;

    ParaEssentials pluginInstance;

    public CommandIgnore(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(USAGE + this.pluginInstance.getHelp("ignore").getUsage());
            return true;
        } else if (sender instanceof Player) {
            Player player = (Player)sender;
            ConcurrentHashMap<String, ChatSettings> chatSettings = this.pluginInstance.chatSettings;
            chatSettings.putIfAbsent(player.getName(), ChatSettings.getDefault());
            ChatSettings settings = chatSettings.get(player.getName());
            synchronized (settings) {
                String sanitized = args[0].replaceAll(Pattern.quote(SEPARATOR), "")
                        .replaceAll(Pattern.quote(SEPARATOR2), "");
                if (!settings.getIgnoredPlayers().contains(sanitized)) {
                    if (settings.getIgnoredPlayers().size() > MAX_IGNORED_PLAYERS) {
                        player.sendMessage(ChatColor.RED + "You can't ignore more than " +
                                ChatColor.YELLOW + MAX_IGNORED_PLAYERS + ChatColor.RED + " players.");
                        return true;
                    }
                    settings.getIgnoredPlayers().add(sanitized);
                    player.sendMessage(ChatColor.GREEN + "Now ignoring " +
                            ChatColor.YELLOW + sanitized + ChatColor.GREEN + ".");
                } else {
                    settings.getIgnoredPlayers().remove(sanitized);
                    player.sendMessage(ChatColor.GREEN + "No longer ignoring " +
                            ChatColor.YELLOW + sanitized + ChatColor.GREEN + ".");
                }
            }
        }
        return true;
    }
}
