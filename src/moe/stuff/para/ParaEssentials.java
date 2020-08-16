package moe.stuff.para;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

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
import moe.stuff.para.commands.CommandToggleChat;
import moe.stuff.para.commands.CommandToggleDeathMessages;

public class ParaEssentials extends JavaPlugin {
    public static final String USAGE = ChatColor.RED + "Usage: " + ChatColor.WHITE;
    static final String CONFIG_FILENAME = "config.yml";
    static final String PLAYERDATA_FILENAME = "playerdata.file";

    public FileConfiguration config;
    public ConcurrentHashMap<String, String> msgPairs; // <receiver, sender>
    public ConcurrentHashMap<String, ChatSettings> chatSettings; // <player, settings>

    public CommandHelp commandHelp;
    public CommandIgnore commandIgnore;
    public CommandIgnoreList commandIgnoreList;
    public CommandKill commandKill;
    public CommandMessage commandMessage;
    public CommandReply commandReply;
    public CommandToggleChat commandToggleChat;
    public CommandToggleDeathMessages commandToggleDeathMessages;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        this.setup(false);
        this.loadPlayerdata();
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
        this.commandToggleChat = new CommandToggleChat(this);
        this.commandToggleDeathMessages = new CommandToggleDeathMessages(this);

        this.getCommand("help").setExecutor(this.commandHelp);
        this.getCommand("ignore").setExecutor(this.commandIgnore);
        this.getCommand("ignorelist").setExecutor(this.commandIgnoreList);
        this.getCommand("msg").setExecutor(this.commandMessage);
        this.getCommand("kill").setExecutor(this.commandKill);
        this.getCommand("r").setExecutor(this.commandReply);
        this.getCommand("togglechat").setExecutor(this.commandToggleChat);
        this.getCommand("toggledeathmsgs").setExecutor(this.commandToggleDeathMessages);

        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void reloadConfig() {
        File configFile = new File(this.getDataFolder(), CONFIG_FILENAME);
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(configFile);
        if (yamlConfig == null) {
            this.config = this.getConfig();
            this.saveDefaultConfig();
        }
        this.config = yamlConfig;
    }

    Object lock = new Object();
    public void loadPlayerdata() {
        synchronized (lock) {
            try {
                Path path = Paths.get(this.getDataFolder().getAbsolutePath(), PLAYERDATA_FILENAME);
                byte[] data = Files.readAllBytes(path);
                String string = new String(data, StandardCharsets.UTF_8);
                ConcurrentHashMap<String, ChatSettings> settings = new ConcurrentHashMap<>();
                String[] entries = string.split(Pattern.quote(","));
                ChatSettings def = ChatSettings.getDefault();
                for (String entry : entries) {
                    if (entry.length() == 0) continue;
                    ChatSettings.DecodeResult result = def.decode(entry);
                    if (result.success) {
                        settings.put(result.playerName, result.instance);
                    }
                }
                this.chatSettings = settings;
            } catch (Exception e) {
                this.chatSettings = new ConcurrentHashMap<>();
            }
        }
    }
    public void savePlayerdata() {
        synchronized (lock) {
            Path path = Paths.get(this.getDataFolder().getAbsolutePath(), PLAYERDATA_FILENAME);
            try {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, ChatSettings> entry : this.chatSettings.entrySet()) {
                    String playerName = entry.getKey();
                    ChatSettings settings = entry.getValue();
                    synchronized (settings) {
                        sb.append(settings.encode(playerName));
                    }
                    sb.append(',');
                }
                String string = sb.toString();
                Files.write(path, string.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.err.println("Failed to write playerdata file.");
                e.printStackTrace();
            }
        }
    }
}
