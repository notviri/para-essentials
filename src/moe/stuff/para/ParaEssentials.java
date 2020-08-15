package moe.stuff.para;
import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import moe.stuff.para.commands.CommandHelp;
import moe.stuff.para.commands.CommandKill;

public class ParaEssentials extends JavaPlugin {
    static final String CONFIG_FILENAME = "config.yml";

    public FileConfiguration config;

    CommandKill commandKill;

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

    public void setup(boolean reload) {
        this.getCommand("help").setExecutor(new CommandHelp(this));
        this.getCommand("kill").setExecutor(new CommandKill(this));
    }

    @Override
    public void reloadConfig() {
        File configFile = new File(this.getDataFolder(), CONFIG_FILENAME);
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(configFile);
        this.config = yamlConfig;
    }
}
