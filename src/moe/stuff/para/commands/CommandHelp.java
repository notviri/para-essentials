package moe.stuff.para.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import moe.stuff.para.ParaEssentials;

public class CommandHelp implements CommandExecutor {
    public class CommandInfo {
        String usage, desc;

        public String getUsage() {
            return usage;
        }

        public String getDesc() {
            return desc;
        }
    }

    ParaEssentials pluginInstance;
    SortedMap<String, CommandInfo> help;
    String[] helpMessage;

    public CommandHelp(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;

        String sepLines = "---------------------------------------------";
        String separator = "" + ChatColor.GOLD + ChatColor.BOLD + sepLines;

        // we cache the help message here
        ArrayList<String> helpMessage = new ArrayList<>();
        helpMessage.add(separator);
        Map<String, Object> commands = pluginInstance.config
                .getConfigurationSection("help")
                .getValues(true);
        TreeMap<String, CommandInfo> commandInfo = new TreeMap<>();
        for (Map.Entry<String, Object> entry : commands.entrySet()) {
            if (!(entry.getKey().contains("."))) continue;
            String[] split = entry.getKey().split("\\.");
            commandInfo.putIfAbsent(split[0], new CommandInfo());
            CommandInfo info = commandInfo.get(split[0]);
            if (split[1].equals("usage")) {
                info.usage = entry.getValue().toString().replace("<command>", split[0]);
            } else if (split[1].equals("desc")) {
                info.desc = entry.getValue().toString();
            }
        }
        for (CommandInfo info : commandInfo.values()) {
            helpMessage.add("" + ChatColor.GOLD + info.usage + ": " + ChatColor.WHITE + info.desc);
        }
        helpMessage.add(separator);
        this.helpMessage = helpMessage.toArray(new String[helpMessage.size()]);
        this.help = commandInfo;
    }

    public SortedMap<String, CommandInfo> getHelp() {
        return this.help;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(this.helpMessage);
        return true;
    }
}
