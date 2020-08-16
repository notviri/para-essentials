package moe.stuff.para;

import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Pattern;

public class ChatSettings {
    public static final String SEPARATOR = "-";
    public static final String SEPARATOR2 = "|";

    public class DecodeResult {
        public boolean success;
        public ChatSettings instance;
        public String playerName;
        public DecodeResult(boolean success, ChatSettings instance, String playerName) {
            this.success = success;
            this.instance = instance;
            this.playerName = playerName;
        }
    }

    // fields
    boolean chatDisabled, deathMessagesDisabled;
    HashSet<String> ignoredPlayers;

    public ChatSettings(boolean chatDisabled, boolean deathMessagesDisabled, HashSet<String> ignoredPlayers) {
        this.chatDisabled = chatDisabled;
        this.deathMessagesDisabled = deathMessagesDisabled;
        this.ignoredPlayers = ignoredPlayers;
    }

    public static ChatSettings getDefault() {
        return new ChatSettings(false, false, new HashSet<>());
    }

    public String encode(String playerName) {
        return playerName + SEPARATOR +
                Boolean.toString(this.chatDisabled) + SEPARATOR +
                Boolean.toString(this.deathMessagesDisabled) + SEPARATOR +
                String.join(SEPARATOR2, this.ignoredPlayers);
    }

    public DecodeResult decode(String encoded) {
        ChatSettings instance = new ChatSettings(false, false, null);
        String[] split = encoded.split(Pattern.quote(SEPARATOR));
        if (split.length == 3 || split.length == 4) {
            String playerName = split[0];
            instance.chatDisabled = Boolean.valueOf(split[1]);
            instance.deathMessagesDisabled = Boolean.valueOf(split[2]);
            if (split.length == 4) {
                instance.ignoredPlayers = new HashSet<>(Arrays.asList(split[3].split(Pattern.quote(SEPARATOR2))));
            } else {
                instance.ignoredPlayers = new HashSet<>();
            }
            return new DecodeResult(true, instance, playerName);
        } else {
            return new DecodeResult(false, null, null);
        }
    }

    public boolean isChatDisabled() {
        return chatDisabled;
    }

    public void setChatDisabled(boolean chatDisabled) {
        this.chatDisabled = chatDisabled;
    }

    public boolean areDeathMessagesDisabled() {
        return deathMessagesDisabled;
    }

    public void setDeathMessagesDisabled(boolean deathMessagesDisabled) {
        this.deathMessagesDisabled = deathMessagesDisabled;
    }

    public HashSet<String> getIgnoredPlayers() {
        return ignoredPlayers;
    }
}
