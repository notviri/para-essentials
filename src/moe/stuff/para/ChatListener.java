package moe.stuff.para;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void filterChat(AsyncPlayerChatEvent event) {
        ConcurrentHashMap<String, ChatSettings> chatSettings = this.pluginInstance.chatSettings;
        Set<Player> recipients = event.getRecipients();
        String playerName = event.getPlayer().getName();
        Iterator<Player> iter = recipients.iterator();
        while (iter.hasNext()) {
            Player target = iter.next();
            ChatSettings settings = chatSettings.get(target.getName());
            if (settings == null) continue;
            synchronized (settings) {
                if (settings.isChatDisabled() || settings.getIgnoredPlayers().contains(playerName)) {
                    iter.remove();
                }
            }
        }
    }
}
