package moe.stuff.para;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import moe.stuff.para.commands.CommandHelp;
import moe.stuff.para.commands.CommandIgnore;
import moe.stuff.para.commands.CommandIgnoreList;
import moe.stuff.para.commands.CommandKill;
import moe.stuff.para.commands.CommandMessage;
import moe.stuff.para.commands.CommandReply;

public class ParaEssentials extends JavaPlugin {
    public static final String USAGE = ChatColor.RED + "Usage: " + ChatColor.WHITE;
    static final String CONFIG_FILENAME = "config.yml";

    public FileConfiguration config;
    public ConcurrentHashMap<String, String> msgPairs; // <receiver, sender>
    public ConcurrentHashMap<String, ChatSettings> chatSettings; // <player, settings>

    public CommandHelp commandHelp;
    public CommandIgnore commandIgnore;
    public CommandIgnoreList commandIgnoreList;
    public CommandKill commandKill;
    public CommandMessage commandMessage;
    public CommandReply commandReply;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        this.setup(false);
    }

    @Override
    public void onDisable() {
        // TODO
    }

    public CommandHelp.CommandInfo getHelp(String command) {
        return this.commandHelp.getHelp().get(command);
    }

    public void setup(boolean isReload) {
        if (!isReload) {
            this.chatSettings = new ConcurrentHashMap<>();
            this.msgPairs = new ConcurrentHashMap<>();
        } else {
            HandlerList.unregisterAll(this);
        }

        this.commandHelp = new CommandHelp(this);
        this.commandIgnore = new CommandIgnore(this);
        this.commandIgnoreList = new CommandIgnoreList(this);
        this.commandKill = new CommandKill(this);
        this.commandMessage = new CommandMessage(this);
        this.commandReply = new CommandReply(this);

        this.getCommand("help").setExecutor(this.commandHelp);
        this.getCommand("ignore").setExecutor(this.commandIgnore);
        this.getCommand("ignorelist").setExecutor(this.commandIgnoreList);
        this.getCommand("msg").setExecutor(this.commandMessage);
        this.getCommand("kill").setExecutor(this.commandKill);
        this.getCommand("r").setExecutor(this.commandReply);

        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void reloadConfig() {
        File configFile = new File(this.getDataFolder(), CONFIG_FILENAME);
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(configFile);
        this.config = yamlConfig;
    }
}
