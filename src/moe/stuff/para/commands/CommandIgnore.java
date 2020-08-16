package moe.stuff.para.commands;

import static moe.stuff.para.ChatSettings.SEPARATOR;
import static moe.stuff.para.ChatSettings.SEPARATOR2;
import static moe.stuff.para.ParaEssentials.USAGE;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
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
                boolean isIgnored = false;
                for (String p : settings.getIgnoredPlayers()) {
                    if (p.toLowerCase().equals(sanitized.toLowerCase())) {
                        isIgnored = true;
                    }
                }
                if (!isIgnored) {
                    if (settings.getIgnoredPlayers().size() > MAX_IGNORED_PLAYERS) {
                        player.sendMessage(ChatColor.RED + "You can't ignore more than " +
                                ChatColor.YELLOW + MAX_IGNORED_PLAYERS + ChatColor.RED + " players.");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(sanitized);
                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "Player " +
                                ChatColor.YELLOW + sanitized + ChatColor.RED + " is not online!");
                        return true;
                    }
                    settings.getIgnoredPlayers().add(target.getName());
                    player.sendMessage(ChatColor.GREEN + "Now ignoring " +
                            ChatColor.YELLOW + target.getName() + ChatColor.GREEN + ".");
                } else {
                    Iterator<String> ignored = settings.getIgnoredPlayers().iterator();
                    while (ignored.hasNext()) {
                        String playerName = ignored.next();
                        if (playerName.toLowerCase().equals(sanitized.toLowerCase())) {
                            ignored.remove();
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "No longer ignoring " +
                            ChatColor.YELLOW + sanitized + ChatColor.GREEN + ".");
                }
            }
            this.pluginInstance.savePlayerdata();
        }
        return true;
    }
}
