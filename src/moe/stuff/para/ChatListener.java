package moe.stuff.para;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.BroadcastMessageEvent;

public class ChatListener implements Listener {
    public static final String DEATH_PREFIX = "[death] ";

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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.broadcastMessage(DEATH_PREFIX + event.getDeathMessage());
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onBroadcast(BroadcastMessageEvent event) {
        if (!event.getMessage().startsWith(DEATH_PREFIX)) return;

        event.setMessage(event.getMessage().replace(DEATH_PREFIX, ""));
        ConcurrentHashMap<String, ChatSettings> chatSettings = this.pluginInstance.chatSettings;
        Set<CommandSender> recipients = event.getRecipients();
        Iterator<CommandSender> iter = recipients.iterator();
        while (iter.hasNext()) {
            CommandSender target = iter.next();
            if (target instanceof Player) {
                Player player = (Player)target;
                ChatSettings settings = chatSettings.get(player.getName());
                if (settings == null) continue;
                synchronized (settings) {
                    if (settings.areDeathMessagesDisabled()) {
                        iter.remove();
                    }
                }
            }
        }
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
