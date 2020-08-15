package moe.stuff.para;
import org.bukkit.plugin.java.JavaPlugin;

import moe.stuff.para.commands.CommandKill;

public class ParaEssentials extends JavaPlugin {
    CommandKill commandKill;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.setup(false);
    }

    @Override
    public void onDisable() {
        // TODO
    }

    public void setup(boolean reload) {
        this.getCommand("kill").setExecutor(new CommandKill(this));
    }
}
