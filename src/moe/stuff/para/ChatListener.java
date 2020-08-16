package moe.stuff.para;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    ParaEssentials pluginInstance;
    String chatFormat;

    public ChatListener(ParaEssentials pluginInstance) {
        this.pluginInstance = pluginInstance;
        this.chatFormat = MessageFormat.parseFormat(this.pluginInstance.config.getString("message.chat"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat(this.chatFormat);
    }
}
